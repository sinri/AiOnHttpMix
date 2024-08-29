package io.github.sinri.AiOnHttpMix.dashscope.qwen.text;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface QwenResponseBase extends UnmodifiableJsonifiableEntity {
    int getStatusCode();

    default String getErrorCode() {
        return readString("code");
    }

    default String getErrorMessage() {
        return readString("message");
    }

    Usage getUsage();

    /**
     * @return 本次请求的系统唯一码。
     */
    default String request_id() {
        return readString("request_id");
    }

    interface Usage extends UnmodifiableJsonifiableEntity {
        static Usage wrap(JsonObject jsonObject) {
            return new QwenResponseBaseUsageImpl(jsonObject);
        }

        /**
         * @return 模型输出内容的 token个数。
         */
        default Integer getOutputTokens() {
            return readInteger("output_tokens");
        }

        /**
         * 本次请求输入内容的token个数。
         * 在enable_search设置为true时，输入的 token 数目由于需要添加搜索相关内容，因此会比您在请求中的输入token个数多。
         */
        default Integer getInputTokens() {
            return readInteger("input_tokens");
        }

        /**
         * usage.output_tokens与usage.input_tokens的总和。
         * ¬
         */
        default Integer getTotalTokens() {
            return readInteger("total_tokens");
        }
    }
}
