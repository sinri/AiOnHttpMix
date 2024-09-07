package io.github.sinri.AiOnHttpMix.moonshot.core;

import io.github.sinri.AiOnHttpMix.utils.ServiceMeta;
import io.github.sinri.keel.core.cutter.Cutter;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import org.jetbrains.annotations.NotNull;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class MoonshotServiceMeta implements ServiceMeta {
    private static final String host = "api.moonshot.cn";
    private static final String endpoint = "https://" + host;//+"/v1";
    private final String apiKey;

    public MoonshotServiceMeta(String apiKey) {
        this.apiKey = apiKey;
    }

    public Future<JsonObject> requestGet(String api,String requestId) {
        WebClient webClient = WebClient.create(Keel.getVertx());
        return webClient
                .getAbs(endpoint + api)
                .bearerTokenAuthentication(apiKey)
                .send()
                .compose(bufferHttpResponse -> {
                    return Future.succeededFuture(bufferHttpResponse.bodyAsJsonObject());
                })
                .andThen(ar -> {
                    webClient.close();
                });
    }

    @Override
    public Future<JsonObject> request(String api, JsonObject requestBody, String requestId) {
        WebClient webClient = WebClient.create(Keel.getVertx());
        return Future.succeededFuture(endpoint + api)
                .compose(url -> {
                    var req = webClient
                            .postAbs(url)
                            .bearerTokenAuthentication(apiKey);
                    return Future.succeededFuture(req);
                })
                .compose(req -> {
                    return req.sendJsonObject(requestBody);
                })
                .compose(bufferHttpResponse -> {
                    int statusCode = bufferHttpResponse.statusCode();
                    if (statusCode == 200) {
                        return Future.succeededFuture(bufferHttpResponse.bodyAsJsonObject());
                    } else {
                        return Future.failedFuture("MoonshotServiceMeta Request Failed, status code is " + statusCode + ", request id is " + requestId);
                    }
                })
                .andThen(ar -> {
                    webClient.close();
                });
    }

    @Override
    public void requestSSE(String api, @NotNull JsonObject parameters, Promise<Void> promise, Cutter<String> cutter, String requestId) {
        HttpClientOptions options = new HttpClientOptions()
                .setKeepAlive(true)
                .setSsl(true)
                .setDefaultHost(host)
                .setDefaultPort(443);
        HttpClient client = Keel.getVertx().createHttpClient(options);

        client.request(HttpMethod.POST, api)
                .compose(httpClientRequest -> {
                    httpClientRequest
                            .putHeader("Content-Type", "application/json")
                            .putHeader("Authorization", "Bearer " + this.apiKey);
                    return httpClientRequest.send(parameters.toString())
                            .onSuccess(httpClientResponse -> {
                                httpClientResponse
                                        .handler(cutter::handle)
                                        .endHandler(v -> {
                                            cutter.end()
                                                    .onSuccess(cutterEnded -> {
                                                        promise.complete();
                                                    })
                                                    .onFailure(throwable -> {
                                                        promise.fail(throwable);
                                                    });
                                        })
                                        .exceptionHandler(throwable -> {
                                            promise.fail(new RuntimeException("httpClientResponse exception for request id " + requestId, throwable));
                                        });
                            });
                })
                .onFailure(throwable -> {
                    promise.fail(new RuntimeException("httpClient request exception for request id " + requestId, throwable));
                });

        promise.future().andThen(ar -> {
            client.close();
        });
    }
}
