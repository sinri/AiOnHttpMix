package io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.txt;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;

public interface ChatResponseBase extends UnmodifiableJsonifiableEntity {
    Usage getUsage();

    /**
     * @return 本次请求的系统唯一码。
     */
    default String request_id() {
        return readString("request_id");
    }

    interface Usage extends UnmodifiableJsonifiableEntity {
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
