package io.github.sinri.AiOnHttpMix.utils.models;

import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;

public abstract class OpenAIOModelSeries implements ChatModel {


    @Override
    public ModelSpecification getSpecification() {
        return ModelSpecification.o;
    }
}
