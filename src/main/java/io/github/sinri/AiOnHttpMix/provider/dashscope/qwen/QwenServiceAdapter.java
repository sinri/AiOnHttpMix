package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.utils.AbnormalResponse;
import io.github.sinri.AiOnHttpMix.utils.ServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.models.dashscope.qwen.QwenChatModelSeries;
import io.github.sinri.AiOnHttpMix.utils.models.dashscope.qwen.QwenVisionModelSeries;
import io.github.sinri.AiOnHttpMix.utils.providers.DashscopeServiceProvider;
import io.github.sinri.AiOnHttpMix.utils.specification.DashscopeModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;

import java.util.function.Function;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

/**
 * @since 2.0.0
 */
public class QwenServiceAdapter implements ServiceAdapter {
    private final String apiKey;

    public QwenServiceAdapter(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Future<JsonObject> request(ChatModel chatModel, JsonObject requestPayload, String requestId) {
        assertModelCompatible(chatModel);

        requestPayload.put("model", chatModel.getModelName());

        String endpoint;
        if (chatModel instanceof QwenChatModelSeries) {
            endpoint = DashscopeServiceProvider.endpointOfDashscopeQwenTextGenerate;
        } else if (chatModel instanceof QwenVisionModelSeries) {
            endpoint = DashscopeServiceProvider.endpointOfDashscopeQwenMultimodalGenerate;
        } else {
            throw new RuntimeException("endpoint mistake");
        }

        AigcMix.getVerboseLogger().info(x -> x
                .message("Start DashscopeServiceMeta.request")
                .context(j -> j
                        .put("api", endpoint)
                        .put("requestId", requestId)
                        .put("input", requestPayload)
                )
        );

        return Keel.useWebClient(webClient -> webClient
                .postAbs(endpoint)
                .putHeader("Content-Type", "application/json")
                .putHeader("Authorization", "Bearer " + apiKey)
                .sendJsonObject(requestPayload)
                .compose(bufferHttpResponse -> {
                    int statusCode = bufferHttpResponse.statusCode();
                    if (statusCode != 200) {
                        throw new AbnormalResponse(bufferHttpResponse);
                    }
                    JsonObject entries = bufferHttpResponse.bodyAsJsonObject();
                    AigcMix.getVerboseLogger().info(x -> x
                            .message("bufferHttpResponse in DashscopeServiceMeta.request")
                            .context(j -> j
                                    .put("requestId", requestId)
                                    .put("output", entries))
                    );
                    return Future.succeededFuture(entries);
                }));
    }

    @Override
    public Future<Void> requestStream(ChatModel chatModel, JsonObject requestPayload, Function<String, Future<Void>> cutterProcessFunc, long cutterTimeout, String requestId) {
        assertModelCompatible(chatModel);

        requestPayload.put("model", chatModel.getModelName());

        String path;
        if (chatModel instanceof QwenChatModelSeries) {
            path = DashscopeServiceProvider.pathOfDashscopeQwenTextGenerate;
        } else if (chatModel instanceof QwenVisionModelSeries) {
            path = DashscopeServiceProvider.pathOfDashscopeQwenMultimodalGenerate;
        } else {
            throw new RuntimeException("path mistake");
        }

        AigcMix.getVerboseLogger()
               .info("Start DashscopeServiceMeta.requestStream", j -> j
                       .put("payload", requestPayload)
                       .put("requestId", requestId));

        return ServiceAdapter.callStreamWithCutter(
                new HttpClientOptions()
                        .setKeepAlive(true)
                        .setSsl(true)
                        .setDefaultHost(DashscopeServiceProvider.hostOfDashscope)
                        .setDefaultPort(443),
                client -> client
                        .request(HttpMethod.POST, path)
                        .compose(httpClientRequest -> {
                            httpClientRequest
                                    .putHeader("Content-Type", "application/json")
                                    .putHeader("Authorization", "Bearer " + apiKey)
                                    .putHeader("X-DashScope-SSE", "enable");
                            return httpClientRequest
                                    .send(requestPayload.toString());
                        }),
                chunk -> {
                    AigcMix.getVerboseLogger().info("sse chunk:\n" + chunk, c -> c.put("request_id", requestId));
                    return cutterProcessFunc.apply(chunk);
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
                   .isClassAssignable(DashscopeModelSpecification.class, modelSpecification.getClass());
    }
}
