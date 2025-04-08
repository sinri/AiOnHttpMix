package io.github.sinri.AiOnHttpMix.mix.chat;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

/**
 * 调用模型时常见的参数设置，不保证被所有模型支持。
 *
 * @since 1.2.2
 */
public interface AnyLLMExtraOptions {
    static AnyLLMExtraOptions create() {
        return new AnyLLMExtraOptionsImpl();
    }

    static AnyLLMExtraOptions wrap(@Nullable JsonObject jsonObject) {
        if (jsonObject == null) {
            return create();
        } else {
            return new AnyLLMExtraOptionsImpl(jsonObject);
        }
    }

    /**
     * @since 1.2.2
     */
    @Nullable
    Double getTemperature();

    /**
     * @since 1.2.2
     */
    AnyLLMExtraOptions setTemperature(@Nullable Double temperature);

    /**
     * @since 1.2.2
     */
    @Nullable
    String getResponseFormat();

    /**
     * @since 1.2.2
     */
    AnyLLMExtraOptions setResponseFormat(@Nullable String responseFormat);

    /**
     * @since 1.2.2
     */
    @Nullable
    Boolean getIncrementalOutput();

    /**
     * @since 1.2.2
     */
    AnyLLMExtraOptions setIncrementalOutput(@Nullable Boolean incrementalOutput);
}
