package io.github.sinri.AiOnHttpMix.dashscope.qwen.vl;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface QwenVLUsage extends UnmodifiableJsonifiableEntity {
    static QwenVLUsage wrap(JsonObject jsonObject) {
        return new QwenVLUsageImpl(jsonObject);
    }
    /**
     * @return 本次请求算法输出内容的 token 数目。
     */
    default Integer getOutputTokens() {
        return readInteger("output_tokens");
    }

    /**
     * 用户输入转换成Token后的长度。
     * 对于通义千问VL模型，input_tokens主要由两部分组成，分别是用户输入文本转换的Token和用户输入图片转换的Token。
     * 需要注意的是，文本与图片中间需要额外的标识进行拼接，因此即使用户输入不包含文本，input_tokens会稍多于image_tokens
     */
    default Integer getInputTokens() {
        return readInteger("input_tokens");
    }

    /**
     * @return 用户输入图片转换成Token后的长度。
     */
    default Integer getImageTokens() {
        return readInteger("image_tokens");
    }
}
