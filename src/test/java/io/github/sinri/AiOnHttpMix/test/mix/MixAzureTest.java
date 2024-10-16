package io.github.sinri.AiOnHttpMix.test.mix;

import io.github.sinri.AiOnHttpMix.mix.AnyLLMKit;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

public class MixAzureTest extends MixTestCore {

    @Override
    protected @NotNull Future<Void> starting() {
        return super.starting()
                .compose(v -> {
                    anyLLMKit = new AnyLLMKit().useChatGPT(getMirageSDK());
                    return Future.succeededFuture();
                });

    }

    @TestUnit(skip = false)
    @Override
    public Future<Void> pureStream() {
        return super.pureStream();
    }

    @TestUnit(skip = false)
    @Override
    public Future<Void> pureNonStream() {
        return super.pureNonStream();
    }

    @TestUnit(skip = false)
    @Override
    public Future<Void> fcNonStream() {
        return super.fcNonStream();
    }

    @TestUnit(skip = false)
    @Override
    public Future<Void> fcStream() {
        return super.fcStream();
    }

    @TestUnit(skip = false)
    @Override
    public Future<Void> mixFcNonStream() {
        return super.mixFcNonStream();
    }

}
