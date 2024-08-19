package io.github.sinri.AiOnHttpMix.dashscope;

import io.github.sinri.AiOnHttpMix.dashscope.vl.QwenVLInput;
import io.github.sinri.AiOnHttpMix.dashscope.vl.QwenVLParameters;
import io.github.sinri.AiOnHttpMix.dashscope.vl.QwenVLResponse;
import io.github.sinri.keel.core.cutter.CutterOnString;
import io.github.sinri.keel.logger.event.KeelEventLogger;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import org.jetbrains.annotations.NotNull;


import static io.github.sinri.keel.facade.KeelInstance.Keel;

/**
 * 通义千问VL
 * 对于输入的图片有以下限制：图片文件大小不超过10MB;图片总的像素数不超过 1048576，这相当于一张长宽均为 1024 的图片总像素数.
 *
 * @see <a href="https://help.aliyun.com/zh/dashscope/developer-reference/tongyi-qianwen-vl-plus-api?spm=a2c4g.11186623.0.0.1254140bG6xrMJ#8f79b5d0f8ker">通义千问VL</a>
 */
public class QwenVLKit {
    public static final String MODEL_QWEN_VL_PLUS = "qwen-vl-plus";
    public static final String MODEL_QWEN_VL_MAX = "qwen-vl-max";
    private final String apiKey;

    public QwenVLKit(String apiKey) {
        this.apiKey = apiKey;
    }

//    public QwenVLKit() {
//        this(Keel.config("dashscope.api_key"));
//    }

    public Future<QwenVLResponse> callWithoutSSE(
            @NotNull String model,
            @NotNull QwenVLInput qwenVLInput,
            @NotNull QwenVLParameters parameters
    ) {
        var body = new JsonObject()
                .put("model", model)
                .put("input", qwenVLInput.toJsonObject())
                .put("parameters", parameters.toJsonObject());
        return WebClient.create(Keel.getVertx())
                .postAbs("https://dashscope.aliyuncs.com/api/v1/services/aigc/multimodal-generation/generation")
                .putHeader("Content-Type", "application/json")
                .putHeader("Authorization", "Bearer " + apiKey)
                .sendJsonObject(body)
                .compose(bufferHttpResponse -> {
                    int statusCode = bufferHttpResponse.statusCode();
                    JsonObject entries = bufferHttpResponse.bodyAsJsonObject();
//                    this.logger.info(eventLog -> eventLog
//                            .message("api responded " + statusCode)
//                            .context(c -> c
//                                    .put("input", body)
//                                    .put("output", entries)
//                            )
//                    );
                    return Future.succeededFuture(new QwenVLResponse(
                            statusCode,
                            entries
                    ));
                });
    }

    public Future<Void> callWithSSE(
            @NotNull String model,
            @NotNull QwenVLInput qwenVLInput,
            @NotNull QwenVLParameters parameters,
            @NotNull CutterOnString cutter
    ) {
        //System.out.println("Enter com.leqee.kumori.sdk.dashscope.QwenVLKit.callWithSSE");

        Promise<Void> promise = Promise.promise();

        // 服务器发送事件 (Server-sent events)
        HttpClientOptions options = new HttpClientOptions()
                .setKeepAlive(true)
                .setSsl(true)
                .setDefaultHost("dashscope.aliyuncs.com")
                .setDefaultPort(443);
        HttpClient client = Keel.getVertx().createHttpClient(options);
        return client.request(HttpMethod.POST, "/api/v1/services/aigc/multimodal-generation/generation")
                .compose(httpClientRequest -> {
                    httpClientRequest
                            .putHeader("Content-Type", "application/json")
                            .putHeader("Authorization", "Bearer " + apiKey)
                            .putHeader("X-DashScope-SSE", "enable")
                    ;

                    var body = new JsonObject()
                            .put("model", model)
                            .put("input", qwenVLInput.toJsonObject())
                            .put("parameters", parameters.toJsonObject());

                    //System.out.println("BODY: "+body.toString());

                    return httpClientRequest.send(body.toString())
                            .compose(httpClientResponse -> {
                                httpClientResponse
                                        .handler(buffer -> {
                                            //System.out.println("buffer: "+buffer);
                                            cutter.handle(buffer);
                                        })
                                        .exceptionHandler(e -> {
                                            e.printStackTrace();
                                        })
                                        .endHandler(end -> {
                                            //System.out.println("Cutter END");
                                            cutter.end()
                                                    .onSuccess(cutterEnded -> {
                                                        promise.complete();
                                                    })
                                                    .onFailure(throwable -> {
                                                        promise.fail(throwable);
                                                    });
                                        });
                                return promise.future();
                            });
                });
    }
}
