package io.github.sinri.AiOnHttpMix.mix;

import java.util.List;

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
