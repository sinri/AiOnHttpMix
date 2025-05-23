package io.github.sinri.AiOnHttpMix.utils.models.mix;

import io.github.sinri.AiOnHttpMix.utils.models.VisionModel;
import io.github.sinri.AiOnHttpMix.utils.specification.MixModelSpecification;

@Deprecated
public class MixVisionModel extends MixModelSpecification implements VisionModel {
    @Override
    public String getModelName() {
        return "MixVisionModel";
    }
}
