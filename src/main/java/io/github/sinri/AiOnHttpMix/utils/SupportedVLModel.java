package io.github.sinri.AiOnHttpMix.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum SupportedVLModel {
    QwenVLPlus(SupportedProvider.DashScope, "qwen-vl-plus"),
    QwenVLMax(SupportedProvider.DashScope, "qwen-vl-max"),
    DoubaoVL(SupportedProvider.Volces),
    ;

    private final @NotNull SupportedProvider provider;
    private final @Nullable String mappedModelCode;

    SupportedVLModel(@NotNull SupportedProvider provider) {
        this.provider = provider;
        this.mappedModelCode = null;
    }

    SupportedVLModel(@NotNull SupportedProvider provider, @NotNull String mappedModelCode) {
        this.provider = provider;
        this.mappedModelCode = mappedModelCode;
    }

    public @NotNull SupportedProvider getProvider() {
        return provider;
    }

    public @Nullable String getMappedModelCode() {
        return mappedModelCode;
    }
}
