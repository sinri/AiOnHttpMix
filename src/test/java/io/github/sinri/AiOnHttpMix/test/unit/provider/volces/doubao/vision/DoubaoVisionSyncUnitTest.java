package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.doubao.vision;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.DoubaoMessageInResponse;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.sync.DoubaoResponseChoice;
import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolCall;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class DoubaoVisionSyncUnitTest extends AbstractDoubaoVisionModelUnitTest {


    @Test
    public void test1() {
        AigcMix.enableVerboseLogger();
        async(() -> {
            return getKit().chat(
                                   getModel(),
                                   requestWithoutToolCall,
                                   UUID.randomUUID().toString()
                           )
                           .compose(resp -> {
                               getUnitTestLogger().info("resp", resp.cloneAsJsonObject());

                               List<DoubaoResponseChoice> choices = resp.getChoices();
                               DoubaoResponseChoice choice = choices.get(0);
                               DoubaoMessageInResponse message = choice.getMessage();
                               getUnitTestLogger().info("role: " + message.getRole());
                               getUnitTestLogger().info("reasoning content: " + message.getReasoningContent());
                               getUnitTestLogger().info("content: " + message.getContent());

                               return Future.succeededFuture();
                           });
        });
    }

    @Test
    public void test2() {
        async(() -> {
            AtomicReference<DoubaoMessageInResponse> toolCallMessageRef = new AtomicReference<>();

            return getKit().chat(
                                   getModel(),
                                   requestWithToolCall,
                                   UUID.randomUUID().toString()
                           )
                           .compose(resp -> {
                               getUnitTestLogger().info("resp", resp.cloneAsJsonObject());

                               List<DoubaoResponseChoice> choices = resp.getChoices();
                               DoubaoResponseChoice choice = choices.get(0);
                               DoubaoMessageInResponse message = choice.getMessage();
                               getUnitTestLogger().info("role: " + message.getRole());
                               getUnitTestLogger().info("reasoning content: " + message.getReasoningContent());
                               getUnitTestLogger().info("content: " + message.getContent());

                               List<ToolCall> toolCalls = message.getToolCalls();
                               var tc = toolCalls.get(0);
                               FunctionToolCall function = tc.getFunction();
                               getUnitTestLogger().info("function " + function.getName() + "(" + function.getArguments() + ")");

                               toolCallMessageRef.set(message);

                               Assert.assertEquals("query_current_weather", function.getName());

                               var a = new JsonObject(function.getArguments());
                               var place = a.getString("place");

                               return Future.succeededFuture(new JsonArray()
                                       .add(new JsonObject()
                                               .put("date", Keel.datetimeHelper().getCurrentDate())
                                               .put("place", place)
                                               .put("weather", "目前有小雨，气温23摄氏度，无风。")
                                       )
                                       .toString()
                               );
                           })
                           .compose(toolCallOutputContent -> {
                               DoubaoMessageInResponse msg = toolCallMessageRef.get();
                               requestWithToolCall.addToolCallChatMessage(msg.getContent(), msg.getToolCalls());
                               requestWithToolCall.addToolOutputChatMessage(toolCallOutputContent, msg.getToolCalls()
                                                                                                      .get(0)
                                                                                                      .getId());

                               return getKit().chat(
                                       getModel(),
                                       requestWithToolCall,
                                       UUID.randomUUID().toString()
                               );
                           })
                           .compose(resp -> {
                               getUnitTestLogger().info("resp", resp.cloneAsJsonObject());

                               List<DoubaoResponseChoice> choices = resp.getChoices();
                               DoubaoResponseChoice choice = choices.get(0);
                               DoubaoMessageInResponse message = choice.getMessage();
                               getUnitTestLogger().info("role: " + message.getRole());
                               getUnitTestLogger().info("reasoning content: " + message.getReasoningContent());
                               getUnitTestLogger().info("content: " + message.getContent());

                               return Future.succeededFuture();
                           });
        });
    }
}
