package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.message.AssistantMessage;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.message.ChatCompletionRequestMessageContentPartImpl;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.message.SystemMessage;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.message.UserMessage;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface OpenAIMessageMixin<E> extends JsonifiableEntity<E> {
    /**
     * <a href="https://learn.microsoft.com/en-us/azure/ai-services/openai/reference#chatcompletionrequestmessagerole">chatCompletionRequestMessageRole</a>
     */
    enum ChatCompletionRequestMessageRole {
        system,
        user,
        assistant,
        @Deprecated function,
        tool
    }

    final class Builder {
        private ChatGPTKit.Message result;

        public ChatGPTKit.Message build() {
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

        public Builder user(List<ChatCompletionRequestMessageContentPart> content) {
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

    interface ChatCompletionRequestMessageContentPart extends JsonifiableEntity<ChatCompletionRequestMessageContentPart> {
        static ChatCompletionRequestMessageContentPart withText(String text) {
            return new ChatCompletionRequestMessageContentPartImpl(new JsonObject()
                    .put("type", ChatCompletionRequestMessageContentPartType.text.name())
                    .put("text", text)
            );
        }

        static ChatCompletionRequestMessageContentPart withImage(String url) {
            return new ChatCompletionRequestMessageContentPartImpl(new JsonObject()
                    .put("type", ChatCompletionRequestMessageContentPartType.image_url.name())
                    .put("url", url)
            );
        }

        static ChatCompletionRequestMessageContentPart withImage(String url, @NotNull ImageDetailLevel imageDetailLevel) {
            return new ChatCompletionRequestMessageContentPartImpl(new JsonObject()
                    .put("type", ChatCompletionRequestMessageContentPartType.image_url.name())
                    .put("url", url)
                    .put("detail", imageDetailLevel.name())
            );
        }

        /**
         * Specifies the detail level of the image.
         */
        enum ImageDetailLevel {
            auto, low, high
        }
    }

    default ChatCompletionRequestMessageRole getRole() {
        String role = readString("role");
        return ChatCompletionRequestMessageRole.valueOf(role);
    }
}