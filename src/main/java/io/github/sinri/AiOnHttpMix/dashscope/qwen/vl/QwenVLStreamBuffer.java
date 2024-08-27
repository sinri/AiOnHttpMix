package io.github.sinri.AiOnHttpMix.dashscope.qwen.vl;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class QwenVLStreamBuffer {
    private final TempChoice primaryChoice;
    // usage
    private QwenVLUsage usage;

    public QwenVLStreamBuffer() {
        this.primaryChoice = new TempChoice();
    }

    public void accept(QwenVLResponse vlChatResponseAsChunk) {
        if (vlChatResponseAsChunk.getUsage() != null) {
            usage = vlChatResponseAsChunk.getUsage();
        }

        QwenVLResponse.Output output = vlChatResponseAsChunk.getOutput();
        if (output != null) {
            List<QwenVLResponse.Choice> choices = output.getChoices();
            if (choices != null && !choices.isEmpty()) {
                QwenVLResponse.Choice choice = choices.get(0);
                primaryChoice.accept(choice);
            }
        }
    }

    public QwenVLResponse toVLChatResponse() {
        return QwenVLResponse.wrap(new JsonObject()
                .put("output", new JsonObject()
                        .put("choices", new JsonArray()
                                .add(primaryChoice.toChoice().cloneAsJsonObject())
                        )
                )
                .put("usage", usage.cloneAsJsonObject())
        );
    }

    public static class TempChoice {
        private QwenVLRole role;
        private final StringBuilder contentText = new StringBuilder();
        private String finishReason;

        public TempChoice() {
        }

        public void accept(QwenVLResponse.Choice choice) {
            finishReason = choice.getFinishReason();

            QwenVLOutputMessage message = choice.getMessage();
            if (message != null) {
                QwenVLRole role1 = message.getRole();
                if (role1 != null) {
                    role = role1;
                }

                List<QwenVLMessageContentItem> content = message.getContent();
                content.forEach(item -> {
                    String text = item.getText();
                    if (text != null) {
                        contentText.append(text);
                    }
                });
            }
        }

        public QwenVLResponse.Choice toChoice() {
            return QwenVLResponse.Choice.wrap(new JsonObject()
                    .put("message", new JsonObject()
                            .put("content", new JsonArray()
                                    .add(new JsonObject()
                                            .put("text", contentText.toString())
                                    )
                            )
                            .put("role", role.name())
                    )
                    .put("finish_reason", finishReason)
            );
        }
    }
}
