package io.github.sinri.AiOnHttpMix.mix.vl;

import io.github.sinri.AiOnHttpMix.mix.AnyLLMRole;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 1.3.0
 */
class AnyVLLMResponseImpl implements AnyVLLMResponse {
    private final AnyLLMRole role;
    private final List<AnyVLLMMessageComponent> content;

    public AnyVLLMResponseImpl(AnyLLMRole role) {
        this.role = role;
        this.content = new ArrayList<>();
    }

    public AnyVLLMResponseImpl addContentComponent(AnyVLLMMessageComponent component) {
        this.content.add(component);
        return this;
    }

    @Override
    public AnyLLMRole getRole() {
        return role;
    }

    @Override
    public List<AnyVLLMMessageComponent> getContent() {
        return content;
    }
}
