package io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.utils.AbnormalResponse;
import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.model.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;
import io.github.sinri.AiOnHttpMix.utils.series.ChatModelSeries;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class ChatGPTServiceAdapter implements ChatModelServiceAdapter {
    private final static Set<ChatModelSeries> supportedChatModelSeriesSet = new HashSet<>();

    static {
        supportedChatModelSeriesSet.add(ChatModelSeries.chatgpt);
    }

    private final String apiKey;
    private final String resourceName;
    private final String deployment;
    private final String apiVersion;

    public ChatGPTServiceAdapter(String apiKey, String resourceName, String deployment, String apiVersion) {
        this.apiKey = apiKey;
        this.resourceName = resourceName;
        this.deployment = deployment;
        this.apiVersion = apiVersion;
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return ServiceProvider.azureOpenAI;
    }

    @Override
    public Set<ChatModelSeries> getChatModelSeries() {
        return supportedChatModelSeriesSet;
    }

    private String generateHost() {
        return resourceName + ".openai.azure.com";
    }

    private String generateUri(String api) {
        return "/openai/deployments/" + deployment + api + "?api-version=" + apiVersion;
    }

    /**
     * @param api start with a slash `/`
     */
    private String generateUrl(String api) {
        return "https://" + generateHost() + generateUri(api);
    }

    @Override
    public Future<JsonObject> request(ChatModel chatModel, JsonObject requestBody, String requestId) {
        if (!isChatModelSupported(chatModel)) {
            throw new IllegalArgumentException("ChatModel is not supported by this ChatModelServiceAdapter.");
        }

        var url = generateUrl("/chat/completions");
        AigcMix.getVerboseLogger().info(x -> x
                .message("Start AzureOpenAIServiceMeta.request")
                .context(
                        j -> j
                                .put("api", url)
                                .put("input", requestBody)
                                .put("requestId", requestId)
                )
        );

        return Keel.useWebClient(webClient -> {
            return webClient
                    .postAbs(url)
                    .putHeader("Content-Type", "application/json")
                    .putHeader("api-key", apiKey)
                    .sendJsonObject(requestBody)
                    .compose(bufferHttpResponse -> {
                        JsonObject entries = bufferHttpResponse.bodyAsJsonObject();
                        if (bufferHttpResponse.statusCode() != 200 || entries == null) {
                            throw new AbnormalResponse(bufferHttpResponse);
                        }
                        AigcMix.getVerboseLogger().info(x -> x
                                .message("bufferHttpResponse in AzureOpenAIServiceMeta.request")
                                .context(j -> j
                                        .put("output", entries)
                                        .put("requestId", requestId)
                                )
                        );
                        return Future.succeededFuture(entries);
                    });
        });
    }

    @Override
    public Future<Void> requestStream(ChatModel chatModel, JsonObject parameters, Function<String, Future<Void>> cutterProcessFunc, long cutterTimeout, String requestId) {
        if (!isChatModelSupported(chatModel)) {
            throw new IllegalArgumentException("ChatModel is not supported by this ChatModelServiceAdapter.");
        }

        var uri = generateUri("/chat/completions");
        return ChatModelServiceAdapter.callStreamWithCutter(
                new HttpClientOptions()
                        .setKeepAlive(true)
                        .setSsl(true)
                        .setDefaultHost(generateHost())
                        .setDefaultPort(443),
                client -> client.request(HttpMethod.POST, uri)
                                .compose(httpClientRequest -> {
                                    httpClientRequest
                                            .putHeader("Content-Type", "application/json")
                                            .putHeader("api-key", apiKey);
                                    return httpClientRequest
                                            .send(parameters.toString());
                                }),
                chunk -> {
                    AigcMix.getVerboseLogger()
                           .info("sse chunk", c -> c.put("chunk", chunk).put("request_id", requestId));
                    return cutterProcessFunc.apply(chunk);
                },
                cutterTimeout
        );
    }
}
