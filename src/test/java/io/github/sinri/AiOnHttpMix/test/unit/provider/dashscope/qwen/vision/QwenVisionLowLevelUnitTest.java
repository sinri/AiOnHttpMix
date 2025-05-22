package io.github.sinri.AiOnHttpMix.test.unit.provider.dashscope.qwen.vision;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.vertx.core.Future;
import org.junit.Test;

import java.util.UUID;

/**
 * 对Qwen的底层服务进行单元测试。
 */
public class QwenVisionLowLevelUnitTest extends AbstractQwenVisionModelUnitTest {


    public QwenVisionLowLevelUnitTest() {
    }

    @Test
    public void test1() {
        AigcMix.enableVerboseLogger();
        async(() -> {
            return getServiceAdapter().request(
                    getModel(),
                    requestWithoutToolCall.toJsonObject(),
                    UUID.randomUUID().toString()).compose(resp -> {
                getUnitTestLogger().info("resp", resp);
                return Future.succeededFuture();
            });
        });
    }

    @Test
    public void test2() {
        async(() -> {
            return getServiceAdapter().requestStream(
                    getModel(),
                    requestWithoutToolCall
                            .parameters(p -> p.stream(true).incrementalOutput(true))
                            .toJsonObject(),
                    chunk -> {
                        getUnitTestLogger().info("chunk: " + chunk);
                        return Future.succeededFuture();
                    },
                    180_000L,
                    UUID.randomUUID().toString()).compose(v -> {
                getUnitTestLogger().info("fin");
                return Future.succeededFuture();
            });
        });
    }
}
