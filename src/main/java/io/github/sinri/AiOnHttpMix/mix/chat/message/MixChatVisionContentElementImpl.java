package io.github.sinri.AiOnHttpMix.mix.chat.message;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class MixChatVisionContentElementImpl extends JsonifiableEntityImpl<MixChatVisionContentElement> implements MixChatVisionContentElement {
    public MixChatVisionContentElementImpl() {
        super();
    }

    public MixChatVisionContentElementImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public MixChatVisionContentElement getImplementation() {
        return this;
    }

    @Override
    public ContentElementType getType() {
        return ContentElementType.valueOf(readString("type"));
    }

    @Override
    public MixChatVisionContentElement setType(ContentElementType contentElementType) {
        this.write("type", contentElementType.name());
        return this;
    }

    @Override
    public String getText() {
        return readString("text");
    }

    @Override
    public MixChatVisionContentElement setText(String text) {
        setType(ContentElementType.text);
        this.write("text", text);
        return this;
    }

    @Override
    public String getImage() {
        return readString("image");
    }

    @Override
    public MixChatVisionContentElement setImage(String image) {
        setType(ContentElementType.image);
        return this.write("image", image);
    }
}
