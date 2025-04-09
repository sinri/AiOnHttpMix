package io.github.sinri.AiOnHttpMix.test.unit.anyvllm;

import io.github.sinri.AiOnHttpMix.mix.vl.AnyVLLMServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.SupportedVLModel;
import io.github.sinri.AiOnHttpMix.volces.core.VolcesServiceMeta;
import io.vertx.core.Future;
import org.junit.Test;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class AnyVLLMPureVolcesDoubaoUnitTest extends AbstractAnyVLLMUnitTest {
    private String getServiceName() {
        return "doubao-1.5-vision-pro-32k";
    }

    protected VolcesServiceMeta generateServiceMeta() {
        String serviceName = getServiceName();
        String apiKey = Keel.config("volces." + serviceName + ".apiKey");
        String model = Keel.config("volces." + serviceName + ".model");

        assert apiKey != null;
        assert model != null;
        return new VolcesServiceMeta(apiKey, model);
    }

    @Override
    protected AnyVLLMServiceAdapter buildAnyVLLMServiceAdapter() {
        return AnyVLLMServiceAdapter.throughSDK(generateServiceMeta(), SupportedVLModel.DoubaoVL);
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
