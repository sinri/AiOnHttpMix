package io.github.sinri.AiOnHttpMix.utils.models;

import io.github.sinri.AiOnHttpMix.utils.series.ChatModelSeries;

/**
 * 表示豆包Pro-32k聊天模型的实现。
 * 该类为单例，名称为"doubao-pro-32k"。
 */
public final class DoubaoPro32kModel implements ChatModel {
    /**
     * 模型名称常量。
     */
    public final static String NAME = "doubao-pro-32k";

    DoubaoPro32kModel() {

    }

    /**
     * 获取所属模型系列。
     * @return 豆包模型系列
     */
    @Override
    public ChatModelSeries getSeries() {
        return ChatModelSeries.doubao;
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
