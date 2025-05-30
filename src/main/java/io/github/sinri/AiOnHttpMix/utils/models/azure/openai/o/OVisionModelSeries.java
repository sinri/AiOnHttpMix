package io.github.sinri.AiOnHttpMix.utils.models.azure.openai.o;

import io.github.sinri.AiOnHttpMix.utils.models.VisionModel;
import io.github.sinri.AiOnHttpMix.utils.specification.OModelSpecification;

/**
 * @since 2.0.1
 */
public abstract class OVisionModelSeries extends OModelSpecification implements VisionModel {
    public final static String NAME_OF_MODEL_SERIES = "OVisionModelSeries";

    public final static String MODEL_NAME_OF_O1 = "o1";
    public final static String MODEL_NAME_OF_O3 = "o3";
    public final static String MODEL_NAME_OF_O4_MINI = "o4-mini";

    public static OVisionModelSeries model(String modelName) {
        return new OVisionModelSeries() {
            @Override
            public String getModelName() {
                return modelName;
            }
        };
    }

    @Override
    public boolean isWithVisionAbility() {
        return true;
    }
}
