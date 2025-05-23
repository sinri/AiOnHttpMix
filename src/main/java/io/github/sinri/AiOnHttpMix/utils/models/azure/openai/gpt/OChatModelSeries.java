package io.github.sinri.AiOnHttpMix.utils.models.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.specification.OModelSpecification;

/**
 * Azure OpenAI O系列模型，支持推理。
 *
 * @since 2.0.0
 */
public abstract class OChatModelSeries extends OModelSpecification implements ChatModel {
    public final static String NAME_OF_MODEL_SERIES = "OChatModelSeries";

    public final static String MODEL_NAME_OF_O1 = "o1";
    public final static String MODEL_NAME_OF_O3_MINI = "o3-mini";

    public static OChatModelSeries model(String modelName) {
        return new OChatModelSeries() {
            @Override
            public String getModelName() {
                return modelName;
            }
        };
    }
}
