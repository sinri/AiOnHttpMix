package io.github.sinri.AiOnHttpMix.mix.chat.message;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.vision.GPTVisionMessageContent;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.vision.GPTVisionMessageContentImageUrl;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.message.vision.QwenVisionContent;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.vision.ContentImageUrl;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.vision.DoubaoVisionContent;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface MixChatVisionContentElement extends JsonifiableEntity<MixChatVisionContentElement> {

    static MixChatVisionContentElement create() {
        return new MixChatVisionContentElementImpl();
    }

    static MixChatVisionContentElement wrap(JsonObject jsonObject) {
        return new MixChatVisionContentElementImpl(jsonObject);
    }

    ContentElementType getType();

    MixChatVisionContentElement setType(ContentElementType contentElementType);

    String getText();

    MixChatVisionContentElement setText(String text);

    String getImage();

    MixChatVisionContentElement setImage(String image);

    default GPTVisionMessageContent toGPTVisionContent() {
        GPTVisionMessageContent content = GPTVisionMessageContent.create();
        ContentElementType type = getType();
        switch (type) {
            case text:
                content.setText(getText());
                break;
            case image:
                content.setImageUrl(GPTVisionMessageContentImageUrl.create()
                                                                   .setUrl(getImage()));
                break;
        }
        return content;
    }

    default QwenVisionContent toQwenVisionContent() {
        QwenVisionContent content = QwenVisionContent.create();
        ContentElementType type = getType();
        switch (type) {
            case text:
                content.setText(getText());
                break;
            case image:
                content.setImage(getImage());
                break;
        }
        return content;
    }

    default DoubaoVisionContent toDoubaoVisionContent() {
        DoubaoVisionContent content = DoubaoVisionContent.create();
        ContentElementType type = getType();
        switch (type) {
            case text:
                content.setText(getText());
                break;
            case image:
                content.setImageUrl(ContentImageUrl.create().setUrl(getImage()));
                break;
        }
        return content;
    }

    enum ContentElementType {
        text, image
    }
}
