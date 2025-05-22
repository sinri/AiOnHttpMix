package io.github.sinri.AiOnHttpMix.utils.models.dashscope.qwen;

public abstract non-sealed class QwenVisionModelSeries extends QwenModelSeries {
    public final static String MODEL_NAME_OF_QWEN_VL_MAX = "qwen-vl-max";
    public final static String MODEL_NAME_OF_QWEN_VL_MAX_LATEST = "qwen-vl-max-latest";
    public final static String MODEL_NAME_OF_QWEN_VL_PLUS = "qwen-vl-plus";
    public final static String MODEL_NAME_OF_QWEN_VL_PLUS_LATEST = "qwen-vl-plus-latest";

    public static QwenVisionModelSeries model(String modelName) {
        return new QwenVisionModelSeries() {
            @Override
            public String getModelName() {
                return modelName;
            }
        };
    }
}
