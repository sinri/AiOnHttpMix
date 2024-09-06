package io.github.sinri.AiOnHttpMix.azure.openai.core;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.utils.ServiceMeta;
import io.github.sinri.keel.core.cutter.CutterOnString;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import org.jetbrains.annotations.NotNull;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class AzureOpenAIServiceMeta implements ServiceMeta {

    private final String apiKey;
    private final String resourceName;
    private final String deployment;
    private final String apiVersion;

    public AzureOpenAIServiceMeta(String apiKey, String resourceName, String deployment, String apiVersion) {
        this.apiKey = apiKey;
        this.resourceName = resourceName;
        this.deployment = deployment;
        this.apiVersion = apiVersion;
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

    public Future<JsonObject> request(
            String api,
            JsonObject requestBody,
            String requestId
    ) {
        AigcMix.getVerboseLogger().error(
                "Start AzureOpenAIServiceMeta.request",
                j -> j
                        .put("api", api)
                        .put("input", requestBody)
                        .put("requestId", requestId)
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
                        AigcMix.getVerboseLogger().error(
                                "Unexpected bufferHttpResponse in AzureOpenAIServiceMeta.request",
                                j -> j
                                        .put("status_code", bufferHttpResponse.statusCode())
                                        .put("error", bufferHttpResponse.bodyAsString())
                                        .put("requestId", requestId)
                        );

                        return Future.failedFuture(new Exception(
                                "MyOpenAI.ServiceMeta.postRequest " + requestId + " Failed: "
                                        + "status code is " + bufferHttpResponse.statusCode()
                                        + " body is " + bufferHttpResponse.bodyAsString()
                        ));
                    } else {
                        AigcMix.getVerboseLogger().error(
                                "bufferHttpResponse in AzureOpenAIServiceMeta.request",
                                j -> j
                                        .put("output", entries)
                                        .put("requestId", requestId)
                        );
                        return Future.succeededFuture(entries);
                    }
                })
                .andThen(ar -> {
                    webClient.close();
                });
    }

    public void requestSSE(
            String api,
            @NotNull JsonObject parameters,
            Promise<Void> promise,
            CutterOnString cutter,
            String requestId
    ) {
        HttpClientOptions options = new HttpClientOptions()
                .setKeepAlive(true)
                .setSsl(true)
                .setDefaultHost(generateHost())
                .setDefaultPort(443);
        HttpClient client = Keel.getVertx().createHttpClient(options);

        AigcMix.getVerboseLogger().warning(
                "Start AzureOpenAIServiceMeta.requestSSE",
                j -> j
                        .put("api", api)
                        .put("input", parameters)
                        .put("requestId", requestId)
        );

        client.request(HttpMethod.POST, generateUri(api))
                .compose(httpClientRequest -> {
                    httpClientRequest
                            .putHeader("Content-Type", "application/json")
                            .putHeader("api-key", apiKey);
                    return httpClientRequest.send(parameters.toString())
                            .compose(httpClientResponse -> {
                                long timer = Keel.getVertx().setTimer(getStreamTimeout(), timeout -> {
                                    client.close();
                                    promise.tryFail("TIMEOUT FOR REQUEST " + requestId);
                                    AigcMix.getVerboseLogger().warning(
                                            "Timeout in AzureOpenAIServiceMeta.requestSSE",
                                            j -> j
                                                    .put("requestId", requestId)
                                    );
                                });
                                httpClientResponse
                                        .handler(cutter::handle)
                                        .endHandler(v -> {
                                            cutter.end()
                                                    .onSuccess(cutterEnded -> {
                                                        Keel.getVertx().cancelTimer(timer);
                                                        promise.tryComplete();
                                                        AigcMix.getVerboseLogger().info(
                                                                "End Success in AzureOpenAIServiceMeta.requestSSE",
                                                                j -> j
                                                                        .put("requestId", requestId)
                                                        );
                                                    })
                                                    .onFailure(throwable -> {
                                                        Keel.getVertx().cancelTimer(timer);
                                                        promise.tryFail(throwable);
                                                        AigcMix.getVerboseLogger().error(
                                                                "End Failure in AzureOpenAIServiceMeta.requestSSE",
                                                                j -> j
                                                                        .put("requestId", requestId)
                                                        );
                                                    });
                                        })
                                        .exceptionHandler(throwable -> {
                                            promise.tryFail(new RuntimeException("httpClientResponse exception", throwable));
                                            Keel.getVertx().cancelTimer(timer);
                                            AigcMix.getVerboseLogger().exception(
                                                    throwable,
                                                    "Exception Handler in AzureOpenAIServiceMeta.requestSSE",
                                                    j -> j
                                                            .put("requestId", requestId)
                                            );
                                        });
                                return Future.succeededFuture();
                            });
                })
                .onFailure(throwable -> {
                    promise.tryFail(new RuntimeException("HttpClient request exception for request: " + requestId, throwable));
                    AigcMix.getVerboseLogger().exception(
                            throwable,
                            "HttpClient Exception Handler in AzureOpenAIServiceMeta.requestSSE",
                            j -> j
                                    .put("requestId", requestId)
                    );
                });

        promise.future().andThen(ar -> {
            client.close();
        });
    }
}
