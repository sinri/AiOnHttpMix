package io.github.sinri.AiOnHttpMix.mirage;

import io.github.sinri.AiOnHttpMix.mix.AnyLLMKit;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMRequest;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMResponse;
import io.github.sinri.keel.core.cutter.Cutter;
import io.github.sinri.keel.core.cutter.CutterOnString;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;

import static io.github.sinri.keel.facade.KeelInstance.Keel;
import static io.github.sinri.keel.helper.KeelHelpersInterface.KeelHelpers;

public class MirageSDK {
    private final String mirageDomain;
    private final String clientCode;
    private final String clientSecret;

    public MirageSDK(String mirageDomain, String clientCode, String clientSecret) {
        this.mirageDomain = mirageDomain;
        this.clientCode = clientCode;
        this.clientSecret = clientSecret;
    }

    private JsonObject buildRequestBody(
            String model,
            String service,
            boolean useNyaCode,
            MirageRequestEntity llmRequestBody
    ) {
        var timestamp = System.currentTimeMillis();
        String checksum = KeelHelpers.digestHelper().md5(clientCode + "@" + timestamp + "@" + clientSecret);

        JsonObject body = new JsonObject()
                .put("client_code", clientCode)
                .put("timestamp", timestamp)
                .put("checksum", checksum);

        body.put("model", model);
        body.put("service", service);
        body.put("use_nyacode", useNyaCode);
        if (useNyaCode) {
            body.put("request", Keel.stringHelper().encodeToNyaCode(llmRequestBody.toJsonObject().toString()));
        } else {
            body.put("request", llmRequestBody.toJsonObject());
        }
        return body;
    }

    /**
     * @since 1.1.5
     */
    private long maxStreamTime = 180_000L;

    public Future<AnyLLMResponse> requestSync(
            String model,
            String service,
            boolean useNyaCode,
            MirageRequestEntity llmRequestBody
    ) {
        var body = buildRequestBody(model, service, useNyaCode, llmRequestBody);
        return Keel.useWebClient(webClient -> {
            var url = "https://" + mirageDomain + "/mirage/aigc/llm/sync";
//            Keel.getLogger().fatal(url);
            return webClient.postAbs(url)
                    .sendJsonObject(body)
                    .compose(bufferHttpResponse -> {
//                        Keel.getLogger().fatal("DEBUG: "+bufferHttpResponse.bodyAsString());
                        if (bufferHttpResponse.statusCode() != 200) {
                            return Future.failedFuture(new Exception("Status Code:" + bufferHttpResponse.statusCode() + "; " + bufferHttpResponse.bodyAsString()));
                        }
                        var resp = bufferHttpResponse.bodyAsJsonObject();
//                         Keel.getLogger().fatal("RESULT", bufferHttpResponse.bodyAsJsonObject());
                        MirageSyncResponse mirageSyncResponse = new MirageSyncResponse(resp);
                        AnyLLMResponse anyLLMResponse = mirageSyncResponse.toAnyLLMResponse();
                        return Future.succeededFuture(anyLLMResponse);
                    });
        });
    }

    /**
     * @param fragmentHandler 针对一个已经格式化好的SSE Chunk的JSON对象字符串表达的处理器
     * @since 1.1.5
     */
    public Future<Void> requestStream(
            String model,
            String service,
            boolean useNyaCode,
            MirageRequestEntity llmRequestBody,
            Handler<String> fragmentHandler
    ) {
        return requestStream(model, service, useNyaCode, llmRequestBody, getMaxStreamTime(), fragmentHandler);
    }

    /**
     * @param fragmentHandler 针对一个已经格式化好的SSE Chunk的JSON对象字符串表达的处理器
     * @see AnyLLMKit#request(AnyLLMRequest, Handler)
     */
    public Future<Void> requestStream(
            String model,
            String service,
            boolean useNyaCode,
            MirageRequestEntity llmRequestBody,
            long maxStreamTime,
            Handler<String> fragmentHandler
    ) {
        var body = buildRequestBody(model, service, useNyaCode, llmRequestBody);

        Promise<Void> promise = Promise.promise();

        HttpClientOptions options = new HttpClientOptions()
                .setKeepAlive(true)
                .setSsl(true)
                .setDefaultHost(mirageDomain)
                .setDefaultPort(443);
        HttpClient client = Keel.getVertx().createHttpClient(options);

        Cutter<String> cutter = new CutterOnString();
        cutter.setComponentHandler(s -> {
            //Keel.getLogger().fatal("MirageSDK.requestStream cut off: " + s);
            var lines = s.split("[\r\n]+");
            for (var line : lines) {
                if (line.startsWith("data:")) {
                    line = line.replaceAll("^data:\\s*", "");
                    fragmentHandler.handle(line);
                    break;
                }
            }
        });

        client.request(HttpMethod.POST, "/mirage/aigc/llm/stream")
                .compose(httpClientRequest -> {
                    httpClientRequest.putHeader("Content-Type", "application/json");
                    return httpClientRequest.send(body.toString())
                            .compose(httpClientResponse -> {
                                long timer = Keel.getVertx().setTimer(maxStreamTime, y -> {
                                    client.close();
                                    promise.tryFail("TIMEOUT FOR REQUEST");
                                });
                                httpClientResponse
                                        .handler(cutter::handle)
                                        .endHandler(v -> {
                                            cutter.end()
                                                    .onSuccess(cutterEnded -> {
                                                        Keel.getVertx().cancelTimer(timer);
                                                        promise.tryComplete();
                                                    })
                                                    .onFailure(throwable -> {
                                                        Keel.getVertx().cancelTimer(timer);
                                                        promise.tryFail(throwable);
                                                    });
                                        })
                                        .exceptionHandler(throwable -> {
                                            promise.tryFail(new RuntimeException("httpClientResponse exception", throwable));
                                            Keel.getVertx().cancelTimer(timer);
                                        });
                                return Future.succeededFuture();
                            });
                })
                .onFailure(throwable -> {
                    promise.tryFail(new RuntimeException("HttpClient request exception for request", throwable));
                });

        promise.future().andThen(ar -> {
            client.close();
        });

        return promise.future();
    }

    /**
     * @since 1.1.5
     */
    public long getMaxStreamTime() {
        return maxStreamTime;
    }

    /**
     * @since 1.1.5
     */
    public MirageSDK setMaxStreamTime(long maxStreamTime) {
        this.maxStreamTime = maxStreamTime;
        return this;
    }
}
