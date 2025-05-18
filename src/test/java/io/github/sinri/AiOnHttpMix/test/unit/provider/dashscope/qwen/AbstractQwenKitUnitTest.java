package io.github.sinri.AiOnHttpMix.test.unit.provider.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.QwenKit;
import org.junit.Before;

/**
 * 基于{@link QwenKit}，对Qwen系列LLM进行集成测试的抽象类。
 */
public class AbstractQwenKitUnitTest extends AbstractQwenServiceAdapterUnitTest {
    private QwenKit qwenKit;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        qwenKit = buildQwenKit();
    }

    private QwenKit buildQwenKit() {
        return new QwenKit();
    }

    protected final QwenKit getQwenKit() {
        return qwenKit;
    }
}
