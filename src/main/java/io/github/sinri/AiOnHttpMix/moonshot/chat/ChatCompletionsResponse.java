package io.github.sinri.AiOnHttpMix.moonshot.chat;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatCompletionsResponse extends SimpleJsonifiableEntity {
    public ChatCompletionsResponse(JsonObject jsonObject) {
        super(jsonObject);
    }

    public String getId() {
        return readString("id");
    }

    public String getObject() {
        return readString("object");
    }

    public Integer getCreated() {
        return readInteger("created");
    }

    public String getModel() {
        return readString("model");
    }

    public List<Choice> getChoices() {
        List<Choice> list = new ArrayList<>();
        List<JsonObject> array = readJsonObjectArray("choices");
        if (array != null) {
            array.forEach(a -> {
                list.add(new Choice(a));
            });
        }
        return list;
    }

    public Usage getUsage() {
        return new Usage(readJsonObject("usage"));
    }

    public static class Choice extends SimpleJsonifiableEntity {
        public Choice(JsonObject jsonObject) {
            super(jsonObject);
        }

        public Integer getIndex() {
            return readInteger("index");
        }

        public Message getMessage() {
            JsonObject message = readJsonObject("message");
            Objects.requireNonNull(message);
            return new Message(message);
        }

        public String getFinishReason() {
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
