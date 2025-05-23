package io.github.sinri.AiOnHttpMix.test.unit.provider.dashscope.qwen.vision;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.message.QwenMessageInChatRequest;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.message.QwenMessageInResponse;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.sync.QwenResponseOutput;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.sync.QwenResponseOutputChoice;
import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolCall;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class QwenVisionStreamUnitTest extends AbstractQwenVisionModelUnitTest {

    @Test
    public void test1() {
        async(() -> {
            return getKit().chatStream(
                                   getModel(),
                                   requestWithoutToolCall,
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
//        AigcMix.enableVerboseLogger();
        async(() -> {
            return getKit().chatStream(
                                   getModel(),
                                   requestWithoutToolCall,
                                   chunk -> {
                                       AigcMix.getVerboseLogger().info("chunk", chunk.cloneAsJsonObject());
                                       try {
                                           QwenResponseOutput output = chunk.getOutput();
                                           List<QwenResponseOutputChoice> choices = output.getChoices();
                                           QwenResponseOutputChoice choice = choices.get(0);
                                           QwenMessageInResponse message = choice.getMessage();

                                           String reasoningContent = message.getReasoningContent();
                                           if (!Keel.stringHelper().isNullOrBlank(reasoningContent)) {
                                               getUnitTestLogger().info("reasoningContent: "+reasoningContent);
                                           }

                                           List<String> contents = message.getContents();
                                           contents.forEach(content->{
                                               getUnitTestLogger().info("content: "+content);
                                           });

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
            return getKit().chatStream(
                                   getModel(),
                                   requestWithoutToolCall,
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
                               List<String> contents = message.getContents();
                               contents.forEach(content->{
                                   getUnitTestLogger().info("content: "+content);
                               });

                               return Future.succeededFuture();
                           });
        });
    }

    @Test
    public void test4() {
        async(() -> {
            return getKit().chatStream(
                                   getModel(),
                                   requestWithToolCall,
                                   180_000L,
                                   UUID.randomUUID().toString()
                           )
                           .compose(response -> {
                               getUnitTestLogger().info("response", response.cloneAsJsonObject());

                               QwenMessageInResponse message = response.getOutput().getChoices().get(0)
                                                                       .getMessage();
                               getUnitTestLogger().info("role: " + message.getRole());
                               getUnitTestLogger().info("reasoning_content: " + message.getReasoningContent());
                               List<String> contents = message.getContents();
                               contents.forEach(content->{
                                   getUnitTestLogger().info("content: "+content);
                               });

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
            AtomicReference<QwenMessageInResponse> toolCallMessageRef = new AtomicReference<>();
            AtomicReference<String> toolCallIdRef = new AtomicReference<>();

            return getKit().chatStream(
                                   getModel(),
                                   requestWithToolCall,
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

                               List<String> contents = message.getContents();
                               contents.forEach(content->{
                                   getUnitTestLogger().info("content: "+content);
                               });

                               List<ToolCall> toolCalls = message.getToolCalls();
                               Assert.assertFalse(toolCalls.isEmpty());
                               ToolCall toolCall = toolCalls.get(0);

                               String toolCallId = toolCall.getId();
                               toolCallIdRef.set(toolCallId);

                               FunctionToolCall function = toolCall.getFunction();

                               Assert.assertEquals("query_current_weather", function.getName());
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
                               requestWithToolCall.input(x -> x
                                       .addMessage(toolCallMessageRef.get())
                                       .addMessage(QwenMessageInChatRequest.createAsToolOutputInRequest(
                                               answer, toolCallIdRef.get()
                                       ))
                               );

                               return getKit().chatStream(
                                       getModel(),
                                       requestWithToolCall,
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

                               List<String> contents = message.getContents();
                               contents.forEach(content->{
                                   getUnitTestLogger().info("content: "+content);
                               });

                               return Future.succeededFuture();
                           });
        });
    }
}
