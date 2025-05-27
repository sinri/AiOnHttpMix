package io.github.sinri.AiOnHttpMix.utils.models.volces.doubao;

import io.github.sinri.AiOnHttpMix.utils.models.volces.VolcesTextModelSeries;

/**
 * 火山引擎提供的豆包系列模型，部分支持推理。
 *
 * @since 2.0.0
 */
public abstract class DoubaoTextModelSeries extends VolcesTextModelSeries {
    public final static String NAME_OF_MODEL_SERIES = "DoubaoChatModelSeries";


    public final static String MODEL_NAME_OF_DOUBAO_PRO_32K = "doubao-pro-32k";
    public final static String MODEL_NAME_OF_DOUBAO_PRO_128K = "doubao-pro-128k";
    public final static String MODEL_NAME_OF_DOUBAO_PRO_256K = "doubao-pro-256k";
    public final static String MODEL_NAME_OF_DOUBAO_1D5_THINKING_PRO_250415 = "doubao-1.5-thinking-pro-250415";

    public static DoubaoTextModelSeries model(String modelName) {
        return new DoubaoTextModelSeries() {
            @Override
            public String getModelName() {
                return modelName;
            }
        };
    }
}
