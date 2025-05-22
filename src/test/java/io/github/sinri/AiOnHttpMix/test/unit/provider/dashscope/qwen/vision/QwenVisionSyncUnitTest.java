package io.github.sinri.AiOnHttpMix.test.unit.provider.dashscope.qwen.vision;

import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.QwenKit;
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

/**
 * 基于{@link QwenKit}，测试同步LLM调用。
 */
public class QwenVisionSyncUnitTest extends AbstractQwenVisionModelUnitTest {
    /**
     * 测试常规单轮对话。
     */
    @Test
    public void test1() {
        async(() -> {
            return getKit().chat(
                                   getServiceAdapter(),
                                   getModel(),
                                   requestWithoutToolCall,
                                   UUID.randomUUID().toString()
                           )
                           .compose(resp -> {
                               getUnitTestLogger().info("resp", resp.cloneAsJsonObject());

                               QwenResponseOutput output = resp.getOutput();
                               List<QwenResponseOutputChoice> choices = output.getChoices();
                               QwenResponseOutputChoice choice = choices.get(0);
                               QwenMessageInResponse message = choice.getMessage();
                               var contents = message.getContents();
                               contents.forEach(content -> {
                                   getUnitTestLogger().info("content[]: " + content);
                               });


                               return Future.succeededFuture();
                           });
        });
    }

    /**
     * 测试工具（函数）调用，并以调用结果追加一轮对话。
     */
    @Test
    public void test2() {
        async(() -> {
            AtomicReference<QwenMessageInResponse> toolCallMessageRef = new AtomicReference<>();
            AtomicReference<String> toolCallIdRef = new AtomicReference<>();

            return getKit().chat(
                                   getServiceAdapter(),
                                   getModel(),
                                   requestWithToolCall,
                                   UUID.randomUUID().toString()
                           )
                           .compose(resp -> {
                               getUnitTestLogger().info("resp", resp.cloneAsJsonObject());

                               QwenResponseOutput output = resp.getOutput();
                               List<QwenResponseOutputChoice> choices = output.getChoices();
                               QwenResponseOutputChoice choice = choices.get(0);
                               QwenMessageInResponse message = choice.getMessage();

                               // for later
                               toolCallMessageRef.set(message);

                               var contents = message.getContents();
                               contents.forEach(content -> {
                                   getUnitTestLogger().info("content[]: " + content);
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

                               return getKit().chat(
                                       getServiceAdapter(),
                                       getModel(),
                                       requestWithToolCall,
                                       UUID.randomUUID().toString()
                               );
                           })
                           .compose(resp -> {
                               getUnitTestLogger().info("resp", resp.cloneAsJsonObject());

                               QwenResponseOutput output = resp.getOutput();
                               List<QwenResponseOutputChoice> choices = output.getChoices();
                               QwenResponseOutputChoice choice = choices.get(0);
                               QwenMessageInResponse message = choice.getMessage();

                               var contents = message.getContents();
                               contents.forEach(content -> {
                                   getUnitTestLogger().info("content[]: " + content);
                               });

                               return Future.succeededFuture();
                           });
        });
    }
}
