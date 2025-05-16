package io.github.sinri.AiOnHttpMix.utils.models;

import io.github.sinri.AiOnHttpMix.utils.series.ChatModelSeries;

/**
 * 表示OpenAI GPT-4o聊天模型的实现。
 * 该类为单例，名称为"gpt-4o"。
 * @since 2.0.0
 */
public final class ChatGPT4OModel implements ChatModel {
    /**
     * 模型名称常量。
     */
    public final static String NAME = "gpt-4o";

    ChatGPT4OModel() {
    }

    /**
     * 获取所属模型系列。
     * @return ChatGPT模型系列
     */
    @Override
    public ChatModelSeries getSeries() {
        return ChatModelSeries.chatgpt;
    }

    /**
     * 获取模型名称。
     * @return 模型名称
     */
    @Override
    public String getName() {
        return NAME;
    }
}
