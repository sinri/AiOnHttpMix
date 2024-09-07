package io.github.sinri.AiOnHttpMix.test.mix;

import io.github.sinri.AiOnHttpMix.dashscope.core.DashscopeServiceMeta;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMKit;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class MixQwenTest extends MixTestCore {


    @Override
    protected @NotNull Future<Void> starting() {
        return super.starting()
                .compose(v -> {
                    String dashscopeApiKey = Keel.config("dashscope.api_key");

                    var serviceMeta = new DashscopeServiceMeta(dashscopeApiKey);
                    anyLLMKit = new AnyLLMKit().useQwen(serviceMeta, SupportedModel.QwenPlus);

                    return Future.succeededFuture();
                });
    }
}
