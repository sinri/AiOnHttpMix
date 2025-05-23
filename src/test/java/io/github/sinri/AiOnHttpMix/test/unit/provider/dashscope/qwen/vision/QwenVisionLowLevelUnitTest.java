package io.github.sinri.AiOnHttpMix.test.unit.provider.dashscope.qwen.vision;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.test.unit.provider.core.AbstractModelRawUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.dashscope.qwen.QwenVisionModelSeries;
import org.junit.Test;

/**
 * 对Qwen的底层服务进行单元测试。
 */
public class QwenVisionLowLevelUnitTest extends AbstractQwenVisionModelUnitTest
        implements AbstractModelRawUnitTest<QwenVisionModelSeries> {


    public QwenVisionLowLevelUnitTest() {
    }

    @Test
    public void test1() {
        AigcMix.enableVerboseLogger();
        async(() -> {
            return toTestSync(requestWithoutToolCall.toJsonObject());
        });
    }

    @Test
    public void test2() {
        async(() -> {
            return toTestStream(requestWithoutToolCall
                    .parameters(p -> p.stream(true).incrementalOutput(true))
                    .toJsonObject()
            );
        });
    }
}
