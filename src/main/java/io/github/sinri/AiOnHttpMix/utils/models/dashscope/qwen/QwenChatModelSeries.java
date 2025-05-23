package io.github.sinri.AiOnHttpMix.utils.models.dashscope.qwen;

/**
 * Dashscope下的通义千问（Qwen）系列模型，部分支持推理。
 *
 * @since 2.0.0
 */
public abstract non-sealed class QwenChatModelSeries extends QwenModelSeries {
    public final static String NAME_OF_MODEL_SERIES = "QwenChatModelSeries";

    public final static String MODEL_NAME_OF_QWEN_PLUS = "qwen-plus";
    public final static String MODEL_NAME_OF_QWEN_PLUS_LATEST = "qwen-plus-latest";
    public final static String MODEL_NAME_OF_QWEN_MAX = "qwen-max";
    public final static String MODEL_NAME_OF_QWEN_MAX_LATEST = "qwen-max-latest";
    public final static String MODEL_NAME_OF_QWEN_TURBO = "qwen-turbo";
    public final static String MODEL_NAME_OF_QWEN_TURBO_LATEST = "qwen-turbo-latest";
    public final static String MODEL_NAME_OF_QWEN_LONG = "qwen-long";
    public final static String MODEL_NAME_OF_QWEN_LONG_LATEST = "qwen-long-latest";
    /**
     * 通义千问翻译模型
     */
    public final static String MODEL_NAME_OF_QWEN_MT_PLUS = "qwen-mt-plus";
    /**
     * 通义千问翻译模型（贫穷版）
     */
    public final static String MODEL_NAME_OF_QWEN_MT_TURBO = "qwen-mt-turbo";

    public static QwenChatModelSeries model(String modelName) {
        return new QwenChatModelSeries() {
            @Override
            public String getModelName() {
                return modelName;
            }
        };
    }
}
