package io.github.sinri.AiOnHttpMix.test.dashscope.wanx.ImageSynthesis;

import io.github.sinri.AiOnHttpMix.dashscope.wanx.DashscopeWanxKit;
import io.github.sinri.AiOnHttpMix.dashscope.wanx.task.WanxAsyncTaskResult;
import io.github.sinri.AiOnHttpMix.test.dashscope.DashscopeTestCore;
import io.vertx.core.Future;
import org.junit.Test;

import java.util.UUID;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class WanxCheckAsyncTaskStatusUnitTest extends DashscopeTestCore {
    @Test
    public void testToCheckAsyncTaskStatus() {
        var task_id = "a3a7ce6a-f2d8-4bef-97b0-654f79be229c";
        Keel.pseudoAwait(promise -> {
            new DashscopeWanxKit().queryImageSynthesisTaskStatus(getServiceMeta(), task_id, UUID.randomUUID().toString())
                    .compose(resp -> {
                        getLogger().info("task status: " + resp.getTaskStatus());
                        if (resp.getTaskStatus() == WanxAsyncTaskResult.WanxTaskStatus.SUCCEEDED) {
                            resp.getResults().forEach(result -> {
                                getLogger().info("result: " + result);
                            });
                        }
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }
}
