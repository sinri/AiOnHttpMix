package io.github.sinri.AiOnHttpMix.utils.models.dashscope.qwen;

/**
 * 表示Qwen-Plus聊天模型的实现。
 * 该类为单例，名称为"qwen-plus"。
 *
 * @since 2.0.0
 */
@Deprecated(forRemoval = true)
public final class QwenPlusModel extends QwenModelSeries {
    /**
     * 模型名称常量。
     */
    public final static String MODEL_NAME = "qwen-plus";

    QwenPlusModel() {

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
