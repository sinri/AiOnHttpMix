package io.github.sinri.AiOnHttpMix.utils.models.volces.deepseek;

import io.github.sinri.AiOnHttpMix.utils.models.volces.VolcesModelSeries;

public abstract class VolcesDeepSeekModelSeries extends VolcesModelSeries {
    public static final String MODEL_NAME_OF_DEEPSEEK_V3_241226 = "deepseek-v3-241226";
    public static final String MODEL_NAME_OF_DEEPSEEK_V3_250324 = "deepseek-v3-250324";

    public static final String MODEL_NAME_OF_DEEPSEEK_R1_250120 = "deepseek-r1-250120";

    public static class Builder implements ChatModelBuilder<VolcesDeepSeekModelSeries> {

        @Override
        public VolcesDeepSeekModelSeries build(String modelName) {
            return new VolcesDeepSeekModelSeries() {
                @Override
                public String getModelName() {
                    return modelName;
                }
            };
        }
    }
}
