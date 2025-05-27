package io.github.sinri.AiOnHttpMix.utils.models.volces.doubao;

import io.github.sinri.AiOnHttpMix.utils.models.volces.VolcesVisionModelSeries;

/**
 * 火山引擎提供的豆包系列模型，支持视觉理解能力。
 *
 * @since 2.0.0
 */
public abstract class DoubaoVisionModelSeries extends VolcesVisionModelSeries {
    public final static String NAME_OF_MODEL_SERIES = "DoubaoVisionModelSeries";

    public final static String MODEL_NAME_OF_DOUBAO_1d5_VISION_PRO_32K_250115 = "doubao-1.5-vision-pro-32k-250115";
    public final static String MODEL_NAME_OF_DOUBAO_1d5_THINKING_VISION_PRO_250428 = "doubao-1.5-thinking-vision-pro-250428";

    public static DoubaoVisionModelSeries model(String modelName) {
        return new DoubaoVisionModelSeries() {
            @Override
            public String getModelName() {
                return modelName;
            }
        };
    }

}
