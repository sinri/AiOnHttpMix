package io.github.sinri.AiOnHttpMix.dashscope.wanx.ImageSynthesis.response;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class WanxImageSynthesisAsyncTaskCreateResultImpl extends UnmodifiableJsonifiableEntityImpl implements WanxImageSynthesisAsyncTaskCreateResult {
    public WanxImageSynthesisAsyncTaskCreateResultImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
