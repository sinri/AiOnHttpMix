package io.github.sinri.AiOnHttpMix.azure.openai.core.impl;

import io.github.sinri.AiOnHttpMix.azure.openai.core.AzureOpenAIServiceMeta;
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

public class ServiceMetaImpl implements AzureOpenAIServiceMeta {
    private final String apiKey;
    private final String resourceName;
    private final String deployment;
    private final String apiVersion;

    public ServiceMetaImpl(String apiKey, String resourceName, String deployment, String apiVersion) {
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

    public Future<JsonObject> postRequest(
            String api,
            JsonObject requestBody,
            String requestId
    ) {
        var url = generateUrl(api);
        return WebClient.create(Keel.getVertx())
                .postAbs(url)
                .putHeader("Content-Type", "application/json")
                .putHeader("api-key", apiKey)
                .sendJsonObject(requestBody)
                .compose(bufferHttpResponse -> {
                    JsonObject entries = bufferHttpResponse.bodyAsJsonObject();
                    if (bufferHttpResponse.statusCode() != 200 || entries == null) {
                        return Future.failedFuture(new Exception(
                                "MyOpenAI.ServiceMeta.postRequest " + requestId + " Failed: "
                                        + "status code is " + bufferHttpResponse.statusCode()
                                        + " body is " + bufferHttpResponse.bodyAsString()
                        ));
                    }
                    return Future.succeededFuture(entries);
                });
    }

    public void postRequestSSE(
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

        client.request(HttpMethod.POST, generateUri(api))
                .compose(httpClientRequest -> {
                    httpClientRequest
                            .putHeader("Content-Type", "application/json")
                            .putHeader("api-key", apiKey);
                    return httpClientRequest.send(parameters.toString())
                            .compose(httpClientResponse -> {
                                long timer = Keel.getVertx().setTimer(180_000L, timeout -> {
                                    client.close();
                                    promise.tryFail("TIMEOUT FOR REQUEST " + requestId);
                                });
                                httpClientResponse
                                        .handler(cutter::handle)
                                        .endHandler(v -> {
                                            cutter.end()
                                                    .onSuccess(cutterEnded -> {
                                                        Keel.getVertx().cancelTimer(timer);
                                                        promise.complete();
                                                    })
                                                    .onFailure(throwable -> {
                                                        Keel.getVertx().cancelTimer(timer);
                                                        promise.fail(throwable);
                                                    });
                                        })
                                        .exceptionHandler(throwable -> {
                                            promise.fail(new RuntimeException("httpClientResponse exception", throwable));
                                            Keel.getVertx().cancelTimer(timer);
                                        });
                                return Future.succeededFuture();
                            });
                })
                .onFailure(throwable -> {
                    promise.fail(new RuntimeException("HttpClient request exception for request: " + requestId, throwable));
                });
    }
}
