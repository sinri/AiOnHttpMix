package io.github.sinri.AiOnHttpMix.utils.models.volces.doubao;

import io.github.sinri.AiOnHttpMix.utils.models.volces.VolcesChatModelSeries;

/**
 * 火山引擎提供的豆包系列模型，支持视觉理解能力。
 *
 * @since 2.0.0
 */
public abstract class DoubaoVisionModelSeries extends VolcesChatModelSeries {
    public final static String MODEL_NAME_OF_DOUBAO_1d5_VISION_PRO_32K_250115 = "doubao-1.5-vision-pro-32k-250115";

    public static DoubaoVisionModelSeries model(String modelName) {
        return new DoubaoVisionModelSeries() {
            @Override
            public String getModelName() {
                return modelName;
            }
        };
    }
}
