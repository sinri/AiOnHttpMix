package io.github.sinri.AiOnHttpMix.utils.model;

import io.github.sinri.AiOnHttpMix.utils.series.ChatModelSeries;

public final class QwenPlusModel implements ChatModel {
    public final static String NAME = "qwen-plus";

    QwenPlusModel() {

    }

    @Override
    public ChatModelSeries getSeries() {
        return ChatModelSeries.qwen;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
