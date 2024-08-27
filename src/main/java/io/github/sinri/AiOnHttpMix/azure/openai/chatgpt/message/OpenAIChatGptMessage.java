package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.message;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGptRole;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface OpenAIChatGptMessage extends JsonifiableEntity<OpenAIChatGptMessage> {
    static Builder builder() {
        return new Builder();
    }

    final class Builder {
        private OpenAIChatGptMessage result;

        public OpenAIChatGptMessage build() {
            return result;
        }

        public Builder system(String content) {
            result = new SystemMessage(content);
            return this;
        }

        public Builder system(String name, String content) {
            result = new SystemMessage(name, content);
            return this;
        }

        public Builder user(String content) {
            result = new UserMessage(new JsonObject()
                    .put("content", content)
            );
            return this;
        }

        public Builder user(List<OpenAIChatGptMessageContentPart> content) {
            JsonArray array = new JsonArray();
            content.forEach(x -> {
                array.add(x.toJsonObject());
            });
            result = new UserMessage(new JsonObject()
                    .put("content", array)
            );
            return this;
        }

        public Builder assistant(String content) {
            result = new AssistantMessage(new JsonObject()
                    .put("content", content)
            );
            return this;
        }

        public Builder assistant(JsonArray toolCalls) {
            result = new AssistantMessage(new JsonObject()
                    .put("tool_calls", toolCalls)
            );
            return this;
        }
    }

    // context azureChatExtensionsMessageContext A representation of the additional context information available when Azure
    //OpenAI chat extensions are involved
    //in the generation of a corresponding chat completions response. This context
    //information is only populated when
    //using an Azure OpenAI request configured to use a matching extension.

    /**
     * The type of the content part.
     */
    enum ChatCompletionRequestMessageContentPartType {
        /**
         * The content part type is text.
         */
        text,
        /**
         * The content part type is image_url.
         */
        image_url,
    }

    default ChatGptRole getRole() {
        String role = readString("role");
        return ChatGptRole.valueOf(role);
    }
}