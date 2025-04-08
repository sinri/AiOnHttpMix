package io.github.sinri.AiOnHttpMix.mix.vl;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.vl.QwenVLInputMessage;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.vl.QwenVLMessageContentItem;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.vl.QwenVLRequest;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMRole;
import io.github.sinri.AiOnHttpMix.volces.v3.request.VolcesChatRequest;
import io.github.sinri.AiOnHttpMix.volces.v3.visual.VolcesVisualChatMessageContent;
import io.github.sinri.AiOnHttpMix.volces.v3.visual.VolcesVisualChatMessageContentImageMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 1.3.0
 */
class AnyVLLMRequestImpl implements AnyVLLMRequest {
    private final String requestId;
    private final List<AnyVLLMRoleMessagePair> messages;
    private int maxExecutionSeconds = 30;

    public AnyVLLMRequestImpl(String requestId) {
        this.requestId = requestId;
        messages = new ArrayList<>();
    }

    @Override
    public String getRequestId() {
        return requestId;
    }

    @Override
    public AnyVLLMRequest addMessage(AnyLLMRole role, List<AnyVLLMMessageComponent> components) {
        messages.add(new AnyVLLMRoleMessagePair(role, components));
        return this;
    }

    @Override
    public int getMaxExecutionSeconds() {
        return maxExecutionSeconds;
    }

    @Override
    public AnyVLLMRequest setMaxExecutionSeconds(int maxExecutionSeconds) {
        this.maxExecutionSeconds = maxExecutionSeconds;
        return this;
    }

    @Override
    public QwenVLRequest toQwenRequest() {
        QwenVLRequest request = QwenVLRequest.create();
        request.handleInput(input -> {
            messages.forEach(pair -> {
                QwenVLInputMessage x = QwenVLInputMessage.create();
                x.setRole(pair.role().toQwenRole().toQwenVLRole());
                pair.content().forEach(c -> {
                    QwenVLMessageContentItem p = QwenVLMessageContentItem.create();
                    if (c.type() == AnyVLLMMessageComponentType.text) {
                        p.setText(c.value());
                    } else if (c.type() == AnyVLLMMessageComponentType.image) {
                        p.setImage(c.value());
                    }
                    x.addContentItem(p);
                });
                input.addMessage(x);
            });
        });
        return request;
    }

    @Override
    public VolcesChatRequest toVolcesRequest() {
        VolcesChatRequest request = VolcesChatRequest.create();
        messages.forEach(pair -> {
            request.addMessage(volcesRequest -> {
                volcesRequest.setRole(pair.role().toVolcesChatRole());

                List<VolcesVisualChatMessageContent> visualContents = new ArrayList<>();
                pair.content().forEach(item -> {
                    var p = VolcesVisualChatMessageContent.create();
                    if (item.type() == AnyVLLMMessageComponentType.text) {
                        p.setText(item.value());
                    } else if (item.type() == AnyVLLMMessageComponentType.image) {
                        p.setImageMeta(VolcesVisualChatMessageContentImageMeta.create()
                                                                              .setImage(item.value())
                        );
                    } else {
                        throw new IllegalArgumentException();
                    }
                    visualContents.add(p);
                });
                volcesRequest.setVisualContent(visualContents);
            });
        });
        return request;
    }
}
