package io.github.sinri.AiOnHttpMix.utils.vision.openai;

import io.github.sinri.keel.core.json.JsonifiableEntity;

public interface OpenAICompatibleVisionContentForImageUrl<C> extends JsonifiableEntity<C> {
    default String getUrl() {
        return readString("url");
    }

    /**
     * @param url 支持传入图片链接或图片的Base64编码，不同模型支持图片大小略有不同，具体请参见使用说明。<br>
     *            传入图片URL：传入图片的可访问链接，推荐使用 TOS（火山引擎对象存储） 存储图片，并生成图片链接。<br>
     *            传入Base64编码：请遵循格式 {@code data:image/[图片格式];base64,[Base64编码]}，可见示例。<br>
     * @see <a href="https://www.volcengine.com/docs/82379/1362931#%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E">使用说明</a>
     * @see <a
     *         href="https://www.volcengine.com/docs/82379/1362931#base64-%E7%BC%96%E7%A0%81%E8%BE%93%E5%85%A5">示例</a>
     */
    default C setUrl(String url) {
        return write("url", url);
    }

}
