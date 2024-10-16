package io.github.sinri.AiOnHttpMix.dashscope.wanx;

import io.github.sinri.AiOnHttpMix.dashscope.core.DashscopeServiceMeta;
import io.github.sinri.AiOnHttpMix.dashscope.wanx.ImageSynthesis.request.WanxImageSynthesisRequest;
import io.github.sinri.AiOnHttpMix.dashscope.wanx.ImageSynthesis.response.WanxImageSynthesisAsyncTaskCreateResult;
import io.github.sinri.AiOnHttpMix.dashscope.wanx.ImageSynthesis.response.WanxImageSynthesisAsyncTaskResult;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class DashscopeWanxKit {
    public DashscopeWanxKit() {
        super();
    }

    public Future<JsonObject> createImageSynthesisTask(DashscopeServiceMeta serviceMeta, JsonObject requestBody, String requestId) {
        return serviceMeta.callWanxiangImageSynthesis(requestBody, requestId);
    }

    public Future<WanxImageSynthesisAsyncTaskCreateResult> createImageSynthesisTask(DashscopeServiceMeta serviceMeta, WanxImageSynthesisRequest request, String requestId) {
        return createImageSynthesisTask(serviceMeta, request.toJsonObject(), requestId)
                .compose(j -> {
                    var r = WanxImageSynthesisAsyncTaskCreateResult.wrap(j);
                    return Future.succeededFuture(r);
                });
    }

    public Future<WanxImageSynthesisAsyncTaskResult> queryImageSynthesisTaskStatus(DashscopeServiceMeta serviceMeta, String taskId, String requestId) {
        return serviceMeta.callAsyncTaskQuery(taskId, requestId)
                .compose(resp -> {
                    var x = new WanxImageSynthesisAsyncTaskResult(resp);
                    return Future.succeededFuture(x);
                });
    }
}
