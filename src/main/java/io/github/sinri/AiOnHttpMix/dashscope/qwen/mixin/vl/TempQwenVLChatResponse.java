package io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.vl;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class TempQwenVLChatResponse {
    private final TempChoice primaryChoice;
    // usage
    private QwenKit.VLChatUsage usage;

    public TempQwenVLChatResponse() {
        this.primaryChoice = new TempChoice();
    }

    public void accept(QwenKit.VLChatResponse vlChatResponseAsChunk) {
        if (vlChatResponseAsChunk.getUsage() != null) {
            usage = vlChatResponseAsChunk.getUsage();
        }

        QwenVLChatResponseMixin.Output output = vlChatResponseAsChunk.getOutput();
        if (output != null) {
            List<QwenVLChatResponseMixin.Choice> choices = output.getChoices();
            if (choices != null && !choices.isEmpty()) {
                QwenVLChatResponseMixin.Choice choice = choices.get(0);
                primaryChoice.accept(choice);
            }
        }
    }

    public QwenKit.VLChatResponse toVLChatResponse() {
        return QwenKit.VLChatResponse.wrap(new JsonObject()
                .put("output", new JsonObject()
                        .put("choices", new JsonArray()
                                .add(primaryChoice.toChoice().cloneAsJsonObject())
                        )
                )
                .put("usage", usage.cloneAsJsonObject())
        );
    }

    public static class TempChoice {
        private QwenVLChatRole role;
        private final StringBuilder contentText = new StringBuilder();
        private String finishReason;

        public TempChoice() {
        }

        public void accept(QwenVLChatResponseMixin.Choice choice) {
            finishReason = choice.getFinishReason();

            QwenKit.VLChatOutputMessage message = choice.getMessage();
            if (message != null) {
                QwenVLChatRole role1 = message.getRole();
                if (role1 != null) {
                    role = role1;
                }

                List<QwenVLChatMessageContentItem> content = message.getContent();
                content.forEach(item -> {
                    String text = item.getText();
                    if (text != null) {
                        contentText.append(text);
                    }
                });
            }
        }

        public QwenVLChatResponseMixin.Choice toChoice() {
            return QwenVLChatResponseMixin.Choice.wrap(new JsonObject()
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
