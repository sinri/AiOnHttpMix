package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.GPTMessageInRequest;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.GPTMessageInResponse;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.request.GPTRequest;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.sync.GPTResponseChoice;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.tool.GPTFunctionToolDefinition;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.tool.GPTToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolCall;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.common.dsl.Schemas;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class GptStreamUnitTest extends AbstractGptUnitTest {
    @Test
    public void test1() {
        async(() -> {
            GPTRequest request = GPTRequest
                    .create()
                    .addMessage(GPTMessageInRequest.createAsSystem("你是日本战国史学家", null))
                    .addMessage(GPTMessageInRequest.createAsUser("筑波山周边是哪些大名的势力范围", null))
                    .stream(true);
            return getKit()
                    .chatStream(
                            getServiceAdapter(),
                            getModel(),
                            request.toJsonObject(),
                            fragment -> {
                                getUnitTestLogger().info("fragment: " + fragment);
                                return Future.succeededFuture();
                            },
                            180_000L,
                            UUID.randomUUID().toString())
                    .compose(v -> {
                        getUnitTestLogger().info("fin");
                        return Future.succeededFuture();
                    });
        });
    }

    @Test
    public void test2() {
        async(() -> {
            GPTRequest request = GPTRequest
                    .create()
                    .addMessage(GPTMessageInRequest.createAsSystem("你是日本战国史学家", null))
                    .addMessage(GPTMessageInRequest.createAsUser("筑波山周边是哪些大名的势力范围", null))
                    .stream(true);
            return getKit()
                    .chatStream(
                            getServiceAdapter(),
                            getModel(),
                            request,
                            chunk -> {
                                getUnitTestLogger().info("chunnk", chunk.cloneAsJsonObject());
                                return Future.succeededFuture();
                            },
                            180_000L,
                            UUID.randomUUID().toString())
                    .compose(v -> {
                        getUnitTestLogger().info("fin");
                        return Future.succeededFuture();
                    });
        });
    }

    @Test
    public void test3() {
        async(() -> {
            GPTRequest request = GPTRequest
                    .create()
                    .addSystemMessage("你是一个王心凌铁粉")
                    .addUserMessage("王心凌的2025年下半年演唱会是什么时候")
                    .addTool(x -> x
                            .type("function")
                            .function(new GPTFunctionToolDefinition()
                                    .name("query_event_schedule")
                                    .description("查询演艺活动日程")
                                    .parameters(Schemas.objectSchema()
                                                       .property("name", Schemas.stringSchema())
                                                       .property("date_range_start", Schemas.stringSchema())
                                                       .property("date_range_end", Schemas.stringSchema())
                                                       .toJson()
                                    ))
                    );

            return getKit().chatStream(
                                   getServiceAdapter(),
                                   getModel(),
                                   request,
                                   chunk -> {
                                       getUnitTestLogger().info("chunk", chunk.cloneAsJsonObject());
                                       return Future.succeededFuture();
                                   },
                                   180_000L,
                                   UUID.randomUUID().toString()
                           )
                           .compose(v -> {
                               getUnitTestLogger().info("Fin");
                               return Future.succeededFuture();
                           });
        });
    }

    @Test
    public void test4() {
        async(() -> {
            GPTRequest request = GPTRequest
                    .create()
                    .addSystemMessage("你是一个王心凌铁粉")
                    .addUserMessage("王心凌的2025年下半年演唱会是什么时候")
                    .addTool(x -> x
                            .type("function")
                            .function(new GPTFunctionToolDefinition()
                                    .name("query_event_schedule")
                                    .description("查询演艺活动日程")
                                    .parameters(Schemas.objectSchema()
                                                       .property("name", Schemas.stringSchema())
                                                       .property("date_range_start", Schemas.stringSchema())
                                                       .property("date_range_end", Schemas.stringSchema())
                                                       .toJson()
                                    ))
                    );
            AtomicReference<GPTMessageInResponse> msgRef = new AtomicReference<>();
            AtomicReference<String> toolCallIdRef = new AtomicReference<>();
            return getKit()
                    .chatStream(
                            getServiceAdapter(),
                            getModel(),
                            request,
                            180_000L,
                            UUID.randomUUID().toString()
                    )
                    .compose(resp -> {
                        getUnitTestLogger().info("resp", resp.cloneAsJsonObject());

                        GPTResponseChoice choice = resp.getChoices().get(0);
                        GPTMessageInResponse message = choice.getMessage();

                        msgRef.set(message);

                        String role = message.getRole();
                        String content = message.getContent();

                        getUnitTestLogger().info("role " + role + " saith: " + content);

                        List<GPTToolCall> toolCalls = message.getToolCalls();
                        Assert.assertFalse(toolCalls.isEmpty());
                        GPTToolCall toolCall = toolCalls.get(0);
                        toolCallIdRef.set(toolCall.getId());
                        FunctionToolCall functionCall = toolCall.getFunction();
                        String name = functionCall.getName();
                        String arguments = functionCall.getArguments();
                        getUnitTestLogger().info("function " + name + "(" + arguments + ")");

                        return Future.succeededFuture(new JsonArray()
                                .add(new JsonObject()
                                        .put("date", "2025-08-12")
                                        .put("name", "王心凌")
                                        .put("place", "青岛市新城艺术中心")
                                        .put("event", "王心凌“低糖理念”巡回演唱会青岛站")
                                )
                                .add(new JsonObject()
                                        .put("date", "2025-11-11")
                                        .put("name", "王心凌×山口百惠")
                                        .put("place", "东京八王子市役所")
                                        .put("event", "双十一演唱会")
                                )
                                .toString()
                        );
                    })
                    .compose(toolCallOutput -> {
                        request.addToolCallMessage(msgRef.get());
                        request.addToolOutputMessage(toolCallOutput, toolCallIdRef.get());

                        return getKit()
                                .chatStream(
                                        getServiceAdapter(),
                                        getModel(),
                                        request,
                                        180_000L,
                                        UUID.randomUUID().toString()
                                );
                    })
                    .compose(resp -> {
                        getUnitTestLogger().info("resp", resp.cloneAsJsonObject());

                        GPTResponseChoice choice = resp.getChoices().get(0);
                        GPTMessageInResponse message = choice.getMessage();

                        msgRef.set(message);

                        String role = message.getRole();
                        String content = message.getContent();

                        getUnitTestLogger().info("role " + role + " saith: " + content);
                        return Future.succeededFuture();
                    });
        });
    }
}
