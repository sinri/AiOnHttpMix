package io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.txt;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;

public interface QwenToolCallMixin extends UnmodifiableJsonifiableEntity {
    default String getType() {
        return readString("type");
    }

    FunctionCall getFunction();

    interface FunctionCall extends UnmodifiableJsonifiableEntity {
        default String getName() {
            return readString("name");
        }

        default String getArguments() {
            return readString("arguments");
        }
    }

    default Integer getIndex(){
        return readInteger("index");
    }

    default String getId(){
        return readString("id");
    }


}