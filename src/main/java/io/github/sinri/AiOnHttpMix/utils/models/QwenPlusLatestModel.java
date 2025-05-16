package io.github.sinri.AiOnHttpMix.utils.models;

import io.github.sinri.AiOnHttpMix.utils.series.ChatModelSeries;

/**
 * 表示Qwen-Plus-Latest聊天模型的实现。
 * 该类为单例，名称为"qwen-plus-latest"。
 */
public final class QwenPlusLatestModel implements ChatModel {
    /**
     * 模型名称常量。
     */
    public final static String NAME = "qwen-plus-latest";

    QwenPlusLatestModel() {

    }

    /**
     * 获取所属模型系列。
     * @return Qwen模型系列
     */
    @Override
    public ChatModelSeries getSeries() {
        return ChatModelSeries.qwen;
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
