package io.github.sinri.AiOnHttpMix.mix;

import java.util.List;

/**
 * @since 1.1.0
 */
class AnyLLMResponseImpl implements AnyLLMResponse {

    List<AnyLLMResponseChoice> choices;

    public AnyLLMResponseImpl(List<AnyLLMResponseChoice> choices) {
        this.choices = choices;
    }

    @Override
    public List<AnyLLMResponseChoice> getChoices() {
        return choices;
    }
}
