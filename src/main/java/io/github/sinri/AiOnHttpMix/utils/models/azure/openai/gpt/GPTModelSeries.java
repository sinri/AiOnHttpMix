package io.github.sinri.AiOnHttpMix.utils.models.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.specification.GPTModelSpecification;

/**
 * Azure OpenAI GPT系列模型，不支持推理。
 *
 * @since 2.0.0
 */
public abstract class GPTModelSeries extends GPTModelSpecification implements ChatModel {
    public final static String MODEL_NAME_OF_GPT_4O = "gpt-4o";

    public static GPTModelSeries model(String modelName) {
        return new GPTModelSeries() {
            @Override
            public String getModelName() {
                return modelName;
            }
        };
    }
}
