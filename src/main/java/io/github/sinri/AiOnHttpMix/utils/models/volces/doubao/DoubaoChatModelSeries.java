package io.github.sinri.AiOnHttpMix.utils.models.volces.doubao;

import io.github.sinri.AiOnHttpMix.utils.models.volces.VolcesChatModelSeries;

/**
 * 火山引擎提供的豆包系列模型，部分支持推理。
 *
 * @since 2.0.0
 */
public abstract class DoubaoChatModelSeries extends VolcesChatModelSeries {
    public final static String MODEL_NAME_OF_DOUBAO_PRO_32K = "doubao-pro-32k";
    public final static String MODEL_NAME_OF_DOUBAO_1D5_THINKING_PRO_250415 = "doubao-1.5-thinking-pro-250415";

    public static DoubaoChatModelSeries model(String modelName) {
        return new DoubaoChatModelSeries() {
            @Override
            public String getModelName() {
                return modelName;
            }
        };
    }
}
