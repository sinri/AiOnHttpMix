package io.github.sinri.AiOnHttpMix.utils.models.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.specification.OModelSpecification;

public abstract class OModelSeries extends OModelSpecification implements ChatModel {
    public final static String MODEL_NAME_OF_O1 = "o1";

    public static OModelSeries model(String modelName) {
        return new OModelSeries() {
            @Override
            public String getModelName() {
                return modelName;
            }
        };
    }
}
