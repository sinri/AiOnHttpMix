package io.github.sinri.AiOnHttpMix.utils.models.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.utils.models.TextModel;
import io.github.sinri.AiOnHttpMix.utils.specification.GPTModelSpecification;

/**
 * Azure OpenAI GPT系列模型，不支持推理。
 *
 * @since 2.0.0
 */
public abstract class GPTTextModelSeries extends GPTModelSpecification implements TextModel {
    public final static String NAME_OF_MODEL_SERIES = "GPTTextModelSeries";
    public final static String MODEL_NAME_OF_GPT_4 = "gpt-4";

    public static GPTTextModelSeries model(String modelName) {
        return new GPTTextModelSeries() {
            @Override
            public String getModelName() {
                return modelName;
            }
        };
    }

    @Override
    public boolean isWithVisionAbility() {
        return false;
    }
}
