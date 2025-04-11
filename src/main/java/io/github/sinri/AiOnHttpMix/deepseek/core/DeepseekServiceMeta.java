package io.github.sinri.AiOnHttpMix.deepseek.core;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.utils.ServiceMeta;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.github.sinri.AiOnHttpMix.utils.SupportedProvider;
import io.github.sinri.keel.core.cutter.IntravenouslyCutter;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class DeepseekServiceMeta implements ServiceMeta {
    public static final String ENDPOINT_SCHEMA = "https";
    public static final String ENDPOINT_HOST = "api.deepseek.com";
    public static final int ENDPOINT_PORT = 443;
    /**
     * @since 1.2.2
     */
    private static final Set<SupportedModel> supportedModels = new HashSet<>();

    static {
        // since 1.2.2
        supportedModels.add(SupportedModel.DeepSeekReasoner);
        supportedModels.add(SupportedModel.DeepSeekChat);
    }

    private final String apiKey;
    private final long streamTimeout = 180_000L;

    public DeepseekServiceMeta(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * @since 1.2.2
     */
    @Override
    public Set<SupportedModel> getSupportedModels() {
        return supportedModels;
    }

    @Override
    public Future<JsonObject> request(String api, JsonObject requestBody, String requestId) {
        AigcMix.getVerboseLogger().info(x -> x
                .message("Start DeepseekServiceMeta.request")
                .context(j -> j
                        .put("api", api)
                        .put("requestId", requestId)
                        .put("input", requestBody))
        );
        return Keel.useWebClient(webClient -> {
                       return webClient.postAbs(ENDPOINT_SCHEMA + "://" + ENDPOINT_HOST + api)
                                       .bearerTokenAuthentication(apiKey)
                                       .sendJsonObject(requestBody);
                   })
                   .compose(bufferHttpResponse -> {
                       AigcMix.getVerboseLogger()
                              .debug("bufferHttpResponse code is " + bufferHttpResponse.statusCode());
                       AigcMix.getVerboseLogger()
                              .debug("bufferHttpResponse body is " + bufferHttpResponse.bodyAsString());
                       if (bufferHttpResponse.statusCode() != 200) {
                           return Future.failedFuture(new Exception("Status code: " + bufferHttpResponse.statusCode() + "; Body: " + bufferHttpResponse.bodyAsString()));
                       }
                       JsonObject body = bufferHttpResponse.bodyAsJsonObject();
                       Objects.requireNonNull(body, "bufferHttpResponse body as json object is null");
                       return Future.succeededFuture(body);
                   });
    }

    @Override
    public Future<Void> requestSSE(String api, @NotNull JsonObject parameters, IntravenouslyCutter<String> cutter, int maxExecutionSeconds, String requestId) {
        return ServiceMeta.requestSSEImpl(
                new HttpClientOptions()
                        .setSsl(true)
                        .setKeepAlive(true),
                httpClient -> httpClient.request(HttpMethod.POST, ENDPOINT_PORT, ENDPOINT_HOST, api)
                                        .compose(request -> request
                                                .putHeader("Content-Type", "application/json")
                                                .putHeader("Authorization", "Bearer " + apiKey)
                                                .send(parameters.toBuffer())
                                        ),
                cutter,
                maxExecutionSeconds * 1000L,
                requestId
        );
    }

    @Override
    public SupportedProvider getSupportedProvider() {
        return SupportedProvider.DeepSeek;
    }
}
