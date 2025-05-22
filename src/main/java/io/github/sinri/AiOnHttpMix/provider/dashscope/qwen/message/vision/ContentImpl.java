package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.message.vision;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class ContentImpl extends JsonifiableEntityImpl<Content> implements Content {
    private String text;
    private String image;
    private Boolean enable_rotate;
    private String video;
    private Float fps;
    private String audio;
    private Integer min_pixels;
    private Integer max_pixels;

    public ContentImpl() {
        super();
    }

    public ContentImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public Content getImplementation() {
        return this;
    }
}
