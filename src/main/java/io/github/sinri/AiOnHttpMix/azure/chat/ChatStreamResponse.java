package io.github.sinri.AiOnHttpMix.azure.chat;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * STREAM
 * {
 * "id":"chatcmpl-8UmfcjGYlWFzZCpKdZ1Ano6deylC0",
 * "object":"chat.completion.chunk",
 * "created":1702347620,
 * "model":"gpt-4",
 * "choices":[
 * {
 * "index":0,
 * "finish_reason":null,
 * "delta":{
 * "content":"。"
 * },
 * "content_filter_results":{
 * "hate":{"filtered":false,"severity":"safe"},
 * "self_harm":{"filtered":false,"severity":"safe"},
 * "sexual":{"filtered":false,"severity":"safe"},
 * "violence":{"filtered":false,"severity":"safe"}
 * }
 * }
 * ],
 * "system_fingerprint":"fp_50a4261de5"
 * }
 */
public class ChatStreamResponse extends SimpleJsonifiableEntity {
    public static final String ObjectAsChatCompletionChunk = "chat.completion.chunk";

    public ChatStreamResponse(JsonObject jsonObject) {
        super(jsonObject);
    }

    public String getId() {
        return readString("id");
    }

    public String getObject() {
        return readString("object");
    }

    public Long getCreated() {
        return readLong("created");
    }

    public String getModel() {
        return readString("model");
    }

    public List<Choice> getChoices() {
        List<Choice> list = new ArrayList<>();
        List<JsonObject> choices = readJsonObjectArray("choices");
        if (choices != null) {
            for (JsonObject choice : choices) {
                list.add(new Choice(choice));
            }
        }
        return list;
    }

    public String getSystemFingerprint() {
        return readString("system_fingerprint");
    }

    public static class Choice extends SimpleJsonifiableEntity {

        public Choice(JsonObject choice) {
            super(choice);
        }

        public Long getIndex() {
            return readLong("index");
        }

        public String getFinishReason() {
            return readString("finish_reason");
        }

        public Delta getDelta() {
            JsonObject delta = readJsonObject("delta");
            return new Delta(Objects.requireNonNull(delta));
        }

        public JsonObject getRawContentFilterResults() {
            return readJsonObject("content_filter_results");
        }
    }

    public static class ChoiceForCCC extends SimpleJsonifiableEntity {
        public ChoiceForCCC(JsonObject jsonObject) {
            super(jsonObject);
        }

        public Long getIndex() {
            return readLong("index");
        }
    }

    public static class Delta extends SimpleJsonifiableEntity {

        public Delta(JsonObject delta) {
            super(delta);
        }

        public String getContent() {
            return readString("content");
        }

        public JsonArray rawToolCalls() {
            return readJsonArray("tool_calls");
        }

        public List<ToolCall> toolCalls() {
            return Objects.requireNonNull(readJsonObjectArray("tool_calls"))
                    .stream().map(ToolCall::new)
                    .toList();
        }
    }
}
