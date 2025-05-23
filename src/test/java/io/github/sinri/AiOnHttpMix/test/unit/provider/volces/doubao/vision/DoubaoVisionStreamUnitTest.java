package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.doubao.vision;

import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.DoubaoMessageInResponse;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.stream.DoubaoResponseChunkChoice;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.stream.DoubaoResponseChunkChoiceDelta;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.sync.DoubaoResponseChoice;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.tool.DoubaoToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolCall;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class DoubaoVisionStreamUnitTest extends AbstractDoubaoVisionModelUnitTest {
    @Test
    public void test1() {
        async(() -> {
            return getKit().chatStream(
                                   getModel(),
                                   requestWithoutToolCall.toJsonObject(),
                                   fragment -> {
                                       getUnitTestLogger().info("fragment:\n" + fragment);
                                       return Future.succeededFuture();
                                   },
                                   180_000L,
                                   UUID.randomUUID().toString()
                           )
                           .compose(v -> {
                               getUnitTestLogger().info("fin");
                               return Future.succeededFuture();
                           });
        });
    }

    @Test
    public void test2() {
        //        AigcMix.enableVerboseLogger();
        async(() -> {
            return getKit().chatStream(
                                   getModel(),
                                   requestWithoutToolCall,
                                   chunk -> {
                                       getUnitTestLogger().info("chunk", chunk.cloneAsJsonObject());

                                       List<DoubaoResponseChunkChoice> choices = chunk.getChoices();
                                       DoubaoResponseChunkChoice choice = choices.get(0);

                                       //                                       getUnitTestLogger().info("chunk choice first: ",choice.cloneAsJsonObject());

                                       DoubaoResponseChunkChoiceDelta delta = choice.getDelta();
                                       String role = delta.getRole();
                                       String reasoningContent = delta.getReasoningContent();
                                       String content = delta.getContent();
                                       getUnitTestLogger().info("delta role: " + role);
                                       getUnitTestLogger().info("delta reasoningContent: " + reasoningContent);
                                       getUnitTestLogger().info("delta content: " + content);

                                       return Future.succeededFuture();
                                   },
                                   180_000L,
                                   UUID.randomUUID().toString()
                           )
                           .compose(v -> {
                               getUnitTestLogger().info("fin");
                               return Future.succeededFuture();
                           });
        });
    }

    @Test
    public void test3() {
        //        AigcMix.enableVerboseLogger();
        async(() -> {
            return getKit().chatStream(
                                   getModel(),
                                   requestWithToolCall,
                                   chunk -> {
                                       getUnitTestLogger().info("chunk", chunk.cloneAsJsonObject());
                                       return Future.succeededFuture();
                                   },
                                   180_000L,
                                   UUID.randomUUID().toString()
                           )
                           .compose(v -> {
                               getUnitTestLogger().info("fin");
                               return Future.succeededFuture();
                           });
        });
    }

    @Test
    public void test4() {
        async(() -> {
            AtomicReference<DoubaoMessageInResponse> toolCallMessageRef = new AtomicReference<>();

            return getKit().chatStream(
                                   getModel(),
                                   requestWithToolCall,
                                   180_000L,
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

                               List<DoubaoToolCall> toolCalls = message.getToolCalls();
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
                               requestWithToolCall.addToolOutputChatMessage(toolCallOutputContent, msg.getToolCalls().get(0)
                                                                                          .getId());

                               return getKit().chatStream(
                                       getModel(),
                                       requestWithToolCall,
                                       180_000L,
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
