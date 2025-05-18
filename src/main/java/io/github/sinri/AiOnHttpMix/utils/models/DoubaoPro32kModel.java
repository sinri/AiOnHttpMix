package io.github.sinri.AiOnHttpMix.utils.models;

/**
 * 表示豆包Pro-32k聊天模型的实现。
 * 该类为单例，名称为"doubao-pro-32k"。
 */
public final class DoubaoPro32kModel extends DoubaoModelSeries {
    /**
     * 模型名称常量。
     */
    public final static String MODEL_NAME = "doubao-pro-32k";

    DoubaoPro32kModel() {

    }

    @Override
    public String getModelName() {
        return MODEL_NAME;
    }


}
