package io.github.sinri.AiOnHttpMix.provider.dashscope.wanx;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.provider.dashscope.wanx.ImageSynthesis.request.WanxImageSynthesisRequest;
import io.github.sinri.AiOnHttpMix.provider.dashscope.wanx.ImageSynthesis.response.WanxImageSynthesisAsyncTaskCreateResult;
import io.github.sinri.AiOnHttpMix.provider.dashscope.wanx.ImageSynthesis.response.WanxImageSynthesisAsyncTaskResult;
import io.github.sinri.AiOnHttpMix.utils.AbnormalResponse;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.Map;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class DashscopeWanxKit {
    private final static String endpointOfDashscopeAsyncTaskQuery = "https://dashscope.aliyuncs.com/api/v1/tasks/";//{task_id}
    private final static String endpointOfDashscopeWanxiangImageSynthesis = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text2image/image-synthesis";

    private final String apiKey;

    public DashscopeWanxKit(String apiKey) {
        this.apiKey = apiKey;
    }

    public Future<JsonObject> createImageSynthesisTask(JsonObject requestBody, String requestId) {
        return callWanxiangImageSynthesis(requestBody, requestId);
    }

    public Future<WanxImageSynthesisAsyncTaskCreateResult> createImageSynthesisTask(WanxImageSynthesisRequest request, String requestId) {
        return createImageSynthesisTask(request.toJsonObject(), requestId)
                .compose(j -> {
                    var r = WanxImageSynthesisAsyncTaskCreateResult.wrap(j);
                    return Future.succeededFuture(r);
                });
    }

    public Future<WanxImageSynthesisAsyncTaskResult> queryImageSynthesisTaskStatus(String taskId, String requestId) {
        return callAsyncTaskQuery(taskId, requestId)
                .compose(resp -> {
                    var x = new WanxImageSynthesisAsyncTaskResult(resp);
                    return Future.succeededFuture(x);
                });
    }

    /**
     * @since 1.1.6
     */
    private Future<JsonObject> callWanxiangImageSynthesis(JsonObject requestBody, String requestId) {
        return request(
                endpointOfDashscopeWanxiangImageSynthesis,
                Map.of("X-DashScope-Async", "enable"),
                requestBody,
                requestId
        );
    }

    private Future<JsonObject> callAsyncTaskQuery(String taskId, String requestId) {
        return Keel.useWebClient(webClient -> webClient
                           .getAbs(endpointOfDashscopeAsyncTaskQuery + taskId)
                           .putHeader("Authorization", "Bearer " + apiKey)
                           .send())
                   .compose(resp -> {
                       var r = resp.bodyAsJsonObject();
                       return Future.succeededFuture(r);
                   });
    }

    private Future<JsonObject> request(String api, JsonObject requestBody, String requestId) {
        return this.request(api, Map.of(), requestBody, requestId);
    }

    /**
     * @since 1.1.6
     */
    private Future<JsonObject> request(String api, Map<String, String> headers, JsonObject requestBody, String requestId) {
        AigcMix.getVerboseLogger().info(x -> x
                .message("Start DashscopeWanxKit.request")
                .context(j -> j
                        .put("api", api)
                        .put("requestId", requestId)
                        .put("input", requestBody)
                )
        );

        return Keel.useWebClient(webClient -> {
            var req = webClient
                    .postAbs(api)
                    .putHeader("Content-Type", "application/json")
                    .putHeader("Authorization", "Bearer " + apiKey);

            headers.forEach(req::putHeader);

            return req.sendJsonObject(requestBody)
                      .compose(bufferHttpResponse -> {
                          int statusCode = bufferHttpResponse.statusCode();
                          if (statusCode != 200) {
                              AigcMix.getVerboseLogger().error(x -> x
                                      .message("Unexpected bufferHttpResponse in DashscopeServiceMeta.request")
                                      .context(
                                              j -> j
                                                      .put("requestId", requestId)
                                                      .put("status_code", statusCode)
                                                      .put("detail", bufferHttpResponse.bodyAsString())
                                      )
                              );

                              return Future.failedFuture(new AbnormalResponse(bufferHttpResponse));
                          } else {
                              JsonObject entries = bufferHttpResponse.bodyAsJsonObject();

                              AigcMix.getVerboseLogger().info(x -> x
                                      .message("bufferHttpResponse in DashscopeServiceMeta.request")
                                      .context(j -> j
                                              .put("requestId", requestId)
                                              .put("output", entries))
                              );

                              return Future.succeededFuture(entries);
                          }
                      });
        });
    }

}
