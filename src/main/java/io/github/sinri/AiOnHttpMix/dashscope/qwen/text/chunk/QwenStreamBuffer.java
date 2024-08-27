package io.github.sinri.AiOnHttpMix.dashscope.qwen.text.chunk;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenRole;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.QwenResponseBase;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.message.QwenMessage;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.response.QwenResponseInMessageFormat;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class QwenStreamBuffer {
    private final TempChoice tempChoice;
    private QwenResponseBase.Usage usage;

    public QwenStreamBuffer() {
        this.tempChoice = new TempChoice();
    }

    public void acceptChunkData(QwenResponseChunk chatMessageResponseInChunk) {
        usage = chatMessageResponseInChunk.getUsage();
        QwenResponseChunk.OutputChunkForMessageResponse output = chatMessageResponseInChunk.getOutput();
        if (output != null) {
            List<QwenResponseChunk.OutputChunkForMessageResponse.Choice> choices = output.getChoices();
            if (!choices.isEmpty()) {
                QwenResponseChunk.OutputChunkForMessageResponse.Choice choice = choices.get(0);
                tempChoice.acceptChoice(choice);
            }
        }
    }

    public QwenResponseInMessageFormat toChatMessageResponse() {
        return QwenResponseInMessageFormat.wrap(new JsonObject()
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

        public void acceptChoice(QwenResponseChunk.OutputChunkForMessageResponse.Choice choice) {
            this.finishReason = choice.getFinishReason();

            QwenMessage message = choice.getMessage();
            this.tempMessage.acceptMessage(message);
        }

        public JsonObject toJsonObject() {
            return new JsonObject()
                    .put("finish_reason", finishReason)
                    .put("message", tempMessage.toJsonObject());
        }
    }

    public static class TempMessage {
        private QwenRole role;
        private final StringBuilder content;

        public TempMessage() {
            content = new StringBuilder();
        }

        public void acceptMessage(QwenMessage message) {
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
