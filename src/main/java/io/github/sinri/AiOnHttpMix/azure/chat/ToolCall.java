package io.github.sinri.AiOnHttpMix.azure.chat;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;


public class ToolCall {
    private final JsonObject jsonObject;

    public ToolCall(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JsonObject toJsonObject() {
        return jsonObject;
    }

    public int index() {
        return jsonObject.getInteger("index");
    }

    @Nullable
    public ToolCallAsFunction asFunctionCall() {
        if (jsonObject.containsKey("function")) {
            return new ToolCallAsFunction(jsonObject.getJsonObject("function"));
        } else {
            return null;
        }
    }

    @Nullable
    public ToolCallAsFunctionStream asFunctionCallStream() {
        if (jsonObject.containsKey("function")) {
            return new ToolCallAsFunctionStream(jsonObject.getJsonObject("function"));
        } else {
            return null;
        }
    }

    public static class ToolCallAsFunction {
        private final JsonObject jsonObject;

        public ToolCallAsFunction(JsonObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        public JsonObject toJsonObject() {
            return jsonObject;
        }

        public JsonObject arguments() {
            return jsonObject.getJsonObject("arguments");
        }

        public String name() {
            return jsonObject.getString("name");
        }
    }

    public static class ToolCallAsFunctionStream {
        private final JsonObject jsonObject;

        public ToolCallAsFunctionStream(JsonObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        public JsonObject toJsonObject() {
            return jsonObject;
        }

        public String argumentsStreamPiece() {
            return jsonObject.getString("arguments");
        }

        @Nullable
        public String name() {
            return jsonObject.getString("name");
        }
    }
}
