package io.github.sinri.AiOnHttpMix.dashscope;


import io.github.sinri.AiOnHttpMix.dashscope.drawing.ApplyTaskParameters;
import io.github.sinri.AiOnHttpMix.dashscope.drawing.ApplyTaskResponse;
import io.github.sinri.AiOnHttpMix.dashscope.drawing.QueryTaskResponse;
import io.github.sinri.keel.logger.event.KeelEventLogger;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class TongyiWanxiangKit {
    public static final String MODEL_WANX_V1 = "wanx-v1";
    private final String apiKey;

    public TongyiWanxiangKit(String apiKey) {
        this.apiKey = apiKey;

    }

//    public TongyiWanxiangKit() {
//        this(Keel.config("dashscope.api_key"));
//    }

    public Future<ApplyTaskResponse> applyTask(String prompt, ApplyTaskParameters parameters) {
        JsonObject body = new JsonObject()
                .put("model", MODEL_WANX_V1)
                .put("input", new JsonObject()
                        .put("prompt", prompt)
                );
        body.put("parameters", parameters.toJsonObject());

        return WebClient.create(Keel.getVertx())
                .postAbs("https://dashscope.aliyuncs.com/api/v1/services/aigc/text2image/image-synthesis")
                .putHeader("Content-Type", "application/json")
                .putHeader("X-DashScope-Async", "enable")
                .bearerTokenAuthentication(apiKey)
                .sendJsonObject(body)
                .compose(bufferHttpResponse -> {
                    int statusCode = bufferHttpResponse.statusCode();
                    JsonObject entries = bufferHttpResponse.bodyAsJsonObject();
//                    if (entries == null) {
//                        entries = new JsonObject().put("raw", bufferHttpResponse.bodyAsString());
//                    }
//                    this.logger.info(eventLog -> eventLog
//                            .message("apply task api responded " + statusCode)
//                            .context(c -> c
//                                    .put("input", body)
//                                    .put("output", entries)
//                            )
//                    );
                    return Future.succeededFuture(new ApplyTaskResponse(statusCode, entries));
                });
    }

    public Future<QueryTaskResponse> queryTask(String taskId) {
        return WebClient.create(Keel.getVertx())
                .getAbs("https://dashscope.aliyuncs.com/api/v1/tasks/" + taskId)
                .bearerTokenAuthentication(apiKey)
                .send()
                .compose(bufferHttpResponse -> {
                    int statusCode = bufferHttpResponse.statusCode();
                    JsonObject entries = bufferHttpResponse.bodyAsJsonObject();
//                    System.out.println("code: " + bufferHttpResponse.statusCode());
//                    System.out.println(bufferHttpResponse.bodyAsString());
//                    this.logger.info(eventLog -> eventLog
//                            .message("query task api responded " + statusCode)
//                            .context(c -> c
//                                    .put("input", new JsonObject().put("task_id", taskId))
//                                    .put("output", entries)
//                            )
//                    );
                    return Future.succeededFuture(new QueryTaskResponse(statusCode, bufferHttpResponse.bodyAsJsonObject()));
                });
    }

}
