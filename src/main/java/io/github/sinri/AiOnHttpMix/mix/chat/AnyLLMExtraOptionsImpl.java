package io.github.sinri.AiOnHttpMix.mix.chat;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @since 1.2.2
 */
class AnyLLMExtraOptionsImpl extends SimpleJsonifiableEntity implements AnyLLMExtraOptions {

    public AnyLLMExtraOptionsImpl() {
        this(new JsonObject());
    }

    public AnyLLMExtraOptionsImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public @Nullable Double getTemperature() {
        return readDouble("temperature");
    }

    @Override
    public AnyLLMExtraOptions setTemperature(@Nullable Double temperature) {
        this.toJsonObject().put("temperature", temperature);
        return this;
    }

    @Override
    public @Nullable String getResponseFormat() {
        return readString("response_format");
    }

    @Override
    public AnyLLMExtraOptions setResponseFormat(@Nullable String responseFormat) {
        this.toJsonObject().put("response_format", responseFormat);
        return this;
    }

    @Override
    public @Nullable Boolean getIncrementalOutput() {
        return this.readBoolean("incremental_output");
    }

    @Override
    public AnyLLMExtraOptions setIncrementalOutput(@Nullable Boolean incrementalOutput) {
        this.toJsonObject().put("incremental_output", incrementalOutput);
        return this;
    }
}
