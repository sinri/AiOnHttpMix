package io.github.sinri.AiOnHttpMix.azure.chat;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatResponse extends SimpleJsonifiableEntity {
    public ChatResponse(JsonObject jsonObject) {
        super(jsonObject);
    }

    public String getId() {
        return this.readString("id");
    }

    public String getObject() {
        return this.readString("object");
    }

    public String getModel() {
        return this.readString("model");
    }

    public Usage getUsage() {
        return new Usage(this.readJsonObject("usage"));
    }

    public JsonArray getChoiceArray() {
        return this.readJsonArray("choices");
    }

    public List<Choice> getChoices() {
        List<Choice> list = new ArrayList<>();
        getChoiceArray().forEach(x -> {
            list.add(new Choice((JsonObject) x));
        });
        return list;
    }

    public static class Usage extends SimpleJsonifiableEntity {
        public Usage(JsonObject jsonObject) {
            super(jsonObject);
        }

        public Integer getPromptTokens() {
            return readInteger("prompt_tokens");
        }

        public Integer getCompletionTokens() {
            return readInteger("completion_tokens");
        }

        public Integer getTotalTokens() {
            return readInteger("total_tokens");
        }
    }

    public static class Choice extends SimpleJsonifiableEntity {
        /**
         * {
         * "message":{
         * "role":"assistant",
         * "content":"Yes, other Azure AI services also support customer managed keys. Azure AI services offer multiple options for customers to manage keys, such as using Azure Key Vault, customer-managed keys in Azure Key Vault or customer-managed keys through Azure Storage service. This helps customers ensure that their data is secure and access to their services is controlled."
         * },
         * "finish_reason":"stop",
         * "index":0
         * }
         */
        public Choice(JsonObject jsonObject) {
            super(jsonObject);
        }

        public String role() {
            return readString("message", "role");
        }

        public String content() {
            return readString("message", "content");
        }

        public JsonArray rawToolCalls() {
            return readJsonArray("message", "tool_calls");
        }

        public List<ToolCall> toolCalls() {
            return Objects.requireNonNull(readJsonObjectArray("message", "tool_calls"))
                    .stream()
                    .map(ToolCall::new)
                    .toList();
        }

        /**
         * This might be `tool_calls`.
         */
        public String finishReason() {
            return readString("finish_reason");
        }

        public Integer index() {
            return readInteger("index");
        }
    }
}
