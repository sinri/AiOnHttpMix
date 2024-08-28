package io.github.sinri.AiOnHttpMix.volces.v3.response;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface VolcesChatUsage extends UnmodifiableJsonifiableEntity {
    static VolcesChatUsage wrap(JsonObject jsonObject) {
        return new VolcesChatUsageImpl(jsonObject);
    }

    /**
     * @return 输入的 prompt token 数量
     */
    default Integer getPromptTokens() {
        return readInteger("prompt_tokens");
    }

    /**
     * @return 模型生成的 token 数量
     */
    default Integer getCompletionTokens() {
        return readInteger("completion_tokens");
    }

    /**
     * @return 本次请求消耗的总 token 数量（输入 + 输出）
     */
    default Integer getTotalTokens() {
        return readInteger("total_tokens");
    }

}
