package io.github.sinri.AiOnHttpMix.test.dashscope.wanx.ImageSynthesis;

import io.github.sinri.AiOnHttpMix.dashscope.wanx.DashscopeWanxKit;
import io.github.sinri.AiOnHttpMix.dashscope.wanx.task.WanxAsyncTaskResult;
import io.github.sinri.AiOnHttpMix.test.dashscope.DashscopeTestCore;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;

import java.util.UUID;

public class WanxTestToCheckAsyncTaskStatus extends DashscopeTestCore {
    @TestUnit
    public Future<Void> testToCheckAsyncTaskStatus() {
        var task_id = "a3a7ce6a-f2d8-4bef-97b0-654f79be229c";
        return new DashscopeWanxKit().queryImageSynthesisTaskStatus(getServiceMeta(), task_id, UUID.randomUUID().toString())
                .compose(resp -> {
                    getLogger().info("task status: " + resp.getTaskStatus());
                    if (resp.getTaskStatus() == WanxAsyncTaskResult.WanxTaskStatus.SUCCEEDED) {
                        resp.getResults().forEach(result -> {
                            getLogger().info("result: " + result);
                        });
                    }
                    return Future.succeededFuture();
                });
    }
}
