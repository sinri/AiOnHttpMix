package io.github.sinri.AiOnHttpMix.test.unit.provider.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.message.QwenMessageInRequest;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.message.QwenMessageInResponse;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.response.sync.QwenResponseOutput;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.response.sync.QwenResponseOutputChoice;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.tool.QwenToolDefinition;
import io.github.sinri.AiOnHttpMix.utils.models.dashscope.qwen.QwenModelSeries;
import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolCall;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.common.dsl.Schemas;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class QwenStreamUnitTest extends AbstractQwenKitUnitTest {

    @Test
    public void test1() {
        async(() -> {

            QwenRequest request = QwenRequest.create()
                                             .input(x -> x
                                                     .addMessage(QwenMessageInRequest.createAsSystemInRequest(
                                                             "你需要根据用户的输入作答，并将答案包装为一个 json object 输出，格式为 `{\"answer\":\"\"}`。"
                                                     ))
                                                     .addMessage(QwenMessageInRequest.createAsUserInRequest(
                                                             "2025年5月3日，杭州的天气是什么"
                                                     ))
                                             )
                                             .parameters(x -> x
                                                     .stream(true)
                                                     .incrementalOutput(true)
                                                     .addTool(new QwenToolDefinition(f -> f
                                                             .name("query_weather")
                                                             .parameters(Schemas.objectSchema()
                                                                                .property("date", Schemas.stringSchema())
                                                                                .property("place", Schemas.stringSchema())
                                                                                .toJson()
                                                             )
                                                     ))
                                             );
            getUnitTestLogger().info("req", request.toJsonObject());
            return getQwenKit().chatStream(
                                       getServiceAdapter(),
                                       qwenPlusLatest,
                                       request.toJsonObject(),
                                       chunk -> {
                                           getUnitTestLogger().info("chunk: " + chunk);
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
        //AigcMix.enableVerboseLogger();
        async(() -> {
            QwenRequest request = QwenRequest.create()
                                             .input(x -> x
                                                     .addMessage(
                                                             QwenMessageInRequest.createAsUserInRequest("歼10CE和阵风哪个厉害啊")
                                                     ))
                                             .parameters(x -> x
                                                     .enableThinking(true)
                                                     .resultFormat("message")
                                                     .stream(true)
                                                     .incrementalOutput(true)
                                                     .enableSearch(true)
                                             );
            getUnitTestLogger().info("req", request.toJsonObject());
            return getQwenKit().chatStream(
                                       getServiceAdapter(),
                                       qwenPlusLatest,
                                       request,
                                       chunk -> {
                                           AigcMix.getVerboseLogger().info("chunk", chunk.cloneAsJsonObject());
                                           try {
                                               QwenResponseOutput output = chunk.getOutput();
                                               List<QwenResponseOutputChoice> choices = output.getChoices();
                                               QwenResponseOutputChoice choice = choices.get(0);
                                               QwenMessageInResponse message = choice.getMessage();

                                               String reasoningContent = message.getReasoningContent();
                                               if (!Keel.stringHelper().isNullOrBlank(reasoningContent)) {
                                                   System.err.print(reasoningContent);
                                               }

                                               String content = message.getContent();
                                               if (!Keel.stringHelper().isNullOrBlank(content)) {
                                                   System.out.print(content);
                                               }

                                               return Future.succeededFuture();
                                           } catch (Throwable throwable) {
                                               getUnitTestLogger().exception(throwable);
                                               return Future.failedFuture(throwable);
                                           }
                                       },
                                       180_000L,
                                       UUID.randomUUID().toString()
                               )
                               .compose(v -> {
                                   System.out.println();
                                   getUnitTestLogger().info("fin");
                                   return Future.succeededFuture();
                               });
        });
    }

    @Test
    public void test3() {
        AigcMix.enableVerboseLogger();
        async(() -> {
            QwenRequest request = QwenRequest.create()
                                             .input(x -> x
                                                     .addMessage(
                                                             QwenMessageInRequest.createAsUserInRequest("歼10CE和阵风哪个厉害啊")
                                                     ))
                                             .parameters(x -> x
                                                     .enableThinking(true)
                                                     .resultFormat("message")
                                                     .stream(true)
                                                     .incrementalOutput(true)
                                                     .enableSearch(true)
                                             );
            getUnitTestLogger().info("req", request.toJsonObject());
            return getQwenKit().chatStream(
                                       getServiceAdapter(),
                                       qwenPlusLatest,
                                       request,
                                       180_000L,
                                       UUID.randomUUID().toString()
                               )
                               .compose(qwenResponse -> {
                                   getUnitTestLogger().info("buffered response", qwenResponse.cloneAsJsonObject());

                                   QwenResponseOutput output = qwenResponse.getOutput();
                                   List<QwenResponseOutputChoice> choices = output.getChoices();
                                   QwenResponseOutputChoice choice = choices.get(0);
                                   QwenMessageInResponse message = choice.getMessage();
                                   getUnitTestLogger().info("role: " + message.getRole());
                                   getUnitTestLogger().info("reasoning_content: " + message.getReasoningContent());
                                   getUnitTestLogger().info("content: " + message.getContent());

                                   return Future.succeededFuture();
                               });
        });
    }

    @Test
    public void test4() {
        async(() -> {
            QwenRequest request = QwenRequest.create()
                                             .input(x -> x
                                                     .addMessage(QwenMessageInRequest.createAsSystemInRequest(
                                                             "你需要根据用户的输入作答，并将答案包装为一个 json object 输出，格式为 `{\"answer\":\"\"}`。"
                                                     ))
                                                     .addMessage(QwenMessageInRequest.createAsUserInRequest(
                                                             "2025年5月3日，杭州的天气是什么"
                                                     ))
                                             )
                                             .parameters(x -> x
                                                     .stream(true)
                                                     .incrementalOutput(true)
                                                     .addTool(new QwenToolDefinition(f -> f
                                                             .name("query_weather")
                                                             .parameters(Schemas.objectSchema()
                                                                                .property("date", Schemas.stringSchema())
                                                                                .property("place", Schemas.stringSchema())
                                                                                .toJson()
                                                             )
                                                     ))
                                             );
            getUnitTestLogger().info("req", request.toJsonObject());
            return getQwenKit().chatStream(
                                       getServiceAdapter(),
                                       qwenPlusLatest,
                                       request,
                                       180_000L,
                                       UUID.randomUUID().toString()
                               )
                               .compose(response -> {
                                   getUnitTestLogger().info("response", response.cloneAsJsonObject());

                                   QwenMessageInResponse message = response.getOutput().getChoices().get(0)
                                                                           .getMessage();
                                   getUnitTestLogger().info("role: " + message.getRole());
                                   getUnitTestLogger().info("reasoning_content: " + message.getReasoningContent());
                                   getUnitTestLogger().info("content: " + message.getContent());

                                   for (ToolCall toolCall : message.getToolCalls()) {
                                       getUnitTestLogger().info("toolCall[" + toolCall.getIndex() + "][" + toolCall.getType() + "] "
                                               + toolCall.getFunction().getName()
                                               + "(" + toolCall.getFunction().getArguments() + ")");
                                   }
                                   return Future.succeededFuture();
                               });
        });
    }

    /**
     * 测试工具（函数）调用，并以调用结果追加一轮对话。
     */
    @Test
    public void test5() {
        //        AigcMix.enableVerboseLogger();
        async(() -> {
            QwenRequest request = QwenRequest
                    .create()
                    .input(x -> x
                            .addSystemMessage("你需要查询相关情报工具，解答用户的问题，注意需要将答案包装为一个 json object 输出，格式为 `{\"answer\":\"\"}`。")
                            .addUserMessage("2025年5月3日，杭州的天气是什么")
                    )
                    .parameters(x -> x
                            .stream(true)
                            .incrementalOutput(true)
                            .resultFormat("message")
                            .addTool(new QwenToolDefinition(f -> f
                                    .name("query_weather")
                                    .description("查询指定地区在某一天的天气记录或天气预报")
                                    .parameters(Schemas.objectSchema()
                                                       .property("date", Schemas.stringSchema())
                                                       .property("place", Schemas.stringSchema())
                                                       .toJson()
                                    )
                            ))
                    );
            getUnitTestLogger().info("round-1-req", request.toJsonObject());

            AtomicReference<QwenMessageInResponse> toolCallMessageRef = new AtomicReference<>();
            AtomicReference<String> toolCallIdRef = new AtomicReference<>();

            return getQwenKit().chatStream(
                                       getServiceAdapter(),
                                       qwenPlus,
                                       request,
                                       180_000L,
                                       UUID.randomUUID().toString()
                               )
                               .compose(resp -> {
                                   getUnitTestLogger().info("round-1-resp", resp.cloneAsJsonObject());

                                   QwenResponseOutput output = resp.getOutput();
                                   List<QwenResponseOutputChoice> choices = output.getChoices();
                                   QwenResponseOutputChoice choice = choices.get(0);
                                   QwenMessageInResponse message = choice.getMessage();

                                   // for later
                                   toolCallMessageRef.set(message);

                                   String content = message.getContent();
                                   getUnitTestLogger().info("content: " + content);

                                   List<ToolCall> toolCalls = message.getToolCalls();
                                   Assert.assertFalse(toolCalls.isEmpty());
                                   ToolCall toolCall = toolCalls.get(0);

                                   String toolCallId = toolCall.getId();
                                   toolCallIdRef.set(toolCallId);

                                   FunctionToolCall function = toolCall.getFunction();

                                   Assert.assertEquals("query_weather", function.getName());
                                   var arg = new JsonObject(function.getArguments());
                                   getUnitTestLogger().info("to call function " + function.getName() + "(" + arg + ")...");
                                   return Future.succeededFuture(arg);
                               })
                               .compose(arg -> {
                                   String date = arg.getString("date");
                                   String place = arg.getString("place");
                                   return Future.succeededFuture(place + "在" + date + "的天气为晴天，偶有西风，温度30到34摄氏度。");
                               })
                               .compose(answer -> {
                                   request.input(x -> x
                                           .addMessage(toolCallMessageRef.get())
                                           .addMessage(QwenMessageInRequest.createAsToolOutputInRequest(
                                                   answer, toolCallIdRef.get()
                                           ))
                                   );

                                   getUnitTestLogger().info("round-2-req", request.toJsonObject());

                                   return getQwenKit().chatStream(
                                           getServiceAdapter(),
                                           qwenPlus,
                                           request,
                                           180_000L,
                                           UUID.randomUUID().toString()
                                   );
                               })
                               .compose(resp -> {
                                   getUnitTestLogger().info("round-2-resp", resp.cloneAsJsonObject());

                                   QwenResponseOutput output = resp.getOutput();
                                   List<QwenResponseOutputChoice> choices = output.getChoices();
                                   QwenResponseOutputChoice choice = choices.get(0);
                                   QwenMessageInResponse message = choice.getMessage();

                                   String content = message.getContent();
                                   getUnitTestLogger().info("content: " + content);

                                   return Future.succeededFuture();
                               });
        });
    }
}
