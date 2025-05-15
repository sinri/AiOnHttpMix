package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai;

import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.model.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.series.ChatModelSeries;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import io.github.sinri.keel.facade.tesuto.unit.KeelUnitTest;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class ChatGptLowLevelUnitTest extends KeelUnitTest {
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
            KeelConfigElement chatgptConfig = Keel.getConfiguration()
                                                  .extract("provider", "azure", "openai", "EighthTower");
            Assert.assertNotNull(chatgptConfig);
            ChatModelServiceAdapter chatModelServiceAdapter = ChatModelSeries.chatgpt.buildServiceMeta(chatgptConfig);
            return chatModelServiceAdapter.request(
                                                  ChatModel.chatgpt4o,
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
            KeelConfigElement chatgptConfig = Keel.getConfiguration()
                                                  .extract("provider", "azure", "openai", "EighthTower");
            Assert.assertNotNull(chatgptConfig);
            ChatModelServiceAdapter chatModelServiceAdapter = ChatModelSeries.chatgpt.buildServiceMeta(chatgptConfig);
            return chatModelServiceAdapter.requestStream(
                                                  ChatModel.chatgpt4o,
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
