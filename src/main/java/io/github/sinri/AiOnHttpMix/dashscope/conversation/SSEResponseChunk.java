package io.github.sinri.AiOnHttpMix.dashscope.conversation;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;


public class SSEResponseChunk extends SimpleJsonifiableEntity {
    /**
     * @param block {@code id:1
     *              event:result
     *              data:{"output":{"finish_reason":"null","text":"最近"},"usage":{"output_tokens":3,"input_tokens":85},"request_id":"1117fb64-5dd9-9df0-a5ca-d7ee0e97032d"} }
     */
    public SSEResponseChunk(String block) {
        super();
        var lines = block.split("[\r\n]");
        for (String line : lines) {
            var components = line.split(":\\s*", 2);
            if (components.length < 2) continue;

            if ("id".equals(components[0])) {
                this.toJsonObject().put("id", components[1]);
            } else if ("event".equals(components[0])) {
                this.toJsonObject().put("event", components[1]);
            } else if ("data".equals(components[0])) {
                this.toJsonObject().put("data", components[1]);
            }
        }
    }

    public Long id() {
        return readLong("id");
    }

    public String event() {
        return readString("event");
    }

    public JsonObject data() {
        return readJsonObject("data");
    }

    public Output output() {
        return new Output(data().getJsonObject("output"));
    }

    public JsonObject usage() {
        return data().getJsonObject("usage");
    }

    public String requestId() {
        return data().getString("request_id");
    }

    public static class Output extends SimpleJsonifiableEntity {
        public Output(JsonObject jsonObject) {
            super(jsonObject);
        }

        public @Nullable String finishReason() {
            return readString("finish_reason");
        }

        public String text() {
            return readString("text");
        }
    }
}
