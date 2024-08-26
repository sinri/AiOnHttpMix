package io.github.sinri.AiOnHttpMix.azure.openai.core.mixin.filter;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;

public interface ContentFilterDetectedResult extends UnmodifiableJsonifiableEntity {
    default Boolean isFiltered() {
        return readBoolean("filtered");
    }

    default Boolean isDetected() {
        return readBoolean("detected");
    }
}
