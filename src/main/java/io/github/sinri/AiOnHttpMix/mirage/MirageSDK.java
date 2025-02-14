package io.github.sinri.AiOnHttpMix.mirage;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMKit;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMRequest;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMResponse;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.github.sinri.keel.core.cutter.Cutter;
import io.github.sinri.keel.core.cutter.CutterOnString;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class MirageSDK {
    private final String mirageDomain;
    private final String clientCode;
    private final String clientSecret;
    /**
     * @since 1.1.5
     */
    private long maxStreamTime = 180_000L;

    public MirageSDK(String mirageDomain, String clientCode, String clientSecret) {
        this.mirageDomain = mirageDomain;
        this.clientCode = clientCode;
        this.clientSecret = clientSecret;
    }

    /**
     * @param model      模型的定义，一般约定使用 {@link SupportedModel#name()} 。
     * @param service    对应模型定义在Mirage服务内提供的服务；当同一个模型有不同部署时，通过服务区分。
     * @param useNyaCode 传输内容是否使用NyaCode编码绕过防火墙。
     */
    private JsonObject buildRequestBody(
            String model,
            String service,
            boolean useNyaCode,
            MirageRequestEntity llmRequestBody
    ) {
        var timestamp = System.currentTimeMillis();
        String checksum = Keel.digestHelper().md5(clientCode + "@" + timestamp + "@" + clientSecret);

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

    public Future<AnyLLMResponse> requestSync(
            String model,
            String service,
            boolean useNyaCode,
            MirageRequestEntity llmRequestBody
    ) {
        var body = buildRequestBody(model, service, useNyaCode, llmRequestBody);
        return Keel.useWebClient(webClient -> {
            var url = "https://" + mirageDomain + "/mirage/aigc/llm/sync";
            return webClient.postAbs(url)
                            .sendJsonObject(body)
                            .compose(bufferHttpResponse -> {
                                if (bufferHttpResponse.statusCode() != 200) {
                                    return Future.failedFuture(new Exception("Status Code:" + bufferHttpResponse.statusCode() + "; " + bufferHttpResponse.bodyAsString()));
                                }

                                AigcMix.getVerboseLogger()
                                       .debug("io.github.sinri.AiOnHttpMix.mirage.MirageSDK.requestSync::bufferHttpResponse | " + bufferHttpResponse.bodyAsString());

                                var resp = bufferHttpResponse.bodyAsJsonObject();
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

        Cutter<String> cutter = new CutterOnString();
        cutter.setComponentHandler(s -> {
            AigcMix.getVerboseLogger().debug(
                    "io.github.sinri.AiOnHttpMix.mirage.MirageSDK.requestStream::component | " + s);

            //Keel.getLogger().fatal("MirageSDK.requestStream cut off: " + s);
            var lines = s.split("[\r\n]+");
            for (var line : lines) {
                if (line.startsWith("data:")) {
                    line = line.replaceAll("^data:\\s*", "");
                    AigcMix.getVerboseLogger()
                           .debug("io.github.sinri.AiOnHttpMix.mirage.MirageSDK.requestStream::line | " + line);

                    fragmentHandler.handle(line);
                    break;
                }
            }
        });

        return Keel.useHttpClient(
                           new HttpClientOptions()
                                   .setKeepAlive(true)
                                   .setSsl(true)
                                   .setDefaultHost(mirageDomain)
                                   .setDefaultPort(443),
                           client -> {
                               return client.request(HttpMethod.POST, "/mirage/aigc/llm/stream")
                                            .compose(httpClientRequest -> {
                                                httpClientRequest.putHeader("Content-Type", "application/json");
                                                return httpClientRequest
                                                        .send(body.toString())
                                                        .compose(httpClientResponse -> {
                                                            long timer = Keel.getVertx()
                                                                             .setTimer(maxStreamTime, y -> {
                                                                                 client.close();
                                                                                 promise.tryFail("TIMEOUT FOR REQUEST");
                                                                             });
                                                            httpClientResponse
                                                                    .handler(cutter::handle)
                                                                    .endHandler(v -> {
                                                                        cutter.end()
                                                                              .onSuccess(cutterEnded -> {
                                                                                  Keel.getVertx()
                                                                                      .cancelTimer(timer);
                                                                                  promise.tryComplete();
                                                                              })
                                                                              .onFailure(throwable -> {
                                                                                  Keel.getVertx()
                                                                                      .cancelTimer(timer);
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
                           }
                   )
                   .eventually(promise::future)
                   .compose(v -> Future.succeededFuture());
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
