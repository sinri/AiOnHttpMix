package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.stream.QwenResponseBuffer;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.stream.QwenResponseChunk;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.stream.QwenResponseFragment;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.sync.QwenResponse;
import io.github.sinri.AiOnHttpMix.utils.models.dashscope.qwen.QwenModelSeries;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.function.Function;

/**
 * @since 2.0.0
 */
public class QwenKit {
    public QwenKit() {

    }

    public Future<JsonObject> chat(
            QwenServiceAdapter qwenServiceAdapter,
            QwenModelSeries chatModel,
            JsonObject rawRequest,
            String requestId) {
        return qwenServiceAdapter.request(chatModel, rawRequest, requestId);
    }

    public Future<QwenResponse> chat(
            QwenServiceAdapter qwenServiceAdapter,
            QwenModelSeries chatModel,
            QwenRequest request,
            String requestId) {
        return qwenServiceAdapter.request(chatModel, request.toJsonObject(), requestId)
                .compose(rawResponse -> {
                    return Future.succeededFuture(QwenResponse.wrap(rawResponse));
                });
    }

    public Future<Void> chatStream(
            QwenServiceAdapter qwenServiceAdapter,
            QwenModelSeries chatModel,
            JsonObject rawRequest,
            Function<String, Future<Void>> chunkProcessFunc,
            long cutterTimeout,
            String requestId) {
        return qwenServiceAdapter.requestStream(chatModel, rawRequest, chunkProcessFunc, cutterTimeout, requestId);
    }

    public Future<Void> chatStream(
            QwenServiceAdapter qwenServiceAdapter,
            QwenModelSeries chatModel,
            QwenRequest request,
            Function<QwenResponseChunk, Future<Void>> chunkProcessFunc,
            long cutterTimeout,
            String requestId) {
        request.parameters(p -> p.stream(true).incrementalOutput(true));
        return chatStream(qwenServiceAdapter, chatModel, request.toJsonObject(), s -> {
            try {
                QwenResponseFragment fragment = QwenResponseFragment.wrap(s);
                JsonObject data = fragment.getData();
                return chunkProcessFunc.apply(QwenResponseChunk.wrap(data));
            } catch (Throwable throwable) {
                AigcMix.getVerboseLogger().exception(throwable, "chunk parse error");
                return Future.failedFuture(throwable);
            }
        }, cutterTimeout, requestId);
    }

    public Future<QwenResponse> chatStream(
            QwenServiceAdapter qwenServiceAdapter,
            QwenModelSeries chatModel,
            QwenRequest request,
            long cutterTimeout,
            String requestId) {
        request.parameters(p -> p.stream(true).incrementalOutput(true));

        QwenResponseBuffer qwenResponseBuffer = new QwenResponseBuffer();

        return chatStream(qwenServiceAdapter, chatModel, request.toJsonObject(), s -> {
            try {
                QwenResponseFragment fragment = QwenResponseFragment.wrap(s);
                JsonObject data = fragment.getData();
                var chunk = QwenResponseChunk.wrap(data);
                qwenResponseBuffer.accept(chunk);
                return Future.succeededFuture();
            } catch (Throwable throwable) {
                AigcMix.getVerboseLogger().exception(throwable, "chunk parse error");
                return Future.failedFuture(throwable);
            }
        }, cutterTimeout, requestId)
                .compose(v -> {
                    return Future.succeededFuture(qwenResponseBuffer.toQwenResponse());
                });
    }
}
