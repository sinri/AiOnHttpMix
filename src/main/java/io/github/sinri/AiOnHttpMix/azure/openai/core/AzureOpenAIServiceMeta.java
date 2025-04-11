package io.github.sinri.AiOnHttpMix.azure.openai.core;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.utils.ServiceMeta;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.github.sinri.AiOnHttpMix.utils.SupportedProvider;
import io.github.sinri.keel.core.cutter.IntravenouslyCutter;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class AzureOpenAIServiceMeta implements ServiceMeta {
    /**
     * @since 1.2.2
     */
    private static final Set<SupportedModel> supportedModels = new HashSet<>();

    static {
        // since 1.2.2
        supportedModels.add(SupportedModel.ChatGPT);
    }

    private final String apiKey;
    private final String resourceName;
    private final String deployment;
    private final String apiVersion;
    private final long streamTimeout = 180_000L;

    public AzureOpenAIServiceMeta(@NotNull String apiKey, @NotNull String resourceName, @NotNull String deployment, @NotNull String apiVersion) {
        this.apiKey = apiKey;
        this.resourceName = resourceName;
        this.deployment = deployment;
        this.apiVersion = apiVersion;
    }

    /**
     * @since 1.2.2
     */
    @Override
    public Set<SupportedModel> getSupportedModels() {
        return supportedModels;
    }

    public String generateHost() {
        return resourceName + ".openai.azure.com";
    }

    public String generateUri(String api) {
        return "/openai/deployments/" + deployment + api + "?api-version=" + apiVersion;
    }

    /**
     * @param api start with a slash `/`
     */
    public String generateUrl(String api) {
        return "https://" + generateHost() + generateUri(api);
    }


    @Override
    public Future<JsonObject> request(
            String api,
            JsonObject requestBody,
            String requestId
    ) {
        AigcMix.getVerboseLogger().info(x -> x
                .message("Start AzureOpenAIServiceMeta.request")
                .context(
                        j -> j
                                .put("api", api)
                                .put("input", requestBody)
                                .put("requestId", requestId)
                )
        );

        var url = generateUrl(api);
        WebClient webClient = WebClient.create(Keel.getVertx());
        return webClient
                .postAbs(url)
                .putHeader("Content-Type", "application/json")
                .putHeader("api-key", apiKey)
                .sendJsonObject(requestBody)
                .compose(bufferHttpResponse -> {
                    JsonObject entries = bufferHttpResponse.bodyAsJsonObject();
                    if (bufferHttpResponse.statusCode() != 200 || entries == null) {
                        AigcMix.getVerboseLogger().error(x -> x
                                .message("Unexpected bufferHttpResponse in AzureOpenAIServiceMeta.request")
                                .context(j -> j
                                        .put("status_code", bufferHttpResponse.statusCode())
                                        .put("error", bufferHttpResponse.bodyAsString())
                                        .put("requestId", requestId)
                                )
                        );

                        return Future.failedFuture(new Exception(
                                "MyOpenAI.ServiceMeta.postRequest " + requestId + " Failed: "
                                        + "status code is " + bufferHttpResponse.statusCode()
                                        + " body is " + bufferHttpResponse.bodyAsString()
                        ));
                    } else {
                        AigcMix.getVerboseLogger().info(x -> x
                                .message("bufferHttpResponse in AzureOpenAIServiceMeta.request")
                                .context(j -> j
                                        .put("output", entries)
                                        .put("requestId", requestId)
                                )
                        );
                        return Future.succeededFuture(entries);
                    }
                })
                .andThen(ar -> {
                    webClient.close();
                });
    }

    @Override
    public Future<Void> requestSSE(
            String api,
            @NotNull JsonObject parameters,
            IntravenouslyCutter<String> cutter,
            int maxExecutionSeconds,
            String requestId
    ) {
        return ServiceMeta.requestSSEImpl(
                new HttpClientOptions()
                        .setKeepAlive(true)
                        .setSsl(true)
                        .setDefaultHost(generateHost())
                        .setDefaultPort(443),
                client -> {
                    return client.request(HttpMethod.POST, generateUri(api))
                                 .compose(httpClientRequest -> {
                                     httpClientRequest
                                             .putHeader("Content-Type", "application/json")
                                             .putHeader("api-key", apiKey);
                                     return httpClientRequest
                                             .send(parameters.toString());
                                 });
                },
                cutter,
                maxExecutionSeconds * 1000L,
                requestId
        );
    }

    @Override
    public SupportedProvider getSupportedProvider() {
        return SupportedProvider.AzureOpenAI;
    }
}
