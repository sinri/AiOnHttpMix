package io.github.sinri.AiOnHttpMix.utils.models.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.utils.models.VisionModel;
import io.github.sinri.AiOnHttpMix.utils.specification.GPTModelSpecification;

/**
 * Azure OpenAI GPT系列模型，不支持推理，支持视觉理解。
 *
 * @since 2.0.0
 */
public abstract class GPTVisionModelSeries extends GPTModelSpecification implements VisionModel {
    public final static String NAME_OF_MODEL_SERIES = "GPTVisionModelSeries";
    public final static String MODEL_NAME_OF_GPT_4O = "gpt-4o";
    /**
     * @since 2.0.1
     */
    public final static String MODEL_NAME_OF_GPT_4dot1 = "gpt-4dot1";
    /**
     * @since 2.0.1
     */
    public final static String MODEL_NAME_OF_GPT_4dot1_MINI = "gpt-4dot1-mini";
    /**
     * @since 2.0.1
     */
    public final static String MODEL_NAME_OF_GPT_4dot1_NANO = "gpt-4dot1-nano";

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
