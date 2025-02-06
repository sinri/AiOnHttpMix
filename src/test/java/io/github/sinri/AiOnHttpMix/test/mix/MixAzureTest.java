package io.github.sinri.AiOnHttpMix.test.mix;

import io.github.sinri.AiOnHttpMix.mix.AnyLLMKit;
import io.vertx.core.Future;
import org.junit.Before;
import org.junit.Test;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class MixAzureTest extends MixTestCore {

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        anyLLMKit = new AnyLLMKit().useAzure(getMirageSDK());
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

    @Test
    public void testFcNonStream() {
        Keel.pseudoAwait(promise -> {
            fcNonStream()
                    .compose(v -> {
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void testFcStream() {
        Keel.pseudoAwait(promise -> {
            fcStream()
                    .compose(v -> {
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void testMixFcNonStream() {
        Keel.pseudoAwait(promise -> {
            mixFcNonStream()
                    .compose(v -> {
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }

}
