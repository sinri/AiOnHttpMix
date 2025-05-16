package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.utils.AbnormalResponse;
import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.providers.DashscopeServiceProvider;
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

public class QwenServiceAdapter implements ChatModelServiceAdapter {
    private static final Set<ChatModelSeries> supportedChatModelSeriesSet = new HashSet<>();

    static {
        supportedChatModelSeriesSet.add(ChatModelSeries.qwen);
    }

    private final String apiKey;

    public QwenServiceAdapter(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return ServiceProvider.dashscope;
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

        requestBody.put("model", chatModel.getName());

        AigcMix.getVerboseLogger().info(x -> x
                .message("Start DashscopeServiceMeta.request")
                .context(j -> j
                        .put("api", DashscopeServiceProvider.endpointOfDashscopeQwenTextGenerate)
                        .put("requestId", requestId)
                        .put("input", requestBody)
                )
        );

        return Keel.useWebClient(webClient -> {
            return webClient
                    .postAbs(DashscopeServiceProvider.endpointOfDashscopeQwenTextGenerate)
                    .putHeader("Content-Type", "application/json")
                    .putHeader("Authorization", "Bearer " + apiKey)
                    .sendJsonObject(requestBody)
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
                    });
        });
    }

    @Override
    public Future<Void> requestStream(ChatModel chatModel, JsonObject parameters, Function<String, Future<Void>> cutterProcessFunc, long cutterTimeout, String requestId) {
        if (!isChatModelSupported(chatModel)) {
            throw new IllegalArgumentException("ChatModel is not supported by this ChatModelServiceAdapter.");
        }

        parameters.put("model", chatModel.getName());

        return ChatModelServiceAdapter.callStreamWithCutter(
                new HttpClientOptions()
                        .setKeepAlive(true)
                        .setSsl(true)
                        .setDefaultHost(DashscopeServiceProvider.hostOfDashscope)
                        .setDefaultPort(443),
                client -> client.request(HttpMethod.POST, DashscopeServiceProvider.pathOfDashscopeQwenTextGenerate)
                                .compose(httpClientRequest -> {
                                    httpClientRequest
                                            .putHeader("Content-Type", "application/json")
                                            .putHeader("Authorization", "Bearer " + apiKey)
                                            .putHeader("X-DashScope-SSE", "enable");
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
