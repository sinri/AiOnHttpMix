package io.github.sinri.AiOnHttpMix.utils.models.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.utils.models.TextModel;
import io.github.sinri.AiOnHttpMix.utils.specification.OModelSpecification;

/**
 * Azure OpenAI O系列模型，支持推理。
 *
 * @since 2.0.0
 */
public abstract class OTextModelSeries extends OModelSpecification implements TextModel {
    public final static String NAME_OF_MODEL_SERIES = "OTextModelSeries";

    public final static String MODEL_NAME_OF_O1 = "o1";
    public final static String MODEL_NAME_OF_O3_MINI = "o3-mini";

    public static OTextModelSeries model(String modelName) {
        return new OTextModelSeries() {
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
