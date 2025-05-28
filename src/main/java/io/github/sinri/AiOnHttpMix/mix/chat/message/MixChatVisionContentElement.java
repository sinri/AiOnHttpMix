package io.github.sinri.AiOnHttpMix.mix.chat.message;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.vision.GPTVisionMessageContent;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.vision.GPTVisionMessageContentImageUrl;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.message.vision.QwenVisionContent;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.vision.ContentImageUrl;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.vision.DoubaoVisionContent;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;

/**
 * MixChatVisionContentElement 接口用于统一抽象多模态（文本与图像）聊天内容元素，
 * 以便在不同大模型（如 OpenAI GPT、阿里 Qwen、火山 Doubao 等）之间进行内容适配和转换。
 * <p>
 * 该接口支持文本和图片两种内容类型，并提供了转换为各大模型专用内容对象的方法。
 * </p>
 *
 * <ul>
 *   <li>通过 {@link #getType()} 获取内容类型（文本或图片）。</li>
 *   <li>通过 {@link #getText()} 和 {@link #getImage()} 获取具体内容。</li>
 *   <li>通过 {@link #toGPTVisionContent()}、{@link #toQwenVisionContent()}、{@link #toDoubaoVisionContent()} 实现内容适配。</li>
 * </ul>
 *
 * @author sinri
 */
public interface MixChatVisionContentElement extends JsonifiableEntity<MixChatVisionContentElement> {

    /**
     * 创建一个空的多模态内容元素实例。
     * @return 新的 MixChatVisionContentElement 实例
     */
    static MixChatVisionContentElement create() {
        return new MixChatVisionContentElementImpl();
    }

    /**
     * 通过 JsonObject 包装生成多模态内容元素实例。
     * @param jsonObject JSON 对象
     * @return MixChatVisionContentElement 实例
     */
    static MixChatVisionContentElement wrap(JsonObject jsonObject) {
        return new MixChatVisionContentElementImpl(jsonObject);
    }

    /**
     * 获取内容元素类型（文本或图片）。
     * @return 内容元素类型
     */
    ContentElementType getType();

    /**
     * 设置内容元素类型。
     * @param contentElementType 内容元素类型
     * @return 当前对象自身，便于链式调用
     */
    MixChatVisionContentElement setType(ContentElementType contentElementType);

    /**
     * 获取文本内容。
     * @return 文本内容字符串
     */
    String getText();

    /**
     * 设置文本内容。
     * @param text 文本内容字符串
     * @return 当前对象自身，便于链式调用
     */
    MixChatVisionContentElement setText(String text);

    /**
     * 获取图片内容（通常为图片 URL）。
     * @return 图片 URL 字符串
     */
    String getImage();

    /**
     * 设置图片内容（通常为图片 URL）。
     * @param image 图片 URL 字符串
     * @return 当前对象自身，便于链式调用
     */
    MixChatVisionContentElement setImage(String image);

    /**
     * 转换为 OpenAI GPT Vision 专用内容对象。
     * @return GPTVisionMessageContent 对象
     */
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

    /**
     * 转换为阿里 Qwen Vision 专用内容对象。
     * @return QwenVisionContent 对象
     */
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

    /**
     * 转换为火山 Doubao Vision 专用内容对象。
     * @return DoubaoVisionContent 对象
     */
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

    /**
     * 内容元素类型枚举，支持文本和图片。
     */
    enum ContentElementType {
        text, image
    }
}
