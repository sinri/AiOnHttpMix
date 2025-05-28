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

    public static Future<QwenResponseChunk> parseStreamFragmentToChunk(String fragment) {
        QwenResponseFragment qwenResponseFragment = QwenResponseFragment.wrap(fragment);
        JsonObject data = qwenResponseFragment.getData();
        QwenResponseChunk chunk = QwenResponseChunk.wrap(data);
        return Future.succeededFuture(chunk);
    }

    public static Future<Void> handleStreamFragment(String fragment, Function<QwenResponseChunk, Future<Void>> chunkProcessFunc) {
        return Future.succeededFuture()
                     .compose(v -> {
                         return parseStreamFragmentToChunk(fragment);
                     })
                     .compose(chunk -> {
                         return chunkProcessFunc.apply(chunk);
                     })
                     .onFailure(throwable -> {
                         AigcMix.getVerboseLogger().exception(throwable, "chunk parse error");
                     });
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
                                  .compose(rawResponse -> Future.succeededFuture(QwenResponse.wrap(rawResponse)));
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
        return chatStream(chatModel, request.toJsonObject(), s -> handleStreamFragment(s, chunkProcessFunc), cutterTimeout, requestId);
    }

    @Override
    public Future<QwenResponse> chatStream(
            ChatModel chatModel,
            QwenRequest request,
            long cutterTimeout,
            String requestId
    ) {
        request.parameters(p -> p.stream(true).incrementalOutput(true));

        QwenResponseBuffer qwenResponseBuffer = new QwenResponseBuffer();

        return chatStream(
                chatModel,
                request,
                chunk -> {
                    qwenResponseBuffer.accept(chunk);
                    return Future.succeededFuture();
                },
                cutterTimeout,
                requestId
        )
                .compose(v -> Future.succeededFuture(qwenResponseBuffer.build()));
    }
}
