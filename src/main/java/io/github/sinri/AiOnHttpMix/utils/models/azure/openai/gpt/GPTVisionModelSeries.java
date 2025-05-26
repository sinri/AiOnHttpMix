package io.github.sinri.AiOnHttpMix.utils.models.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.utils.models.VisionModel;
import io.github.sinri.AiOnHttpMix.utils.specification.GPTModelSpecification;

public abstract class GPTVisionModelSeries extends GPTModelSpecification implements VisionModel {
    public final static String NAME_OF_MODEL_SERIES = "GPTVisionModelSeries";
    public final static String MODEL_NAME_OF_GPT_4O = "gpt-4o";

    public static GPTVisionModelSeries model(String modelName) {
        return new GPTVisionModelSeries() {
            @Override
            public String getModelName() {
                return modelName;
            }
        };
    }

    @Override
    public boolean isWithVisionAbility() {
        return true;
    }
}
