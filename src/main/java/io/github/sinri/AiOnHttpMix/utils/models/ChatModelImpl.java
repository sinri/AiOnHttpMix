package io.github.sinri.AiOnHttpMix.utils.models;

import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;

class ChatModelImpl implements ChatModel {
    private final ModelSpecification specification;
    private final String modelName;

    public ChatModelImpl(ModelSpecification specification, String modelName) {
        this.specification = specification;
        this.modelName = modelName;
    }

    @Override
    public String getModelName() {
        return modelName;
    }

    @Override
    public ModelSpecification getSpecification() {
        return specification;
    }
}
