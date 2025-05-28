package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIServiceAdapter;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.core.filter.OpenAIPromptFilterResults;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.request.GPTRequest;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.stream.GPTResponseBuffer;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.stream.GPTResponseChunk;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.stream.GPTResponseFragment;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.sync.GPTResponse;
import io.github.sinri.AiOnHttpMix.utils.FilteredRequest;
import io.github.sinri.AiOnHttpMix.utils.ServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.ServiceKit;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.function.Function;

public class GPTKit implements ServiceKit<GPTRequest, GPTResponse, GPTResponseChunk> {

    private final OpenAIServiceAdapter serviceAdapter;

    public GPTKit(OpenAIServiceAdapter serviceAdapter) {
        this.serviceAdapter = serviceAdapter;
    }

    public static Future<GPTResponseChunk> parseStreamFragmentToChunk(String fragment) {
        AigcMix.getVerboseLogger().info("fragment:\n" + fragment);
        GPTResponseFragment f = GPTResponseFragment.wrap(fragment);
        JsonObject data = f.getData();
        if (data == null) return Future.succeededFuture(null);
        GPTResponseChunk chunk = GPTResponseChunk.wrap(data);
        return Future.succeededFuture(chunk);
    }

    public static Future<Void> handleStreamFragment(String fragment, Function<GPTResponseChunk, Future<Void>> cutterProcessFunc) {
        return Future.succeededFuture()
                     .compose(v -> {
                         return parseStreamFragmentToChunk(fragment);
                     })
                     .compose(chunk -> {
                         if (chunk == null) {
                             AigcMix.getVerboseLogger().warning("data in fragment is parsed to null");
                             return Future.succeededFuture();
                         } else {
                             return cutterProcessFunc.apply(chunk);
                         }
                     })
                     .onFailure(e -> {
                         AigcMix.getVerboseLogger().exception(e);
                     });
    }

    @Override
    public ServiceAdapter getServiceAdapter() {
        return serviceAdapter;
    }

    @Override
    public Future<GPTResponse> chat(
            ChatModel chatModel,
            GPTRequest request,
            String requestId) {
        return serviceAdapter.request(
                                     chatModel,
                                     request.toJsonObject(),
                                     requestId)
                             .compose(jsonObject -> Future.succeededFuture(GPTResponse.wrap(jsonObject)))
                             .compose(resp -> {
                                 List<OpenAIPromptFilterResults> promptFilterResults = resp.getPromptFilterResults();
                                 if (promptFilterResults.stream()
                                                        .filter(pfr -> pfr.getContentFilterResults()
                                                                          .whetherFiltered())
                                                        .findFirst()
                                                        .isEmpty()) {
                                     return Future.succeededFuture(resp);
                                 } else {
                                     throw new FilteredRequest();
                                 }
                             });
    }

    @Override
    public Future<Void> chatStream(
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

    @Override
    public Future<Void> chatStream(
            ChatModel chatModel,
            GPTRequest request,
            Function<GPTResponseChunk, Future<Void>> cutterProcessFunc,
            long cutterTimeout,
            String requestId) {
        request.stream(true);
        return chatStream(
                chatModel,
                request.toJsonObject(),
                fragment -> handleStreamFragment(fragment, cutterProcessFunc),
                cutterTimeout,
                requestId);
    }

    @Override
    public Future<GPTResponse> chatStream(
            ChatModel chatModel,
            GPTRequest request,
            long cutterTimeout,
            String requestId
    ) {
        GPTResponseBuffer buffer = new GPTResponseBuffer();
        return chatStream(
                chatModel,
                request,
                chunk -> {
                    AigcMix.getVerboseLogger().info("chunk:", chunk.cloneAsJsonObject());
                    buffer.accept(chunk);
                    return Future.succeededFuture();
                },
                cutterTimeout,
                requestId
        )
                .compose(v -> Future.succeededFuture(buffer.build()));
    }
}
