package io.github.sinri.AiOnHttpMix.test.unit.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.dashscope.core.DashscopeServiceMeta;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.test.unit.AnyKitUnitTest;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public abstract class AbstractQwenKitUnitTest extends AnyKitUnitTest<DashscopeServiceMeta, QwenKit> {

    @Override
    protected DashscopeServiceMeta generateServiceMeta() {
        String dashscopeApiKey = Keel.config("dashscope.api_key");
        return new DashscopeServiceMeta(dashscopeApiKey);
    }

    @Override
    protected QwenKit generateKit() {
        return new QwenKit();
    }
}
