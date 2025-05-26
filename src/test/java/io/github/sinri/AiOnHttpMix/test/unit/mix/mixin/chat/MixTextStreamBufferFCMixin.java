package io.github.sinri.AiOnHttpMix.test.unit.mix.mixin.chat;

import io.github.sinri.AiOnHttpMix.mix.chat.message.MixChatMessage;
import io.github.sinri.AiOnHttpMix.mix.chat.text.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum;
import io.github.sinri.AiOnHttpMix.test.unit.mix.mixin.MixCoreMixin;
import io.github.sinri.AiOnHttpMix.utils.tools.FunctionParameterDefinition;
import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonFunctionToolDefinition;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonToolDefinition;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.common.dsl.SchemaType;
import org.junit.Assert;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public interface MixTextStreamBufferFCMixin extends MixCoreMixin {
    default Future<Void> toTestStreamBuffer(SupportedModelEnum supportedModelEnum) {
        AtomicReference<ToolCall> toolCallRef = new AtomicReference<>();
        return Future.succeededFuture()
                     .compose(round1 -> {
                         return getMixChatKit().chatStream(MixChatRequest
                                 .create()
                                 .setSupportedModelEnum(supportedModelEnum)
                                 .addMessage(MixChatMessage.create()
                                                           .setRole("user")
                                                           .setTextContent("2010年8月1日柯桥有没有下雨")
                                 )
                                 .addTool(new CommonToolDefinition(new CommonFunctionToolDefinition(
                                         "query_weather",
                                         "查询指定地点在特定日期的天气情况",
                                         List.of(
                                                 new FunctionParameterDefinition(SchemaType.STRING, "place", "地点，如城市名称"),
                                                 new FunctionParameterDefinition(SchemaType.STRING, "date", "日期")
                                         )
                                 )))
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
                         return getMixChatKit().chatStream(MixChatRequest
                                 .create()
                                 .setSupportedModelEnum(supportedModelEnum)
                                 .addMessage(MixChatMessage.create()
                                                           .setRole("user")
                                                           .setTextContent("2010年8月1日柯桥有没有下雨")
                                 )
                                 .addMessage(MixChatMessage.create()
                                                           .setRole("assistant")
                                                           .setToolCalls(List.of(toolCall))
                                 )
                                 .addMessage(MixChatMessage.create()
                                                           .setRole("tool")
                                                           .setTextContent(toolCallOutput)
                                                           .setToolCallId(toolCall.getId())
                                 )
                         );
                     })
                     .compose(resp2 -> {
                         getUnitTestLogger().info("resp 2", resp2.cloneAsJsonObject());
                         MixChatMessage message = resp2.getMessage();
                         String role = message.getRole();
                         String content = message.getTextContent();
                         //                         String reasoningContent = message.getReasoningContent();

                         getUnitTestLogger().info("role: " + role + "\ncontent: " + content);
                         return Future.succeededFuture();
                     });
    }
}
