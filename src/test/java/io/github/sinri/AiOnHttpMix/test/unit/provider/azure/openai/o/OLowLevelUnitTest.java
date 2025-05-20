package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai.o;

import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.Test;

import java.util.UUID;

public class OLowLevelUnitTest extends AbstractOServiceAdapterUnitTest {
    @Test
    public void test1() {
        JsonObject request = new JsonObject();
        request.put("messages", new JsonArray()
                .add(new JsonObject()
                        .put("role", "user")
                        .put("content", "chatgpt、claude和gemini的关系是什么")));
        async(() -> {
            return getServiceAdapter().request(
                                              ChatModel.chatgptO1,
                                              request,
                                              UUID.randomUUID().toString()
                                      )
                                      .compose(resp -> {
                                          getUnitTestLogger().info("resp", resp);
                                          return Future.succeededFuture();
                                      });
        });
    }
}
