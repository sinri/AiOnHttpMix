package io.github.sinri.AiOnHttpMix.test.unit.provider.volces;

import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.series.ChatModelSeries;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import io.github.sinri.keel.facade.tesuto.unit.KeelUnitTest;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class DoubaoLowLevelUnitTest extends KeelUnitTest {
    public DoubaoLowLevelUnitTest() {
    }

    @Before
    @Override
    public void setUp() {

    }

    private JsonObject generateRequest(boolean useStreamIncrement) {
        JsonObject request = new JsonObject();
        request.put("stream", useStreamIncrement);
        request.put("temperature", 0.7);
        request.put("top_p", 0.95);
        request.put("max_tokens", 2048);

        JsonArray messages = new JsonArray();

        JsonObject systemMessage = new JsonObject()
                .put("role", "system")
                .put("content", "You are a helpful assistant.");
        messages.add(systemMessage);

        JsonObject userMessage = new JsonObject()
                .put("role", "user")
                .put("content", "萧山机场到杭州东站怎么走？");
        messages.add(userMessage);

        request.put("messages", messages);
        return request;
    }

    @Test
    public void test1() {
        async(() -> {
            KeelConfigElement doubaoConfig = Keel.getConfiguration()
                                                 .extract("provider", "volces", "doubao");
            Assert.assertNotNull(doubaoConfig);
            ChatModelServiceAdapter chatModelServiceAdapter = ChatModelSeries.doubao.buildServiceMeta(doubaoConfig);
            return chatModelServiceAdapter.request(
                                                  ChatModel.doubaoPro32k,
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
            KeelConfigElement doubaoConfig = Keel.getConfiguration()
                                                 .extract("provider", "volces", "doubao");
            Assert.assertNotNull(doubaoConfig);
            ChatModelServiceAdapter chatModelServiceAdapter = ChatModelSeries.doubao.buildServiceMeta(doubaoConfig);
            return chatModelServiceAdapter.requestStream(
                                                  ChatModel.doubaoPro32k,
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
