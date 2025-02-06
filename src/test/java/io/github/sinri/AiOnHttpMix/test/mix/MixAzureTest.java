package io.github.sinri.AiOnHttpMix.test.mix;

import io.github.sinri.AiOnHttpMix.mix.AnyLLMKit;
import io.github.sinri.keel.facade.async.KeelAsyncKit;
import io.vertx.core.Future;
import org.junit.Before;
import org.junit.Test;

public class MixAzureTest extends MixTestCore {

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        anyLLMKit = new AnyLLMKit().useAzure(getMirageSDK());
    }

    @Test
    public void testPureStream() {
        KeelAsyncKit.pseudoAwait(promise -> {
            pureStream()
                    .compose(v -> {
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void testPureNonStream() {
        KeelAsyncKit.pseudoAwait(promise -> {
            pureNonStream()
                    .compose(v -> {
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void testFcNonStream() {
        KeelAsyncKit.pseudoAwait(promise -> {
            fcNonStream()
                    .compose(v -> {
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void testFcStream() {
        KeelAsyncKit.pseudoAwait(promise -> {
            fcStream()
                    .compose(v -> {
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void testMixFcNonStream() {
        KeelAsyncKit.pseudoAwait(promise -> {
            mixFcNonStream()
                    .compose(v -> {
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }

}
