package io.github.sinri.AiOnHttpMix.dashscope.wanx.ImageSynthesis.request;

import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

public interface WanxImageSynthesisInput extends JsonifiableEntity<WanxImageSynthesisInput> {
    static WanxImageSynthesisInput create() {
        return new WanxImageSynthesisInputImpl();
    }

    static WanxImageSynthesisInput wrap(JsonObject jsonObject) {
        return new WanxImageSynthesisInputImpl().reloadDataFromJsonObject(jsonObject);
    }

    @Nullable
    default String getPrompt() {
        return this.readString("prompt");
    }

    /**
     * @param prompt 描述画面的提示词信息。支持中英文，长度不超过500个字符，超过部分会自动截断。
     */
    default WanxImageSynthesisInput setPrompt(String prompt) {
        this.toJsonObject().put("prompt", prompt);
        return this;
    }

    @Nullable
    default String getNegativePrompt() {
        return this.readString("negative_prompt");
    }

    /**
     * @param prompt 画面中不想出现的内容描述词信息。支持中英文，长度不超过500个字符，超过部分会自动截断。 Optional.
     */
    default WanxImageSynthesisInput setNegativePrompt(String prompt) {
        this.toJsonObject().put("negative_prompt", prompt);
        return this;
    }

    @Nullable
    default String getRefImg() {
        return this.readString("ref_img");
    }

    /**
     * @param url 输入参考图像的URL；图片格式可为 jpg，png，tiff，webp等常见位图格式。默认为空。 Optional.
     */
    default WanxImageSynthesisInput setRefImg(String url) {
        this.toJsonObject().put("ref_img", url);
        return this;
    }
}
