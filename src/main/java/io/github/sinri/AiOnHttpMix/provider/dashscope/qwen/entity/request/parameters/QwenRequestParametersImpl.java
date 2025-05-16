package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.request.parameters;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class QwenRequestParametersImpl extends JsonifiableEntityImpl<QwenRequestParameters> implements QwenRequestParameters {
    public QwenRequestParametersImpl() {
        this(new JsonObject());
    }

    public QwenRequestParametersImpl(JsonObject jsonObject) {
        super(jsonObject);
        this.resultFormat("message");
    }

    @Override
    public @NotNull QwenRequestParameters getImplementation() {
        return this;
    }

    public static class QwenRequestOcrOptionsImpl extends JsonifiableEntityImpl<QwenRequestOcrOptions> implements QwenRequestOcrOptions {
        public QwenRequestOcrOptionsImpl() {
            super();
        }

        public QwenRequestOcrOptionsImpl(JsonObject jsonObject) {
            super(jsonObject);
        }

        public QwenRequestOcrOptionsImpl(String task, @Nullable JsonObject task_config) {
            super(new JsonObject().put("task", task));
            if (task_config != null) {
                write("task_config", task_config);
            }
        }

        @Override
        public @NotNull QwenRequestOcrOptions getImplementation() {
            return this;
        }
    }

    public static class QwenRequestSearchOptionsImpl extends JsonifiableEntityImpl<QwenRequestSearchOptions> implements QwenRequestSearchOptions {

        public QwenRequestSearchOptionsImpl() {
            super();
        }

        public QwenRequestSearchOptionsImpl(JsonObject jsonObject) {
            super(jsonObject);
        }

        @Override
        public @NotNull QwenRequestSearchOptions getImplementation() {
            return this;
        }
    }
}
