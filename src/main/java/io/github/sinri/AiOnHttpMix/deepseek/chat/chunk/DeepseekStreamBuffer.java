package io.github.sinri.AiOnHttpMix.deepseek.chat.chunk;

import io.github.sinri.AiOnHttpMix.deepseek.chat.DeepseekChatResponse;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMResponse;
import io.github.sinri.AiOnHttpMix.utils.LLMStreamBuffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class DeepseekStreamBuffer implements LLMStreamBuffer {
    private final JsonObject buffer;
    private final DeepseekChoiceBuffer choiceBuffer;

    public DeepseekStreamBuffer() {
        buffer = new JsonObject();
        choiceBuffer = new DeepseekChoiceBuffer();
    }

    public void accept(DeepseekResponseChunk chunk) {
        if (buffer.isEmpty()) {
            buffer.put("id", chunk.getId());
            buffer.put("created", chunk.getCreated());
            buffer.put("model", chunk.getModel());
        }

        List<DeepseekResponseChunk.ChoiceChunk> array = chunk.getChoices();
        if (array != null && !array.isEmpty()) {
            DeepseekResponseChunk.ChoiceChunk choiceChunk = array.get(0);
            choiceBuffer.accept(choiceChunk);
        }
    }

    public DeepseekChatResponse toResponse() {
        buffer.put("choices", new JsonArray().add(choiceBuffer.toChoice().cloneAsJsonObject()));
        return DeepseekChatResponse.wrap(buffer);
    }

    @Override
    public AnyLLMResponse toAnyLLMResponse() {
        return AnyLLMResponse.from(toResponse());
    }

    public static class DeepseekChoiceBuffer {
        private final StringBuilder contentBuffer = new StringBuilder();
        private final StringBuilder reasoningContentBuffer = new StringBuilder();
        private final JsonObject buffer;
        private String role;

        public DeepseekChoiceBuffer() {
            buffer = new JsonObject();
        }

        public void accept(DeepseekResponseChunk.ChoiceChunk choiceChunk) {
            if (buffer.isEmpty()) {
                Integer index = choiceChunk.getIndex();
                buffer.put("index", index);
            }
            String finishReason = choiceChunk.getFinishReason();
            if (finishReason != null) {
                buffer.put("finish_reason", finishReason);
            }
            DeepseekResponseChunk.ChoiceChunkDelta delta = choiceChunk.getDelta();
            if (delta != null) {
                if (this.role == null) {
                    this.role = delta.getRole();
                }
                String reasoningContent = delta.getReasoningContent();
                if (reasoningContent != null) {
                    reasoningContentBuffer.append(reasoningContent);
                }
                String content = delta.getContent();
                if (content != null) {
                    contentBuffer.append(content);
                }
            }
        }

        public DeepseekChatResponse.Choice toChoice() {
            buffer.put("message", new JsonObject()
                    .put("role", role)
                    .put("content", contentBuffer.toString())
                    .put("reasoning_content", reasoningContentBuffer.toString())
            );
            return DeepseekChatResponse.Choice.wrap(buffer);
        }
    }
}
