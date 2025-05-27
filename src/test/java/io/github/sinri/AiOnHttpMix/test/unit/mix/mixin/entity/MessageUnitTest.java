package io.github.sinri.AiOnHttpMix.test.unit.mix.mixin.entity;

import io.github.sinri.AiOnHttpMix.mix.chat.message.MixChatMessage;
import io.github.sinri.AiOnHttpMix.mix.chat.message.MixChatVisionContentElement;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonFunctionToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonToolCall;
import io.github.sinri.keel.facade.tesuto.unit.KeelUnitTest;
import org.junit.Test;

import java.util.List;

public class MessageUnitTest extends KeelUnitTest {
    @Test
    public void testTextChat() {
        getUnitTestLogger().info("basic text chat request", MixChatMessage.create()
                                                                          .setRole("user")
                                                                          .setTextContent("PROMPT")
                                                                          .toJsonObject());

        getUnitTestLogger().info("replied tool call response", MixChatMessage.create()
                                                                             .setRole("assistant")
                                                                             .setToolCalls(List.of(
                                                                                     new CommonToolCall("TOOL_CALL_ID", 0, new CommonFunctionToolCall("FUNCTION_NAME", "FUNCTION_ARGUMENTS"))
                                                                             ))
                                                                             .toJsonObject());

        getUnitTestLogger().info("with tool call output to request", MixChatMessage.create()
                                                                                   .setRole("tool")
                                                                                   .setTextContent("TOOL_CALL_OUTPUT")
                                                                                   .setToolCallId("TOOL_CALL_ID")
                                                                                   .toJsonObject()
        );

        getUnitTestLogger().info("basic text chat response", MixChatMessage.create()
                                                                           .setRole("assistant")
                                                                           .setTextContent("RESPONSE")
                                                                           .toJsonObject());

        getUnitTestLogger().info("thought text chat response", MixChatMessage.create()
                                                                             .setRole("assistant")
                                                                             .setReasoningContent("REASONING_CONTENT")
                                                                             .setTextContent("RESPONSE")
                                                                             .toJsonObject());
    }

    @Test
    public void testVisionChat() {
        getUnitTestLogger().info("basic vision chat request", MixChatMessage.create()
                                                                            .setRole("user")
                                                                            .setVisionContent(List.of(
                                                                                    MixChatVisionContentElement.create()
                                                                                                               .setText("TEXT_PROMPT"),
                                                                                    MixChatVisionContentElement.create()
                                                                                                               .setImage("IMAGE_PROMPT")
                                                                            ))
                                                                            .toJsonObject());

        getUnitTestLogger().info("replied tool call response", MixChatMessage.create()
                                                                             .setRole("assistant")
                                                                             .setToolCalls(List.of(
                                                                                     new CommonToolCall("TOOL_CALL_ID", 0, new CommonFunctionToolCall("FUNCTION_NAME", "FUNCTION_ARGUMENTS"))
                                                                             ))
                                                                             .toJsonObject());

        getUnitTestLogger().info("with tool call output to request", MixChatMessage.create()
                                                                                   .setRole("tool")
                                                                                   .setVisionContent(List.of(
                                                                                           MixChatVisionContentElement.create()
                                                                                                                      .setText("TEXT_PROMPT")
                                                                                   ))
                                                                                   .setToolCallId("TOOL_CALL_ID")
                                                                                   .toJsonObject()
        );

        getUnitTestLogger().info("basic vision chat response", MixChatMessage.create()
                                                                             .setRole("assistant")
                                                                             .setTextContent("RESPONSE")
                                                                             .toJsonObject());

        getUnitTestLogger().info("thought vision chat response", MixChatMessage.create()
                                                                               .setRole("assistant")
                                                                               .setReasoningContent("REASONING_CONTENT")
                                                                               .setTextContent("RESPONSE")
                                                                               .toJsonObject());
    }
}
