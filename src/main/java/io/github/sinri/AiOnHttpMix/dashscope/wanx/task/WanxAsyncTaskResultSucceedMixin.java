package io.github.sinri.AiOnHttpMix.dashscope.wanx.task;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;

public interface WanxAsyncTaskResultSucceedMixin extends UnmodifiableJsonifiableEntity {
    default Integer getImageCount() {
        return readInteger("usage", "image_count");
    }
}
