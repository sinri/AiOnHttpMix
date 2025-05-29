package io.github.sinri.AiOnHttpMix.mix.chat.response.stream;

import io.github.sinri.AiOnHttpMix.mix.chat.message.MixChatMessage;
import io.github.sinri.AiOnHttpMix.mix.chat.response.MixChatResponse;
import io.github.sinri.AiOnHttpMix.utils.StreamPieceCollector;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.common.ToolCallStreamPieceCollector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MixChatResponseBuffer implements StreamPieceCollector<MixChatResponseChunk, MixChatResponse> {
    // private final Map<Integer,MixChatResponseChunkChoiceBuffer>
    // choiceBufferMap=new HashMap<>();
    private final MixChatResponseChunkChoiceBuffer choiceBuffer = new MixChatResponseChunkChoiceBuffer();

    @Override
    public void accept(MixChatResponseChunk piece) {
        List<MixChatResponseChunkChoice> choices = piece.getChoices();
        if (!choices.isEmpty()) {
            choiceBuffer.accept(choices.get(0));
        }
    }

    @Override
    public MixChatResponse build() {
        MixChatResponse mixChatResponse = MixChatResponse.create();
        mixChatResponse.setMessage(choiceBuffer.build());
        return mixChatResponse;
    }

    private static class MixChatResponseChunkChoiceBuffer
            implements StreamPieceCollector<MixChatResponseChunkChoice, MixChatMessage> {

        private String role;
        private final StringBuilder contentBuffer = new StringBuilder();
        private final StringBuilder reasoningContentBuffer = new StringBuilder();
        private Integer index;
        private String finishReason;
        private final Map<Integer, ToolCallStreamPieceCollector> tcMap = new HashMap<>();

        @Override
        public void accept(MixChatResponseChunkChoice piece) {
            String content = piece.getContent();
            String reasoningContent = piece.getReasoningContent();
            List<ToolCall> toolCalls = piece.getToolCalls();

            if (role == null) {
                this.role = piece.getRole();
            }
            if (index == null) {
                this.index = piece.getIndex();
            }
            if (finishReason == null) {
                this.finishReason = piece.getFinishReason();
            }
            if (content != null) {
                this.contentBuffer.append(content);
            }
            if (reasoningContent != null) {
                this.reasoningContentBuffer.append(reasoningContent);
            }
            if (toolCalls != null) {
                for (int i = 0; i < toolCalls.size(); i++) {
                    ToolCall toolCall = toolCalls.get(i);
                    tcMap.computeIfAbsent(i, x -> new ToolCallStreamPieceCollector())
                            .accept(toolCall);
                }
            }
        }

        @Override
        public MixChatMessage build() {
            MixChatMessage message = MixChatMessage.create();
            message.setRole(role);
            message.setTextContent(contentBuffer.toString());
            message.setReasoningContent(reasoningContentBuffer.toString());
            message.setIndex(index);
            message.setFinishReason(finishReason);
            return message;
        }
    }
}
