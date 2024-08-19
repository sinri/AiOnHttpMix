package io.github.sinri.AiOnHttpMix.azure.chat;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Properties:
 * - id: string
 * - model: string
 * - created: long
 * - object: string `extensions.chat.completion.chunk`
 * - choices: array
 * - index: long
 * - delta: object
 * - content: string
 * - end_turn: bool
 * - finish_reason: null|string
 */
public class ExtensionsChatCompletionChunkResponse extends SimpleJsonifiableEntity {

    public static final String ObjectCode = "extensions.chat.completion.chunk";

    public ExtensionsChatCompletionChunkResponse(JsonObject jsonObject) {
        super(jsonObject);
    }

    public String getId() {
        return readString("id");
    }

    public String getModel() {
        return readString("model");
    }

    public Long getCreated() {
        return readLong("created");
    }

    public String getObject() {
        return readString("object");
    }

    public List<Choice> getChoices() {
        List<Choice> list = new ArrayList<>();
        List<JsonObject> array = readJsonObjectArray("choices");
        if (array != null) {
            array.forEach(x -> {
                list.add(new Choice(x));
            });
        }
        return list;
    }

    public static class Choice extends SimpleJsonifiableEntity {
        public Choice(JsonObject jsonObject) {
            super(jsonObject);
        }

        public Long getIndex() {
            return readLong("index");
        }

        public Boolean getEndTurn() {
            return readBoolean("end_turn");
        }

        public @Nullable String getFinishReason() {
            return readString("finish_reason");
        }

        public Delta getDelta() {
            return new Delta(readJsonObject("delta"));
        }
    }

    public static class Delta extends SimpleJsonifiableEntity {
        public Delta(JsonObject jsonObject) {
            super(jsonObject);
        }

        public @Nullable String getContent() {
            return readString("content");
        }

        public @Nullable String getRole() {
            return readString("role");
        }

        public @Nullable Context getContext() {
            var x = readJsonObject("context");
            if (x == null) return null;
            return new Context(x);
        }
    }

    public static class Context extends SimpleJsonifiableEntity {
        public Context(JsonObject jsonObject) {
            super(jsonObject);
        }

        public List<Message> getMessages() {
            List<Message> list = new ArrayList<>();
            var array = readJsonObjectArray("messages");
            if (array != null) {
                array.forEach(x -> {
                    list.add(new Message(x));
                });
            }
            return list;
        }
    }

    public static class Message extends SimpleJsonifiableEntity {
        public Message(JsonObject jsonObject) {
            super(jsonObject);
        }

        public String getRole() {
            return readString("role");
        }

        public String getContent() {
            return readString("content");
        }

        public Boolean getEndTurn() {
            return readBoolean("end_turn");
        }
    }
}
