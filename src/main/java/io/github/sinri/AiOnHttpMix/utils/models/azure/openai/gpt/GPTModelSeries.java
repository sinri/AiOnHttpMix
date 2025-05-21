package io.github.sinri.AiOnHttpMix.utils.models.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;

public abstract class GPTModelSeries implements ChatModel {
    public final static String MODEL_NAME_OF_GPT_4O = "gpt-4o";
    @Override
    final public ModelSpecification getSpecification() {
        return ModelSpecification.gpt;
    }

    public static class Builder implements ChatModelBuilder<GPTModelSeries> {
        @Override
        public GPTModelSeries build(String modelName) {
            return new GPTModelSeries() {
                @Override
                public String getModelName() {
                    return modelName;
                }
            };
        }
    }
}
