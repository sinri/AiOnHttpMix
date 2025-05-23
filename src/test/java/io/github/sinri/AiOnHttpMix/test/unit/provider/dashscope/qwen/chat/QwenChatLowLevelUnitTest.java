package io.github.sinri.AiOnHttpMix.test.unit.provider.dashscope.qwen.chat;

import io.github.sinri.AiOnHttpMix.test.unit.provider.core.AbstractModelRawUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.dashscope.qwen.QwenChatModelSeries;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.Test;

/**
 * 对Qwen的底层服务进行单元测试。
 */
public class QwenChatLowLevelUnitTest extends AbstractQwenChatModelUnitTest
        implements AbstractModelRawUnitTest<QwenChatModelSeries> {
    public QwenChatLowLevelUnitTest() {
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
            return toTestSync(generateRequest(false));
        });
    }

    @Test
    public void test2() {
        async(() -> {
            return toTestStream(generateRequest(true));
        });
    }
}
