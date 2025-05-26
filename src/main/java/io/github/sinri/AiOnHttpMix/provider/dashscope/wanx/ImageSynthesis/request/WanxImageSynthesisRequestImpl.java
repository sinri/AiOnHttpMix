package io.github.sinri.AiOnHttpMix.provider.dashscope.wanx.ImageSynthesis.request;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;

import javax.annotation.Nonnull;

class WanxImageSynthesisRequestImpl extends JsonifiableEntityImpl<WanxImageSynthesisRequest> implements WanxImageSynthesisRequest {

    @Nonnull
    @Override
    public WanxImageSynthesisRequest getImplementation() {
        return this;
    }
}
