package io.github.sinri.AiOnHttpMix.utils.models;

import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;

public abstract class VolcesModelSeries implements ChatModel {
    @Override
    final public ModelSpecification getSpecification() {
        return ModelSpecification.volces;
    }

}
