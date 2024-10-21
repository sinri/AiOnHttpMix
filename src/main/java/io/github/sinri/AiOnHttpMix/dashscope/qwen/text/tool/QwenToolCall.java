package io.github.sinri.AiOnHttpMix.dashscope.qwen.text.tool;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface QwenToolCall extends UnmodifiableJsonifiableEntity {
    static QwenToolCall wrap(JsonObject jsonObject) {
        return new QwenToolCallImpl(jsonObject);
    }

    default String getType() {
        return readString("type");
    }

    FunctionCall getFunction();

    default Integer getIndex() {
        return readInteger("index");
    }

    default String getId() {
        return readString("id");
    }

    interface FunctionCall extends UnmodifiableJsonifiableEntity {
        static FunctionCall wrap(JsonObject jsonObject) {
            return new QwenToolCallImpl.FunctionCallImpl(jsonObject);
        }

        default String getName() {
            return readString("name");
        }

        default String getArguments() {
            return readString("arguments");
        }
    }


}