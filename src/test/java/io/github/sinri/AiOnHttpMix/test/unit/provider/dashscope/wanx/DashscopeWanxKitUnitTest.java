package io.github.sinri.AiOnHttpMix.test.unit.provider.dashscope.wanx;

import io.github.sinri.AiOnHttpMix.provider.dashscope.wanx.DashscopeWanxKit;
import io.github.sinri.AiOnHttpMix.provider.dashscope.wanx.ImageSynthesis.request.WanxImageSynthesisModel;
import io.github.sinri.AiOnHttpMix.provider.dashscope.wanx.ImageSynthesis.request.WanxImageSynthesisParameters;
import io.github.sinri.AiOnHttpMix.provider.dashscope.wanx.ImageSynthesis.request.WanxImageSynthesisRequest;
import io.github.sinri.AiOnHttpMix.provider.dashscope.wanx.ImageSynthesis.response.WanxImageSynthesisAsyncTaskResult;
import io.github.sinri.AiOnHttpMix.provider.dashscope.wanx.task.WanxAsyncTaskResult;
import io.github.sinri.keel.facade.tesuto.unit.KeelUnitTest;
import io.vertx.core.Future;
import org.junit.Test;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class DashscopeWanxKitUnitTest extends KeelUnitTest {
    @Test
    public void test1() {
        async(() -> {
            String apiKey = Keel.config("dashscope.api_key");

            DashscopeWanxKit dashscopeWanxKit = new DashscopeWanxKit(apiKey);

            AtomicReference<String> taskIdRef = new AtomicReference<>();

            return dashscopeWanxKit.createImageSynthesisTask(
                                           WanxImageSynthesisRequest.create()
                                                                    .setModel(WanxImageSynthesisModel.WanxV1)
                                                                    .handleInput(input -> input
                                                                            .setPrompt("高高的山岗上，雪绒花开放")
                                                                    )
                                                                    .handleParameters(p -> p
                                                                            .setStyle(WanxImageSynthesisParameters.Style.anime)
                                                                    ),
                                           UUID.randomUUID().toString()
                                   )
                                   .compose(resp -> {
                                       getUnitTestLogger().info("resp", resp.cloneAsJsonObject());

                                       String taskId = resp.getTaskId();
                                       getUnitTestLogger().info("task id: " + taskId);

                                       taskIdRef.set(taskId);

                                       return Keel.asyncSleep(1000L);
                                   })
                                   .compose(v -> {
                                       return Keel.asyncCallRepeatedly(repeatedlyCallTask -> {
                                           String taskId = taskIdRef.get();
                                           return dashscopeWanxKit.queryImageSynthesisTaskStatus(taskId, UUID.randomUUID()
                                                                                                             .toString())
                                                                  .compose(resp -> {
                                                                      WanxAsyncTaskResult.WanxTaskStatus taskStatus = resp.getTaskStatus();
                                                                      if (taskStatus == WanxAsyncTaskResult.WanxTaskStatus.SUCCEEDED) {
                                                                          getUnitTestLogger().info("task status: " + taskStatus);
                                                                          List<WanxImageSynthesisAsyncTaskResult.Result> results = resp.getResults();
                                                                          results.forEach(result -> {
                                                                              getUnitTestLogger().info("result: " + result.url());
                                                                          });
                                                                          repeatedlyCallTask.stop();
                                                                          return Future.succeededFuture();
                                                                      } else if (taskStatus == WanxAsyncTaskResult.WanxTaskStatus.FAILED) {
                                                                          getUnitTestLogger().error("failed, message: " + resp.getMessage());
                                                                          repeatedlyCallTask.stop();
                                                                          return Future.succeededFuture();
                                                                      } else {
                                                                          return Keel.asyncSleep(3000L);
                                                                      }
                                                                  });
                                       });
                                   });
        });
    }
}
