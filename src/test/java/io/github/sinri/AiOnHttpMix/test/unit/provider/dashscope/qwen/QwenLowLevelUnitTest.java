package io.github.sinri.AiOnHttpMix.test.unit.provider.dashscope.qwen;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.Test;

import java.util.UUID;

/**
 * 对Qwen的底层服务进行单元测试。
 */
public class QwenLowLevelUnitTest extends AbstractQwenServiceAdapterUnitTest {
    public QwenLowLevelUnitTest() {
    }

    private JsonObject generateRequest(boolean useStreamIncrement) {
        JsonObject request = new JsonObject();
        request.put("input", new JsonObject()
                .put("messages", new JsonArray()
                        .add(new JsonObject()
                                .put("role", "user")
                                .put("content", "歼10CE和阵风哪个厉害？"))));
        JsonObject parameters = new JsonObject()
                .put("result_format", "message")
                .put("temperature", 0.7)
                .put("top_p", 0.8)
                .put("max_tokens", 1500);
        if (useStreamIncrement) {
            parameters.put("stream", true)
                      .put("incremental_output", true);
        }
        request.put("parameters", parameters);
        return request;
    }

    @Test
    public void test1() {
        async(() -> {
            return getServiceAdapter().request(
                    qwenPlus,
                    generateRequest(false),
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
                    qwenPlus,
                    generateRequest(true),
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
