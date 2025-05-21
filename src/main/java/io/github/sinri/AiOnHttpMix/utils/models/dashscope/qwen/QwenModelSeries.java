package io.github.sinri.AiOnHttpMix.utils.models.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.specification.DashscopeModelSpecification;

public abstract class QwenModelSeries extends DashscopeModelSpecification implements ChatModel {
    public final static String MODEL_NAME_OF_QWEN_PLUS = "qwen-plus";
    public final static String MODEL_NAME_OF_QWEN_PLUS_LATEST = "qwen-plus-latest";

    public static QwenModelSeries model(String modelName) {
        return new QwenModelSeries() {
            @Override
            public String getModelName() {
                return modelName;
            }
        };
    }
}
