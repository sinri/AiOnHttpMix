package io.github.sinri.AiOnHttpMix.mix.chat.response;

import io.github.sinri.AiOnHttpMix.mix.chat.message.MixChatMessage;
import io.github.sinri.AiOnHttpMix.mix.chat.message.MixChatVisionContentElement;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.GPTMessageInResponse;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.sync.GPTResponse;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.sync.GPTResponseChoice;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.message.QwenMessageInResponse;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.sync.QwenResponse;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.sync.QwenResponseOutputChoice;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.DoubaoMessageInResponse;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.sync.DoubaoResponse;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.sync.DoubaoResponseChoice;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonToolCall;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public interface MixChatResponse extends UnmodifiableJsonifiableEntity {

    static MixChatResponse wrap(JsonObject jsonObject) {
        return new MixChatResponseImpl(jsonObject);
    }

    @Deprecated
    private static <T extends ToolCall> void handleToolCalls(List<T> toolCalls, JsonObject j) {
        if (!toolCalls.isEmpty()) {
            JsonArray array = new JsonArray();
            toolCalls.forEach(toolCall -> array.add(new JsonObject()
                    .put("id", toolCall.getId())
                    .put("type", toolCall.getType())
                    .put("function",
                            toolCall.getFunction() == null ? null
                                    : new JsonObject()
                                    .put("name", toolCall.getFunction().getName())
                                    .put("arguments", toolCall.getFunction().getArguments())
                    )
            ));
            j.put("tool_calls", array);
        }
    }

    private static <T extends ToolCall> void handleToolCalls(List<T> toolCalls, MixChatMessage mixChatMessage) {
        if (!toolCalls.isEmpty()) {
            List<ToolCall> list = new ArrayList<>();

            toolCalls.forEach(tc -> {
                CommonToolCall commonToolCall = new CommonToolCall(tc.getId(), tc.getIndex(), tc.getFunction());
                list.add(commonToolCall);
            });

            mixChatMessage.setToolCalls(list);
        }
    }

    private static MixChatResponse build(MixChatMessage message) {
        return new MixChatResponseImpl(new JsonObject()
                .put(MixChatResponseImpl.KEY_MESSAGE, message.cloneAsJsonObject())
        );
    }

    static MixChatResponse from(GPTResponse resp) {
        List<GPTResponseChoice> choices = resp.getChoices();
        GPTResponseChoice choice = choices.get(0);
        GPTMessageInResponse message = choice.getMessage();

        MixChatMessage mixChatMessage = MixChatMessage.create();
        mixChatMessage.setRole(message.getRole());
        String content = message.getContent();
        if (content != null) {
            mixChatMessage.setTextContent(content);
        }
        List<CommonToolCall> toolCalls = message.getToolCalls();
        handleToolCalls(toolCalls, mixChatMessage);

        return MixChatResponse.build(mixChatMessage);
    }

    static MixChatResponse from(DoubaoResponse resp) {
        List<DoubaoResponseChoice> choices = resp.getChoices();
        DoubaoResponseChoice choice = choices.get(0);
        DoubaoMessageInResponse message = choice.getMessage();

        MixChatMessage mixChatMessage = MixChatMessage.create();
        mixChatMessage.setRole(message.getRole());
        String content = message.getContent();
        if (content != null) {
            mixChatMessage.setTextContent(content);
        }
        List<ToolCall> toolCalls = message.getToolCalls();
        handleToolCalls(toolCalls, mixChatMessage);

        return MixChatResponse.build(mixChatMessage);
    }

    static MixChatResponse from(QwenResponse resp) {
        List<QwenResponseOutputChoice> choices = resp.getOutput().getChoices();
        QwenResponseOutputChoice choice = choices.get(0);
        QwenMessageInResponse message = choice.getMessage();

        MixChatMessage mixChatMessage = MixChatMessage.create();
        mixChatMessage.setRole(message.getRole());
        List<String> contents = message.getContents();
        if (!contents.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (var c : contents) {
                MixChatVisionContentElement e = MixChatVisionContentElement.create()
                                                                           .setText(c);
                String text = e.getText();
                sb.append(text);
            }
            mixChatMessage.setTextContent(sb.toString());
        } else {
            String content = message.getContent();
            if (content != null) {
                mixChatMessage.setTextContent(content);
            }
        }

        List<ToolCall> toolCalls = message.getToolCalls();
        handleToolCalls(toolCalls, mixChatMessage);

        return MixChatResponse.build(mixChatMessage);
    }

    MixChatMessage getMessage();
}
