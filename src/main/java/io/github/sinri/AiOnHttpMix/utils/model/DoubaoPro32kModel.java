package io.github.sinri.AiOnHttpMix.utils.model;

import io.github.sinri.AiOnHttpMix.utils.series.ChatModelSeries;

public final class DoubaoPro32kModel implements ChatModel {
    public final static String NAME = "doubao-pro-32k";

    DoubaoPro32kModel() {

    }

    @Override
    public ChatModelSeries getSeries() {
        return ChatModelSeries.doubao;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
