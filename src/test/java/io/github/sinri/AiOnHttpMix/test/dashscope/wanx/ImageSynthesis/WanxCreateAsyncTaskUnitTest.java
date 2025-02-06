package io.github.sinri.AiOnHttpMix.test.dashscope.wanx.ImageSynthesis;

import io.github.sinri.AiOnHttpMix.dashscope.wanx.DashscopeWanxKit;
import io.github.sinri.AiOnHttpMix.dashscope.wanx.ImageSynthesis.request.WanxImageSynthesisParameters;
import io.github.sinri.AiOnHttpMix.dashscope.wanx.ImageSynthesis.request.WanxImageSynthesisRequest;
import io.github.sinri.AiOnHttpMix.test.dashscope.DashscopeTestCore;
import io.vertx.core.Future;
import org.junit.Test;

import java.util.UUID;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class WanxCreateAsyncTaskUnitTest extends DashscopeTestCore {
    @Test
    public void test1() {
        Keel.pseudoAwait(promise -> {
            new DashscopeWanxKit().createImageSynthesisTask(
                            getServiceMeta(),
                            WanxImageSynthesisRequest.create()
                                    .handleInput(input -> input
                                            .setPrompt("阳光下的泳池中，少女躺在水面扑腾")
                                    )
                                    .handleParameters(p -> p
                                            .setStyle(WanxImageSynthesisParameters.Style.FlatIllustration)
                                            .setN(1)
                                    )
                            ,
                            UUID.randomUUID().toString()
                    )
                    .compose(resp -> {
                        getLogger().info("task id: " + resp.getTaskId());
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }


}
