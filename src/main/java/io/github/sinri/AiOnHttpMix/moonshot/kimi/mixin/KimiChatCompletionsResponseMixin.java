package io.github.sinri.AiOnHttpMix.moonshot.kimi.mixin;

import io.github.sinri.AiOnHttpMix.moonshot.kimi.KimiKit;
import io.github.sinri.AiOnHttpMix.moonshot.kimi.impl.ChatCompletionsResponseImpl;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Objects;

public interface KimiChatCompletionsResponseMixin extends UnmodifiableJsonifiableEntity {
    default String getId() {
        return readString("id");
    }

    default String getObject() {
        return readString("object");
    }

    default Integer getCreated() {
        return readInteger("created");
    }

    default String getModel() {
        return readString("model");
    }

    default List<Choice> getChoices() {
        List<JsonObject> choices = readJsonObjectArray("choices");
        if (choices == null) {
            return null;
        }
        return choices.stream().map(Choice::wrap).toList();
    }

    default KimiUsageMixin getUsage() {
        return KimiUsageMixin.wrap(readJsonObject("usage"));
    }

    interface Choice extends UnmodifiableJsonifiableEntity {
        static Choice wrap(JsonObject json) {
            return new ChatCompletionsResponseImpl.ChoiceImpl(json);
        }

        default Integer getIndex() {
            return readInteger("index");
        }

        default KimiKit.Message getMessage() {
            JsonObject message = readJsonObject("message");
            Objects.requireNonNull(message);
            return KimiKit.Message.wrap(message);
        }

        default String getFinishReason() {
            return readString("finish_reason");
        }

        /*
        {
            "index":0,
            "message":{
                "role":"assistant",
                "content":"",
                "tool_calls":[
                    {
                        "index":0,
                        "id":"weatherQueryFunc:0",
                        "type":"function",
                        "function":{
                            "name":"weatherQueryFunc",
                            "arguments":"{\n    \"day\": \"2024-02-02\",\n    \"place\": \"东京都江户川区\"\n}"
                        }
                    }
                ]
            },
            "finish_reason":"tool_calls"
         }
         */
    }
}
