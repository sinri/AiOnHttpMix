package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIServiceAdapter;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.request.GPTRequest;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.stream.GPTResponseBuffer;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.stream.GPTResponseChunk;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.stream.GPTResponseFragment;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.sync.GPTResponse;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.function.Function;

public class GPTKit {
    public Future<JsonObject> chat(
            OpenAIServiceAdapter serviceAdapter,
            ChatModel chatModel,
            JsonObject rawRequest,
            String requestId) {
        return serviceAdapter.request(
                chatModel,
                rawRequest,
                requestId);
    }

    public Future<GPTResponse> chat(
            OpenAIServiceAdapter serviceAdapter,
            ChatModel chatModel,
            GPTRequest request,
            String requestId) {
        return serviceAdapter.request(
                                     chatModel,
                                     request.toJsonObject(),
                                     requestId)
                             .compose(jsonObject -> {
                                 return Future.succeededFuture(GPTResponse.wrap(jsonObject));
                             });
    }

    public Future<Void> chatStream(
            OpenAIServiceAdapter serviceAdapter,
            ChatModel chatModel,
            JsonObject requestPayload,
            Function<String, Future<Void>> cutterProcessFunc,
            long cutterTimeout,
            String requestId) {
        requestPayload.put("stream", true);
        return serviceAdapter.requestStream(
                chatModel,
                requestPayload,
                cutterProcessFunc,
                cutterTimeout,
                requestId);
    }

    public Future<Void> chatStream(
            OpenAIServiceAdapter serviceAdapter,
            ChatModel chatModel,
            GPTRequest request,
            Function<GPTResponseChunk, Future<Void>> cutterProcessFunc,
            long cutterTimeout,
            String requestId) {
        request.stream(true);
        return chatStream(
                serviceAdapter,
                chatModel,
                request.toJsonObject(),
                fragment -> {
                    try {
                        GPTResponseFragment f = GPTResponseFragment.wrap(fragment);
                        JsonObject data = f.getData();
                        if (data != null) {
                            GPTResponseChunk chunk = GPTResponseChunk.wrap(data);
                            return cutterProcessFunc.apply(chunk);
                        } else {
                            AigcMix.getVerboseLogger().warning("data in fragment is parsed to null");
                            return Future.succeededFuture();
                        }
                    } catch (Throwable e) {
                        AigcMix.getVerboseLogger().exception(e);
                        return Future.failedFuture(e);
                    }
                },
                cutterTimeout,
                requestId);
    }

    public Future<GPTResponse> chatStream(
            OpenAIServiceAdapter serviceAdapter,
            ChatModel chatModel,
            GPTRequest request,
            long cutterTimeout,
            String requestId
    ) {
        GPTResponseBuffer buffer = new GPTResponseBuffer();
        return chatStream(
                serviceAdapter,
                chatModel,
                request,
                chunk -> {
                    buffer.accept(chunk);
                    return Future.succeededFuture();
                },
                cutterTimeout,
                requestId
        )
                .compose(v -> {
                    return Future.succeededFuture(buffer.build());
                });

    }
}
