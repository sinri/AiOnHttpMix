package io.github.sinri.AiOnHttpMix.test.unit.mix.withNative.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.mix.chat.MixChatMessage;
import io.github.sinri.AiOnHttpMix.mix.chat.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum;
import io.github.sinri.AiOnHttpMix.mix.tools.MixFunctionToolDefinition;
import io.github.sinri.AiOnHttpMix.mix.tools.MixToolDefinition;
import io.github.sinri.AiOnHttpMix.test.unit.mix.withNative.AbstractMixNativeUnitTest;
import io.github.sinri.AiOnHttpMix.utils.tools.FunctionParameterDefinition;
import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolCall;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.common.dsl.SchemaType;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MixNativeGPTSyncFCUnitTest extends AbstractMixNativeUnitTest {
    private Future<Void> toTestSync(SupportedModelEnum supportedModelEnum) {
        AigcMix.enableVerboseLogger();

        AtomicReference<ToolCall> toolCallRef = new AtomicReference<>();
        return Future.succeededFuture()
                     .compose(round1 -> {
                         return getMixChatKit().chat(MixChatRequest.create()
                                                                   .setSupportedModelEnum(supportedModelEnum)
                                                                   .addMessage(MixChatMessage.create()
                                                                                             .setRole("user")
                                                                                             .setContent("2010年8月1日柯桥有没有下雨")
                                                                   )
                                                                   .addTool(new MixToolDefinition(new MixFunctionToolDefinition()
                                                                           .name("query_weather")
                                                                           .description("查询指定地点在特定日期的天气情况")
                                                                           .parameters(List.of(
                                                                                   new FunctionParameterDefinition(SchemaType.STRING, "place", "地点，如城市名称"),
                                                                                   new FunctionParameterDefinition(SchemaType.STRING, "date", "日期")
                                                                           ))
                                                                   ))
                         );
                     })
                     .compose(resp1 -> {
                         getUnitTestLogger().info("resp 1", resp1.cloneAsJsonObject());
                         MixChatMessage message = resp1.getMessage();

                         List<ToolCall> toolCalls = message.getToolCalls();
                         ToolCall toolCall = toolCalls.get(0);

                         toolCallRef.set(toolCall);

                         FunctionToolCall function = toolCall.getFunction();
                         String functionName = function.getName();
                         Assert.assertEquals("query_weather", functionName);
                         String arguments = function.getArguments();
                         UnmodifiableJsonifiableEntity wrapped = UnmodifiableJsonifiableEntity.wrap(new JsonObject(arguments));
                         String place = wrapped.readString("place");
                         String date = wrapped.readString("date");
                         return Future.succeededFuture(new JsonObject()
                                 .put("place", place)
                                 .put("date", date)
                                 .put("weather", "天气晴朗，8级西风，气温38到45摄氏度")
                                 .toString()
                         );
                     })
                     .compose(toolCallOutput -> {
                         ToolCall toolCall = toolCallRef.get();
                         return getMixChatKit().chat(MixChatRequest.create()
                                                                   .setSupportedModelEnum(supportedModelEnum)
                                                                   .addMessage(MixChatMessage.create()
                                                                                             .setRole("user")
                                                                                             .setContent("2010年8月1日柯桥有没有下雨")
                                                                   )
                                                                   .addMessage(MixChatMessage.create()
                                                                                             .setRole("assistant")
                                                                                             .setToolCalls(List.of(toolCall))
                                                                   )
                                                                   .addMessage(MixChatMessage.create()
                                                                                             .setRole("tool")
                                                                                             .setContent(toolCallOutput)
                                                                                             .setToolCallId(toolCall.getId())
                                                                   )
                         );
                     })
                     .compose(resp2 -> {
                         getUnitTestLogger().info("resp 2", resp2.cloneAsJsonObject());
                         MixChatMessage message = resp2.getMessage();
                         String role = message.getRole();
                         String content = message.getContent();
                         //                         String reasoningContent = message.getReasoningContent();

                         getUnitTestLogger().info("role: " + role + "\ncontent: " + content);
                         return Future.succeededFuture();
                     });
    }

    @Test
    public void testSyncSimpleForGPT() {
        async(() -> toTestSync(SupportedModelEnum.ChatGPT));
    }

    @Test
    public void testSyncSimpleForDoubao() {
        async(() -> toTestSync(SupportedModelEnum.Doubao));
    }

    @Test
    public void testSyncSimpleForVolcesDeepSeek() {
        async(() -> toTestSync(SupportedModelEnum.DeepSeekChatOnVolces));
    }

    @Test
    public void testSyncSimpleForVolcesMoonshot() {
        async(() -> toTestSync(SupportedModelEnum.KimiOnVolces));
    }

    @Test
    public void testSyncSimpleForQwen() {

        async(() -> toTestSync(SupportedModelEnum.QwenPlusLatest));
    }
}
