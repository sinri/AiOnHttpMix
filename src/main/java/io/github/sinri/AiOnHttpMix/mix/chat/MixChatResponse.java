package io.github.sinri.AiOnHttpMix.mix.chat;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.GPTMessageInResponse;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.sync.GPTResponse;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.sync.GPTResponseChoice;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.tool.GPTToolCall;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.message.QwenMessageInResponse;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.sync.QwenResponse;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.sync.QwenResponseOutputChoice;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.DoubaoMessageInResponse;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.sync.DoubaoResponse;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.sync.DoubaoResponseChoice;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface MixChatResponse extends UnmodifiableJsonifiableEntity {

    static MixChatResponse from(GPTResponse resp) {
        List<GPTResponseChoice> choices = resp.getChoices();
        GPTResponseChoice choice = choices.get(0);
        GPTMessageInResponse message = choice.getMessage();

        var j = new JsonObject();
        String role = message.getRole();
        j.put("role", role);
        String content = message.getContent();
        j.put("content", content);
        List<GPTToolCall> toolCalls = message.getToolCalls();
        if (!toolCalls.isEmpty()) {
            JsonArray array = new JsonArray();
            toolCalls.forEach(toolCall -> {
                array.add(new JsonObject()
                        .put("id", toolCall.getId())
                        .put("type", toolCall.getType())
                        .put("function",
                                toolCall.getFunction() == null ? null
                                        : new JsonObject()
                                        .put("name", toolCall.getFunction().getName())
                                        .put("arguments", toolCall.getFunction().getArguments())
                        )
                );
            });
            j.put("tool_calls", array);
        }
        return new MixChatResponseImpl(new JsonObject()
                .put("message", j)
        );
    }

    static MixChatResponse from(DoubaoResponse resp) {
        List<DoubaoResponseChoice> choices = resp.getChoices();
        DoubaoResponseChoice choice = choices.get(0);
        DoubaoMessageInResponse message = choice.getMessage();

        var j = new JsonObject();
        String role = message.getRole();
        j.put("role", role);
        String content = message.getContent();
        j.put("content", content);
        var toolCalls = message.getToolCalls();
        if (!toolCalls.isEmpty()) {
            JsonArray array = new JsonArray();
            toolCalls.forEach(toolCall -> {
                array.add(new JsonObject()
                        .put("id", toolCall.getId())
                        .put("type", toolCall.getType())
                        .put("function",
                                toolCall.getFunction() == null ? null
                                        : new JsonObject()
                                        .put("name", toolCall.getFunction().getName())
                                        .put("arguments", toolCall.getFunction().getArguments())
                        )
                );
            });
            j.put("tool_calls", array);
        }
        return new MixChatResponseImpl(new JsonObject()
                .put("message", j)
        );
    }

    static MixChatResponse from(QwenResponse resp) {
        List<QwenResponseOutputChoice> choices = resp.getOutput().getChoices();
        QwenResponseOutputChoice choice = choices.get(0);
        QwenMessageInResponse message = choice.getMessage();

        var j = new JsonObject();
        String role = message.getRole();
        j.put("role", role);
        String content = message.getContent();
        j.put("content", content);
        var toolCalls = message.getToolCalls();
        if (!toolCalls.isEmpty()) {
            JsonArray array = new JsonArray();
            toolCalls.forEach(toolCall -> {
                array.add(new JsonObject()
                        .put("id", toolCall.getId())
                        .put("type", toolCall.getType())
                        .put("function",
                                toolCall.getFunction() == null ? null
                                        : new JsonObject()
                                        .put("name", toolCall.getFunction().getName())
                                        .put("arguments", toolCall.getFunction().getArguments())
                        )
                );
            });
            j.put("tool_calls", array);
        }
        return new MixChatResponseImpl(new JsonObject()
                .put("message", j)
        );
    }

    MixChatMessage getMessage();
}
