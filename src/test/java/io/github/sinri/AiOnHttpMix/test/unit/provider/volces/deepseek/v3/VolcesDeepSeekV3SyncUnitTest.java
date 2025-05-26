package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.deepseek.v3;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.DoubaoMessageInChatRequest;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.DoubaoMessageInResponse;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.request.DoubaoRequest;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.request.ThinkingOptions;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.sync.DoubaoResponseChoice;
import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonFunctionToolDefinition;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonToolDefinition;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.common.dsl.Schemas;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class VolcesDeepSeekV3SyncUnitTest extends AbstractVolcesDeepSeekV3ModelUnitTest {
    @Test
    public void test1() {
        AigcMix.enableVerboseLogger();
        async(() -> {
            DoubaoRequest request = DoubaoRequest.create()
                                                 .addMessage(DoubaoMessageInChatRequest.createAsSystemMessage("你是一个王心凌铁粉"))
                                                 .addMessage(DoubaoMessageInChatRequest.createAsUserMessage("王心凌的教育经历是什么"))
                                                 .thinking(ThinkingOptions.create()
                                                                          .type("enabled"));

            return getKit().chat(
                                   getModel(),
                                   request,
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
            DoubaoRequest request = DoubaoRequest.create()
                                                 .addSystemChatMessage("你是一个王心凌铁粉")
                                                 .addUserChatMessage("王心凌在2024年8月开过演唱会吗")
                                                 .addTool(new CommonToolDefinition(new CommonFunctionToolDefinition()
                                                         .name("query_event_schedule")
                                                         .description("查询演艺活动日程")
                                                         .parameters(Schemas.objectSchema()
                                                                            .property("name", Schemas.stringSchema()
                                                                            )
                                                                            .property("date_range_start", Schemas.stringSchema())
                                                                            .property("date_range_end", Schemas.stringSchema())
                                                                            .toJson()
                                                         )));

            AtomicReference<DoubaoMessageInResponse> toolCallMessageRef = new AtomicReference<>();

            return getKit().chat(
                                   getModel(),
                                   request,
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

                               Assert.assertEquals("query_event_schedule", function.getName());

                               return Future.succeededFuture(new JsonArray()
                                       .add(new JsonObject()
                                               .put("date", "2024-08-12")
                                               .put("name", "王心凌")
                                               .put("place", "青岛市新城艺术中心")
                                               .put("event", "王心凌“低糖理念”巡回演唱会青岛站")
                                       )
                                       .toString()
                               );
                           })
                           .compose(toolCallOutputContent -> {
                               DoubaoMessageInResponse msg = toolCallMessageRef.get();
                               request.addToolCallChatMessage(msg.getContent(), msg.getToolCalls());
                               request.addToolOutputChatMessage(toolCallOutputContent, msg.getToolCalls().get(0)
                                                                                          .getId());

                               return getKit().chat(
                                       getModel(),
                                       request,
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
