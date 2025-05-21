package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.Test;

import java.util.UUID;

public class GptLowLevelUnitTest extends AbstractGptServiceAdapterUnitTest {
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
    public void test1() {
        async(() -> {
            return getServiceAdapter()
                    .request(
                            gpt4o,
                            generateRequest(false),
                            UUID.randomUUID().toString()
                    )
                    .compose(resp -> {
                        getUnitTestLogger().info("resp", resp);
                        return Future.succeededFuture();
                    });
        });
    }

    @Test
    public void test2() {
        async(() -> {
            return getServiceAdapter()
                    .requestStream(
                            gpt4o,
                            generateRequest(true),
                            chunk -> {
                                getUnitTestLogger().info("chunk: " + chunk);
                                return Future.succeededFuture();
                            },
                            180_000L,
                            UUID.randomUUID().toString()
                    )
                    .compose(resp -> {
                        getUnitTestLogger().info("fin");
                        return Future.succeededFuture();
                    });
        });
    }
}
