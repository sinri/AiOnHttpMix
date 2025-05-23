package io.github.sinri.AiOnHttpMix.provider.volces;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.utils.AbnormalResponse;
import io.github.sinri.AiOnHttpMix.utils.ServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.VolcesModelSpecification;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

/**
 * @since 2.0.0
 */
public class VolcesServiceAdapter implements ServiceAdapter {

    private final Map<String, String> modelDeploymentMap;
    private final String apiKey;

    public VolcesServiceAdapter(KeelConfigElement config) {
        String apiKey = config.readString(List.of("apiKey"));
        Map<String, String> modelDeploymentMap = new HashMap<>();
        KeelConfigElement models = config.extract("model");
        if (models != null) {
            models.getChildren().forEach((k, v) -> {
                modelDeploymentMap.put(k, v.getValueAsString());
            });
        }

        this.apiKey = apiKey;
        this.modelDeploymentMap = modelDeploymentMap;
    }

    public VolcesServiceAdapter(String apiKey, Map<String, String> modelDeploymentMap) {
        this.apiKey = apiKey;
        this.modelDeploymentMap = modelDeploymentMap;
    }

    public String toModelMappedDeploymentId(ChatModel chatModel) {
        String deploymentId = this.modelDeploymentMap.get(chatModel.getModelName());
        if (deploymentId == null) {
            return chatModel.getModelName();
        }
        return deploymentId;
    }

    @Override
    public Future<JsonObject> request(ChatModel chatModel, JsonObject requestPayload, String requestId) {
        assertModelCompatible(chatModel);

        String url = "https://" + VolcesModelSpecification.hostOfV3ChatCompletions + VolcesModelSpecification.pathOfV3ChatCompletions;

        requestPayload.put("model", toModelMappedDeploymentId(chatModel));

        AigcMix.getVerboseLogger().info(x -> x
                .message("Start VolcesServiceMeta.request")
                .context(j -> j
                        .put("api", url)
                        .put("requestId", requestId)
                        .put("input", requestPayload)
                )
        );

        return Keel.useWebClient(webClient -> {
            return webClient
                    .postAbs(url)
                    .bearerTokenAuthentication(apiKey)
                    .sendJsonObject(requestPayload)
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
    public Future<Void> requestStream(ChatModel chatModel, JsonObject requestPayload, Function<String, Future<Void>> cutterProcessFunc, long cutterTimeout, String requestId) {
        assertModelCompatible(chatModel);

        requestPayload.put("model", toModelMappedDeploymentId(chatModel));
        return ServiceAdapter.callStreamWithCutter(
                new HttpClientOptions()
                        .setKeepAlive(true)
                        .setSsl(true)
                        .setDefaultHost(VolcesModelSpecification.hostOfV3ChatCompletions)
                        .setDefaultPort(443),
                client -> {
                    return client.request(HttpMethod.POST, VolcesModelSpecification.pathOfV3ChatCompletions)
                                 .compose(httpClientRequest -> {
                                     httpClientRequest
                                             .putHeader("Content-Type", "application/json")
                                             .putHeader("Authorization", "Bearer " + apiKey);
                                     return httpClientRequest
                                             .send(requestPayload.toString());
                                 });
                },
                fragment -> {
                    AigcMix.getVerboseLogger().info("[" + requestId + "] sse fragment: \n" + fragment);
                    return cutterProcessFunc.apply(fragment);
                },
                cutterTimeout
        );
    }

    @Override
    public boolean isModelCompatible(ChatModel chatModel) {
        return isModelCompatible((ModelSpecification) chatModel);
    }

    @Override
    public boolean isModelCompatible(ModelSpecification modelSpecification) {
        return Keel.reflectionHelper()
                   .isClassAssignable(VolcesModelSpecification.class, modelSpecification.getClass());
    }
}
