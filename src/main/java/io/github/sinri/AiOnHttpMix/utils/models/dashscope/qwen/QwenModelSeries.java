package io.github.sinri.AiOnHttpMix.utils.models.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;

public abstract class QwenModelSeries implements ChatModel {
    public final static String MODEL_NAME_OF_QWEN_PLUS = "qwen-plus";
    public final static String MODEL_NAME_OF_QWEN_PLUS_LATEST = "qwen-plus-latest";

    @Override
    final public ModelSpecification getSpecification() {
        return ModelSpecification.qwen;
    }

    public static class Builder implements ChatModelBuilder<QwenModelSeries> {

        @Override
        public QwenModelSeries build(String modelName) {
            return new QwenModelSeries() {
                @Override
                public String getModelName() {
                    return modelName;
                }
            };
        }
    }
}
