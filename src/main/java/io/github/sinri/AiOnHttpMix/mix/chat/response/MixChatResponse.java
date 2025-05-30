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
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * MixChatResponse 是一个统一的聊天响应接口，
 * 用于封装来自不同大模型（如 OpenAI GPT、Qwen、Doubao 等）的响应，
 * 并将其标准化为 MixChatMessage 结构，便于上层业务统一处理。
 * <p>
 * 该接口提供了多种静态工厂方法用于将不同厂商的响应对象转换为 MixChatResponse，
 * 并对工具调用（ToolCall）等内容进行适配和处理。
 * <p>
 * 典型用法：
 * <pre>
 *     MixChatResponse response = MixChatResponse.from(gptResponse);
 *     MixChatMessage message = response.getMessage();
 * </pre>
 */
public interface MixChatResponse extends UnmodifiableJsonifiableEntity {

    static MixChatResponse create() {
        return new MixChatResponseImpl();
    }

    /**
     * 将 JsonObject 包装为 MixChatResponse 实例。
     *
     * @param jsonObject 原始 JSON 对象
     * @return MixChatResponse 实例
     */
    static MixChatResponse wrap(JsonObject jsonObject) {
        return new MixChatResponseImpl(jsonObject);
    }

    /**
     * 处理工具调用（ToolCall）列表，并设置到 MixChatMessage。
     *
     * @param toolCalls      工具调用列表
     * @param mixChatMessage 目标 MixChatMessage
     * @param <T>            ToolCall 子类型
     */
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

    /**
     * 根据 MixChatMessage 构建 MixChatResponse。
     *
     * @param message 标准化的 MixChatMessage
     * @return MixChatResponse 实例
     */
    private static MixChatResponse build(MixChatMessage message) {
        return new MixChatResponseImpl(new JsonObject()
                .put(MixChatResponseImpl.KEY_MESSAGE, message.cloneAsJsonObject())
        );
    }

    /**
     * 将 OpenAI GPTResponse 转换为 MixChatResponse。
     * 只取第一个 choice。
     *
     * @param resp OpenAI GPTResponse 响应对象
     * @return MixChatResponse 实例
     */
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

    /**
     * 将 DoubaoResponse 转换为 MixChatResponse。
     * 只取第一个 choice。
     *
     * @param resp DoubaoResponse 响应对象
     * @return MixChatResponse 实例
     */
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

    /**
     * 将 QwenResponse 转换为 MixChatResponse。
     * 只取第一个 choice。
     *
     * @param resp QwenResponse 响应对象
     * @return MixChatResponse 实例
     */
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

    /**
     * 获取标准化后的 MixChatMessage。
     *
     * @return MixChatMessage 实例
     */
    MixChatMessage getMessage();

    MixChatResponse setMessage(MixChatMessage message);
}
