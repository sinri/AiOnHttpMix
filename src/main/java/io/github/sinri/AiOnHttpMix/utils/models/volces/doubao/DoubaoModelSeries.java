package io.github.sinri.AiOnHttpMix.utils.models.volces.doubao;

import io.github.sinri.AiOnHttpMix.utils.models.volces.VolcesModelSeries;

/**
 * Volces体系下Doubao大模型系列
 */
public abstract class DoubaoModelSeries extends VolcesModelSeries {
    public final static String MODEL_NAME_OF_DOUBAO_PRO_32K = "doubao-pro-32k";
    public final static String MODEL_NAME_OF_DOUBAO_1D5_THINKING_PRO_250415 = "doubao-1.5-thinking-pro-250415";

    public static class Builder implements ChatModelBuilder<DoubaoModelSeries> {

        @Override
        public DoubaoModelSeries build(String modelName) {
            return new DoubaoModelSeries() {
                @Override
                public String getModelName() {
                    return modelName;
                }
            };
        }
    }
}
