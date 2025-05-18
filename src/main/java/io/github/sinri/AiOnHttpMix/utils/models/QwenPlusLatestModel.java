package io.github.sinri.AiOnHttpMix.utils.models;

/**
 * 表示Qwen-Plus-Latest聊天模型的实现。
 * 该类为单例，名称为"qwen-plus-latest"。
 */
public final class QwenPlusLatestModel extends QwenModelSeries {
    /**
     * 模型名称常量。
     */
    public final static String MODEL_NAME = "qwen-plus-latest";

    QwenPlusLatestModel() {

    }

    /**
     * 获取模型名称。
     *
     * @return 模型名称
     */
    @Override
    public String getModelName() {
        return MODEL_NAME;
    }
}
