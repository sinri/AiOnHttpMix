package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface OpenAIChatGptResponseToolCall extends UnmodifiableJsonifiableEntity {
    static OpenAIChatGptResponseToolCall wrap(JsonObject jsonObject) {
        return new OpenAIChatGptResponseToolCallImpl(jsonObject);
    }

    static OpenAIChatGptResponseToolCall wrapForChunk(JsonObject jsonObject) {
        return new OpenAIChatGptResponseChunkToolCallImpl(jsonObject);
    }
//        {
//            "function":{
//            "arguments":"{\"keywords\":[\"天猫平台\",\"商品销售额\",\"每年\"]}",
//                    "name":"searchDataSet"
//        },
//            "id":"call_9E5rm9w7tIPGoi0XHp6mESBM",
//                "type":"function"
//        }

    default String getId() {
        return readString("id");
    }

    @Nullable
    default String getType() {
        return readString("type");
    }

    @Nullable
    default Integer getIndex() {
        return readInteger("index");
    }

    default OpenAIChatGptResponseFunctionCall getFunction() {
        JsonObject x = readJsonObject("function");
        return new OpenAIChatGptResponseFunctionCallImpl(Objects.requireNonNull(x));
    }

}