package io.github.sinri.AiOnHttpMix.utils.models.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;

public abstract class OModelSeries implements ChatModel {
    public final static String MODEL_NAME_OF_O1 = "o1";

    @Override
    public ModelSpecification getSpecification() {
        return ModelSpecification.o;
    }

    public static class Builder implements ChatModelBuilder<OModelSeries> {
        @Override
        public OModelSeries build(String modelName) {
            return new OModelSeries() {
                @Override
                public String getModelName() {
                    return modelName;
                }
            };
        }
    }
}
