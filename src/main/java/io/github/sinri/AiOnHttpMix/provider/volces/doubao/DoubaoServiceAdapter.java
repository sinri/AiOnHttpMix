package io.github.sinri.AiOnHttpMix.provider.volces.doubao;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.utils.AbnormalResponse;
import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;
import io.github.sinri.AiOnHttpMix.utils.series.ChatModelSeries;
import io.github.sinri.AiOnHttpMix.utils.series.DoubaoModelService;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class DoubaoServiceAdapter implements ChatModelServiceAdapter {
    private static final Set<ChatModelSeries> supportedChatModelSeriesSet = new HashSet<>();

    static {
        supportedChatModelSeriesSet.add(ChatModelSeries.doubao);
    }

    private final Map<String, String> modelDeploymentMap;
    private final String apiKey;

    public DoubaoServiceAdapter(String apiKey, Map<String, String> modelDeploymentMap) {
        this.apiKey = apiKey;
        this.modelDeploymentMap = modelDeploymentMap;
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return ServiceProvider.volces;
    }

    @Override
    public Set<ChatModelSeries> getChatModelSeries() {
        return supportedChatModelSeriesSet;
    }

    @Override
    public Future<JsonObject> request(ChatModel chatModel, JsonObject requestBody, String requestId) {
        if (!isChatModelSupported(chatModel)) {
            throw new IllegalArgumentException("ChatModel is not supported by this ChatModelServiceAdapter.");
        }

        String url = "https://" + DoubaoModelService.hostOfV3ChatCompletions + DoubaoModelService.pathOfV3ChatCompletions;

        requestBody.put("model", this.modelDeploymentMap.get(chatModel.getName()));

        AigcMix.getVerboseLogger().info(x -> x
                .message("Start VolcesServiceMeta.request")
                .context(j -> j
                        .put("api", url)
                        .put("requestId", requestId)
                        .put("input", requestBody)
                )
        );

        return Keel.useWebClient(webClient -> {
            return webClient
                    .postAbs(url)
                    .bearerTokenAuthentication(apiKey)
                    .sendJsonObject(requestBody)
                    .compose(bufferHttpResponse -> {
                        if (bufferHttpResponse.statusCode() != 200) {
                            throw new AbnormalResponse(bufferHttpResponse);
                        } else {
                            JsonObject respAsJsonObject = bufferHttpResponse.bodyAsJsonObject();
                            AigcMix.getVerboseLogger().info(x -> x
                                    .message("bufferHttpResponse in VolcesServiceMeta.request")
                                    .context(j -> j
                                            .put("requestId", requestId)
                                            .put("output", respAsJsonObject))
                            );
                            return Future.succeededFuture(respAsJsonObject);
                        }
                    });
        });
    }

    @Override
    public Future<Void> requestStream(ChatModel chatModel, JsonObject parameters, Function<String, Future<Void>> cutterProcessFunc, long cutterTimeout, String requestId) {
        if (!isChatModelSupported(chatModel)) {
            throw new IllegalArgumentException("ChatModel is not supported by this ChatModelServiceAdapter.");
        }

        parameters.put("model", this.modelDeploymentMap.get(chatModel.getName()));
        return ChatModelServiceAdapter.callStreamWithCutter(
                new HttpClientOptions()
                        .setKeepAlive(true)
                        .setSsl(true)
                        .setDefaultHost(DoubaoModelService.hostOfV3ChatCompletions)
                        .setDefaultPort(443),
                client -> {
                    return client.request(HttpMethod.POST, DoubaoModelService.pathOfV3ChatCompletions)
                                 .compose(httpClientRequest -> {
                                     httpClientRequest
                                             .putHeader("Content-Type", "application/json")
                                             .putHeader("Authorization", "Bearer " + apiKey);
                                     return httpClientRequest
                                             .send(parameters.toString());
                                 });
                },
                chunk -> {
                    AigcMix.getVerboseLogger()
                           .info("sse chunk", c -> c.put("chunk", chunk).put("request_id", requestId));
                    return cutterProcessFunc.apply(chunk);
                },
                cutterTimeout
        );
    }
}
