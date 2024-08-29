package io.github.sinri.AiOnHttpMix.dashscope.qwen.text.response;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

class QwenResponseInTextFormatImpl extends UnmodifiableJsonifiableEntityImpl implements QwenResponseInTextFormat {
    private final int statusCode;

    public QwenResponseInTextFormatImpl(int statusCode, @NotNull JsonObject jsonObject) {
        super(jsonObject);
        this.statusCode = statusCode;
    }

    @Override
    public OutputForTextResponse getOutput() {
        JsonObject output = readJsonObject("output");
        Objects.requireNonNull(output);
        return new OutputForTextResponseImpl(output);
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public Usage getUsage() {
        JsonObject usage = readJsonObject("usage");
        Objects.requireNonNull(usage);
        return Usage.wrap(usage);
    }

    public static class OutputForTextResponseImpl extends UnmodifiableJsonifiableEntityImpl implements OutputForTextResponse {

        public OutputForTextResponseImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }
}
