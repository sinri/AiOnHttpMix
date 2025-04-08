package io.github.sinri.AiOnHttpMix.mix.vl;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.vl.QwenVLRequest;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMRole;
import io.github.sinri.AiOnHttpMix.volces.v3.request.VolcesChatRequest;

import java.util.List;
import java.util.UUID;

/**
 * @since 1.3.0
 */
public interface AnyVLLMRequest {
    static AnyVLLMRequest create() {
        return create(UUID.randomUUID().toString());
    }

    static AnyVLLMRequest create(String requestId) {
        return new AnyVLLMRequestImpl(requestId);
    }

    String getRequestId();

    AnyVLLMRequest addMessage(AnyLLMRole role, List<AnyVLLMMessageComponent> components);

    /**
     * @since 1.2.2
     */
    int getMaxExecutionSeconds();

    /**
     * @since 1.2.2
     */
    AnyVLLMRequest setMaxExecutionSeconds(int maxExecutionSeconds);

    QwenVLRequest toQwenRequest();

    VolcesChatRequest toVolcesRequest();
}
