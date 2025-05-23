package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.test.unit.provider.core.AbstractModelRawUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.azure.openai.gpt.GPTChatModelSeries;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.Test;

public class GptLowLevelUnitTest extends AbstractGptUnitTest implements AbstractModelRawUnitTest<GPTChatModelSeries> {
    private JsonObject generateRequest(boolean useStreamIncrement) {
        JsonObject request = new JsonObject();
        request.put("messages", new JsonArray()
                .add(new JsonObject()
                        .put("role", "user")
                        .put("content", "chatgpt、claude和gemini的关系是什么")));
        request.put("temperature", 0.7);
        request.put("max_tokens", 1000);
        request.put("top_p", 0.95);
        if (useStreamIncrement) {
            request.put("stream", true);
        }
        return request;

    }

    @Test
    public void testSync() {
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
