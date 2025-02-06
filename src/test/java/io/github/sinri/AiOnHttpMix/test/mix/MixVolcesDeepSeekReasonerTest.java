package io.github.sinri.AiOnHttpMix.test.mix;

import io.github.sinri.AiOnHttpMix.mix.AnyLLMKit;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.vertx.core.Future;
import org.junit.Before;
import org.junit.Test;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class MixVolcesDeepSeekReasonerTest extends MixTestCore {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        anyLLMKit = new AnyLLMKit().useVolces(getMirageSDK(), SupportedModel.DeepSeekReasonerOnVolces);
    }

    @Test
    public void testPureStream() {
        Keel.pseudoAwait(promise -> {
            pureStream()
                    .compose(v -> {
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void testPureNonStream() {
        Keel.pseudoAwait(promise -> {
            pureNonStream()
                    .compose(v -> {
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }
}
