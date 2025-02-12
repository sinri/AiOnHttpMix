package io.github.sinri.AiOnHttpMix.test.unit.deepseek;

import io.github.sinri.AiOnHttpMix.deepseek.DeepseekKit;
import io.github.sinri.AiOnHttpMix.deepseek.core.DeepseekServiceMeta;
import io.github.sinri.AiOnHttpMix.test.unit.AnyKitUnitTest;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class AbstractDeepSeekKitUnitTest extends AnyKitUnitTest<DeepseekServiceMeta, DeepseekKit> {
    @Override
    protected DeepseekServiceMeta generateServiceMeta() {
        String apiKey = Keel.config("DeepSeek.main.apiKey");
        return new DeepseekServiceMeta(apiKey);
    }

    @Override
    protected DeepseekKit generateKit() {
        return new DeepseekKit();
    }
}
