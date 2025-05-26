package io.github.sinri.AiOnHttpMix.provider.dashscope.wanx.ImageSynthesis.request;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;

import javax.annotation.Nonnull;

class WanxImageSynthesisParametersImpl extends JsonifiableEntityImpl<WanxImageSynthesisParameters> implements WanxImageSynthesisParameters {

    @Nonnull
    @Override
    public WanxImageSynthesisParameters getImplementation() {
        return this;
    }
}
