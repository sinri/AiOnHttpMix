package io.github.sinri.AiOnHttpMix.utils.models;

import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;

public abstract class QwenModelSeries implements ChatModel {
    @Override
    final public ModelSpecification getSpecification() {
        return ModelSpecification.qwen;
    }
}
