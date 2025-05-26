package io.github.sinri.AiOnHttpMix.provider.dashscope.wanx.ImageSynthesis.response;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class WanxImageSynthesisAsyncTaskCreateResultImpl extends UnmodifiableJsonifiableEntityImpl implements WanxImageSynthesisAsyncTaskCreateResult {
    public WanxImageSynthesisAsyncTaskCreateResultImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
