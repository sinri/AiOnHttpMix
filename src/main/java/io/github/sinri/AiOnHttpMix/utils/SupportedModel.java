package io.github.sinri.AiOnHttpMix.utils;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum SupportedModel {
    ChatGPT(SupportedModelSeries.ChatGPT),
    QwenPlus(SupportedModelSeries.Qwen, QwenKit.QwenModel.QWEN_PLUS.getModelCode()),
    QwenMax(SupportedModelSeries.Qwen, QwenKit.QwenModel.QWEN_MAX.getModelCode()),
    Volces(SupportedModelSeries.Volces),
    ;
    private final @NotNull SupportedModelSeries series;
    private final @Nullable String mappedModelCode;

    SupportedModel(@NotNull SupportedModelSeries series) {
        this.series = series;
        this.mappedModelCode = null;
    }

    SupportedModel(@NotNull SupportedModelSeries series, @NotNull String mappedModelCode) {
        this.series = series;
        this.mappedModelCode = mappedModelCode;
    }

    public @NotNull SupportedModelSeries getSeries() {
        return series;
    }

    public @Nullable String getMappedModelCode() {
        return mappedModelCode;
    }

    @NotNull
    public QwenKit.QwenModel asQwenModel() {
        if (series == SupportedModelSeries.Qwen) {
            if (mappedModelCode == null) {
                throw new IllegalArgumentException();
            }
            return QwenKit.QwenModel.fromModelCode(mappedModelCode);
        }
        throw new IllegalArgumentException();
    }
}
