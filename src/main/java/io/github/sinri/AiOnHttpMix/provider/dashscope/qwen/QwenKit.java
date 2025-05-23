package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.stream.QwenResponseBuffer;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.stream.QwenResponseChunk;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.stream.QwenResponseFragment;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.sync.QwenResponse;
import io.github.sinri.AiOnHttpMix.utils.ServiceKit;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.function.Function;

/**
 * @since 2.0.0
 */
public class QwenKit implements ServiceKit<QwenRequest, QwenResponse, QwenResponseChunk> {
    private final QwenServiceAdapter serviceAdapter;

    public QwenKit(QwenServiceAdapter serviceAdapter) {
        this.serviceAdapter = serviceAdapter;
    }

    @Override
    public QwenServiceAdapter getServiceAdapter() {
        return serviceAdapter;
    }

    @Override
    public Future<JsonObject> chat(
            ChatModel chatModel,
            JsonObject rawRequest,
            String requestId) {
        return getServiceAdapter().request(chatModel, rawRequest, requestId);
    }


    @Override
    public Future<QwenResponse> chat(
            ChatModel chatModel,
            QwenRequest request,
            String requestId) {
        return getServiceAdapter().request(chatModel, request.toJsonObject(), requestId)
                                  .compose(rawResponse -> {
                                      return Future.succeededFuture(QwenResponse.wrap(rawResponse));
                                  });
    }

    @Override
    public Future<Void> chatStream(
            ChatModel chatModel,
            JsonObject rawRequest,
            Function<String, Future<Void>> chunkProcessFunc,
            long cutterTimeout,
            String requestId) {
        return getServiceAdapter().requestStream(chatModel, rawRequest, chunkProcessFunc, cutterTimeout, requestId);
    }

    @Override
    public Future<Void> chatStream(
            ChatModel chatModel,
            QwenRequest request,
            Function<QwenResponseChunk, Future<Void>> chunkProcessFunc,
            long cutterTimeout,
            String requestId) {
        request.parameters(p -> p.stream(true).incrementalOutput(true));
        return chatStream(chatModel, request.toJsonObject(), s -> {
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

    @Override
    public Future<QwenResponse> chatStream(
            ChatModel chatModel,
            QwenRequest request,
            long cutterTimeout,
            String requestId) {
        request.parameters(p -> p.stream(true).incrementalOutput(true));

        QwenResponseBuffer qwenResponseBuffer = new QwenResponseBuffer();

        return chatStream(chatModel, request.toJsonObject(), s -> {
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
