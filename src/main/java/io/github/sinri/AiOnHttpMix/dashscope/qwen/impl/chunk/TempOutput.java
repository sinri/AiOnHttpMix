package io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.chunk;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.txt.ChatResponseBase;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.txt.QwenChatMessageResponseInChunkMixin;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.txt.QwenMessageMixin;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class TempOutput {
    private final TempChoice tempChoice;
    private ChatResponseBase.Usage usage;

    public TempOutput() {
        this.tempChoice = new TempChoice();
    }

    public void acceptChunkData(QwenKit.ChatMessageResponseInChunk chatMessageResponseInChunk) {
        usage = chatMessageResponseInChunk.getUsage();
        QwenChatMessageResponseInChunkMixin.OutputChunkForMessageResponse output = chatMessageResponseInChunk.getOutput();
        if (output != null) {
            List<QwenChatMessageResponseInChunkMixin.OutputChunkForMessageResponse.Choice> choices = output.getChoices();
            if (!choices.isEmpty()) {
                QwenChatMessageResponseInChunkMixin.OutputChunkForMessageResponse.Choice choice = choices.get(0);
                tempChoice.acceptChoice(choice);
            }
        }
    }

    public QwenKit.ChatMessageResponse toChatMessageResponse() {
        return QwenKit.ChatMessageResponse.wrap(new JsonObject()
                .put("usage", usage.cloneAsJsonObject())
                .put("output", new JsonObject()
                        .put("choices", new JsonArray()
                                .add(tempChoice.toJsonObject())
                        )
                )
        );
    }

    public static class TempChoice {
        private String finishReason;
        private final TempMessage tempMessage;

        public TempChoice() {
            this.tempMessage = new TempMessage();
        }

        public void acceptChoice(QwenChatMessageResponseInChunkMixin.OutputChunkForMessageResponse.Choice choice) {
            this.finishReason = choice.getFinishReason();

            QwenKit.Message message = choice.getMessage();
            this.tempMessage.acceptMessage(message);
        }

        public JsonObject toJsonObject() {
            return new JsonObject()
                    .put("finish_reason", finishReason)
                    .put("message", tempMessage.toJsonObject());
        }
    }

    public static class TempMessage {
        private QwenMessageMixin.Role role;
        private final StringBuilder content;

        public TempMessage() {
            content = new StringBuilder();
        }

        public void acceptMessage(QwenKit.Message message) {
            if (message.getRole() != null) {
                this.role = message.getRole();
            }
            if (message.getContent() != null) {
                this.content.append(message.getContent());
            }
        }

        public JsonObject toJsonObject() {
            return new JsonObject()
                    .put("role", this.role.name())
                    .put("content", this.content.toString());
        }
    }
}
