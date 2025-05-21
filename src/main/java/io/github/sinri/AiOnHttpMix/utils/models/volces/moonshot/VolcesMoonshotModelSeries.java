package io.github.sinri.AiOnHttpMix.utils.models.volces.moonshot;

import io.github.sinri.AiOnHttpMix.utils.models.volces.VolcesModelSeries;

/**
 * 火山引擎提供的Moonshot系列模型，不支持推理。
 *
 * @since 2.0.0
 */
public abstract class VolcesMoonshotModelSeries extends VolcesModelSeries {
    public static final String MODEL_NAME_OF_MOONSHOT_V1_8k = "moonshot-v1-8k";
    public static final String MODEL_NAME_OF_MOONSHOT_V1_32k = "moonshot-v1-32k";
    public static final String MODEL_NAME_OF_MOONSHOT_V1_128k = "moonshot-v1-128k";

    public static VolcesMoonshotModelSeries model(String modelName) {
        return new VolcesMoonshotModelSeries() {
            @Override
            public String getModelName() {
                return modelName;
            }
        };
    }
}
