package io.github.sinri.AiOnHttpMix.moonshot.chat;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatCompletionsStreamResponse extends SimpleJsonifiableEntity {
    public ChatCompletionsStreamResponse(JsonObject jsonObject) {
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

    public static class Choice extends SimpleJsonifiableEntity {
        public Choice(JsonObject jsonObject) {
            super(jsonObject);
        }

        public Integer getIndex() {
            return readInteger("index");
        }

        public Message getDelta() {
            JsonObject delta = readJsonObject("delta");
            Objects.requireNonNull(delta);
            String roleExp = delta.getString("role");
            Role role;
            if (roleExp == null) {
                role = null;
            } else {
                role = Role.valueOf(roleExp);
            }
            return new Message(role, delta.getString("content"));
        }

        @Nullable
        public String getFinishReason() {
            return readString("finish_reason");
        }

        @Nullable
        public Usage getUsage() {
            JsonObject x = readJsonObject("usage");
            if (x == null) return null;
            return new Usage(x);
        }
    }


}
