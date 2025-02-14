package io.github.sinri.AiOnHttpMix.test.unit.provider.dashscope.wanx.ImageSynthesis;

import io.github.sinri.AiOnHttpMix.dashscope.core.DashscopeServiceMeta;
import io.github.sinri.AiOnHttpMix.dashscope.wanx.DashscopeWanxKit;
import io.github.sinri.AiOnHttpMix.dashscope.wanx.ImageSynthesis.request.WanxImageSynthesisParameters;
import io.github.sinri.AiOnHttpMix.dashscope.wanx.ImageSynthesis.request.WanxImageSynthesisRequest;
import io.github.sinri.AiOnHttpMix.dashscope.wanx.task.WanxAsyncTaskResult;
import io.github.sinri.AiOnHttpMix.test.unit.core.AnyKitUnitTest;
import io.github.sinri.AiOnHttpMix.test.unit.core.AnyKitUnitTestRequestMixin;
import io.github.sinri.AiOnHttpMix.test.unit.core.TestPassed;
import io.vertx.core.Future;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class WanxCheckAsyncTaskUnitTest extends AnyKitUnitTest<DashscopeServiceMeta, DashscopeWanxKit>
        implements AnyKitUnitTestRequestMixin<WanxImageSynthesisRequest> {

    @Override
    protected DashscopeServiceMeta generateServiceMeta() {
        String dashscopeApiKey = Keel.config("dashscope.api_key");
        return new DashscopeServiceMeta(dashscopeApiKey);
    }

    @Override
    protected DashscopeWanxKit generateKit() {
        return new DashscopeWanxKit();
    }

    @Test
    @TestPassed(time = "2025-02-13")
    public void test() {
        this.async(() -> createTask()
                .compose(taskId -> Keel.asyncCallRepeatedly(callTask -> checkTask(taskId)
                        .compose(status -> {
                            if (status == WanxAsyncTaskResult.WanxTaskStatus.PENDING || status == WanxAsyncTaskResult.WanxTaskStatus.RUNNING) {
                                getUnitTestLogger().info("...");
                                return Keel.asyncSleep(1000L);
                            } else {
                                callTask.stop();
                                return Future.succeededFuture();
                            }
                        }))));
    }

    private Future<String> createTask() {
        return getKit().createImageSynthesisTask(
                               getServiceMeta(),
                               generateRequest(),
                               generateRequestId()
                       )
                       .compose(resp -> {
                           String taskId = resp.getTaskId();
                           getUnitTestLogger().info("task id: " + taskId);
                           Assert.assertNotNull(taskId);
                           return Future.succeededFuture(taskId);
                       });
    }

    private Future<WanxAsyncTaskResult.WanxTaskStatus> checkTask(String taskId) {
        return getKit().queryImageSynthesisTaskStatus(getServiceMeta(), taskId, UUID.randomUUID().toString())
                       .compose(resp -> {
                           WanxAsyncTaskResult.WanxTaskStatus taskStatus = resp.getTaskStatus();
                           getUnitTestLogger().info("task status: " + taskStatus);
                           if (taskStatus == WanxAsyncTaskResult.WanxTaskStatus.SUCCEEDED) {
                               resp.getResults().forEach(result -> {
                                   getUnitTestLogger().info("result url: " + result.url());
                               });
                           }
                           return Future.succeededFuture(taskStatus);
                       });
    }

    @Override
    public WanxImageSynthesisRequest generateRequest() {
        return WanxImageSynthesisRequest.create()
                                        .handleInput(input -> input
                                                .setPrompt("中东战乱，少女正在逃命")
                                        )
                                        .handleParameters(p -> p
                                                .setStyle(WanxImageSynthesisParameters.Style.FlatIllustration)
                                                .setN(1)
                                        );
    }
}
