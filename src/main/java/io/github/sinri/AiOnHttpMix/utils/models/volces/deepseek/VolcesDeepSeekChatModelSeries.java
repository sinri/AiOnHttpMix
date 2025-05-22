package io.github.sinri.AiOnHttpMix.utils.models.volces.deepseek;

import io.github.sinri.AiOnHttpMix.utils.models.volces.VolcesChatModelSeries;

/**
 * 火山引擎提供的DeepSeek系列模型，部分支持推理。
 *
 * @since 2.0.0
 */
public abstract class VolcesDeepSeekChatModelSeries extends VolcesChatModelSeries {
    public static final String MODEL_NAME_OF_DEEPSEEK_V3_241226 = "deepseek-v3-241226";
    public static final String MODEL_NAME_OF_DEEPSEEK_V3_250324 = "deepseek-v3-250324";

    public static final String MODEL_NAME_OF_DEEPSEEK_R1_250120 = "deepseek-r1-250120";

    public static VolcesDeepSeekChatModelSeries model(String modelName) {
        return new VolcesDeepSeekChatModelSeries() {
            @Override
            public String getModelName() {
                return modelName;
            }
        };
    }
}
