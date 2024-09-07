package io.github.sinri.AiOnHttpMix.test.mix;

import io.github.sinri.AiOnHttpMix.mix.AnyLLMKit;
import io.github.sinri.AiOnHttpMix.volces.core.VolcesServiceMeta;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class MixVolcesTest extends MixTestCore {
    @Override
    protected @NotNull Future<Void> starting() {
        return super.starting()
                .compose(v -> {
                    String apiKey = Keel.config("volces.doubao-pro-128k.apiKey");
                    String model = Keel.config("volces.doubao-pro-128k.model");

                    var serviceMeta = new VolcesServiceMeta(apiKey, model);
                    anyLLMKit = new AnyLLMKit().useVolces(serviceMeta);

                    return Future.succeededFuture();
                });
    }
}
