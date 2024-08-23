package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.response.FunctionCallImpl;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface OpenAIToolCallMixin extends UnmodifiableJsonifiableEntity {
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
    default Integer getIndex(){
        return readInteger("index");
    }

    default FunctionCall getFunction() {
        JsonObject x = readJsonObject("function");
        return new FunctionCallImpl(Objects.requireNonNull(x));
    }

    interface FunctionCall extends UnmodifiableJsonifiableEntity {
        @Nullable
        default String getName() {
            return readString("name");
        }

        @Nullable
        default String getArguments() {
            return readString("arguments");
        }
    }

    interface FunctionCallInChunk extends FunctionCall {

        default String getArgumentsStreamPiece() {
            return readString("arguments");
        }
    }
}