package io.github.sinri.AiOnHttpMix.test.unit.anyvllm;

import io.github.sinri.AiOnHttpMix.dashscope.core.DashscopeServiceMeta;
import io.github.sinri.AiOnHttpMix.mix.vl.AnyVLLMServiceAdapter;
import io.github.sinri.AiOnHttpMix.mix.vl.AnyVLLMServiceAdapterThroughSDK;
import io.github.sinri.AiOnHttpMix.utils.SupportedVLModel;
import io.vertx.core.Future;
import org.junit.Test;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class AnyVLLMPureDashScopeQwenUnitTest extends AbstractAnyVLLMUnitTest {
    protected DashscopeServiceMeta generateServiceMeta() {
        String dashscopeApiKey = Keel.config("dashscope.api_key");
        return new DashscopeServiceMeta(dashscopeApiKey);
    }

    @Override
    protected AnyVLLMServiceAdapter buildAnyVLLMServiceAdapter() {
        return new AnyVLLMServiceAdapterThroughSDK()
                .setServiceMeta(generateServiceMeta())
                .setVlModel(SupportedVLModel.QwenVLPlus);
    }


    @Test
    public void testCommonVL() {
        async(() -> withAnyVLLMKit(anyVLLMKit -> {
            return anyVLLMKit.request(buildVLLMRequest())
                             .compose(anyVLLMResponse -> {
                                 getUnitTestLogger().info("anyVLLMResponse", anyVLLMResponse.toJsonObject());
                                 return Future.succeededFuture();
                             });
        }));
    }
}
