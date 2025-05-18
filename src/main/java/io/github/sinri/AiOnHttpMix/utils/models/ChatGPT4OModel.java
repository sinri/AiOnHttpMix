package io.github.sinri.AiOnHttpMix.utils.models;

/**
 * 表示OpenAI GPT-4o聊天模型的实现。
 * 该类为单例，名称为"gpt-4o"。
 *
 * @since 2.0.0
 */
public final class ChatGPT4OModel extends ChatGPTModelSeries {
    /**
     * 模型名称常量。
     */
    public final static String MODEL_NAME = "gpt-4o";

    ChatGPT4OModel() {
    }

    @Override
    public String getModelName() {
        return MODEL_NAME;
    }
}
