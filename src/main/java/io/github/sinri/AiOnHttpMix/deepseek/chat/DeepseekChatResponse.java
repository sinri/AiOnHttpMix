package io.github.sinri.AiOnHttpMix.deepseek.chat;

import io.github.sinri.AiOnHttpMix.deepseek.chat.message.DeepseekMessageInResponse;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface DeepseekChatResponse extends UnmodifiableJsonifiableEntity {
    static DeepseekChatResponse wrap(JsonObject jsonObject) {
        return new DeepseekChatResponseImpl(jsonObject);
    }

    /**
     * @return 该对话的唯一标识符。
     */
    default String getId() {
        return readString("id");
    }

    /**
     * @return 创建聊天完成时的 Unix 时间戳（以秒为单位）。
     */
    default Integer getCreated() {
        return readInteger("created");
    }

    /**
     * @return 生成该 completion 的模型名。
     */
    default String getModel() {
        return readString("model");
    }

    /**
     * @return This fingerprint represents the backend configuration that the model runs with.
     */
    default String getSystemFingerprint() {
        return readString("system_fingerprint");
    }

    default List<Choice> getChoices() {
        var a = readJsonObjectArray("choices");
        if (a == null) {
            return List.of();
        }
        return a.stream().map(Choice::wrap).toList();
    }

    // object 对象的类型, 其值为 chat.completion。
    // usage 该对话补全请求的用量信息。

    interface Choice extends UnmodifiableJsonifiableEntity {
        static Choice wrap(JsonObject jsonObject) {
            return new DeepseekChatResponseImpl.ChoiceImpl(jsonObject);
        }

        /**
         * stop：模型自然停止生成，或遇到 stop 序列中列出的字符串。
         * length ：输出长度达到了模型上下文长度限制，或达到了 max_tokens 的限制。
         * content_filter：输出内容因触发过滤策略而被过滤。
         * insufficient_system_resource：系统推理资源不足，生成被打断。
         *
         * @return 模型停止生成 token 的原因。
         */
        default String getFinishReason() {
            return readString("finish_reason");
        }

        /**
         * @return 该 completion 在模型生成的 completion 的选择列表中的索引。
         */
        default Integer getIndex() {
            return readInteger("index");
        }

        /**
         * @return 模型生成的 completion 消息。
         */
        default DeepseekMessageInResponse getMessage() {
            JsonObject message = readJsonObject("message");
            return DeepseekMessageInResponse.wrap(message);
        }

        // logprobs: 该 choice 的对数概率信息。
    }
}
