package io.github.sinri.AiOnHttpMix.deepseek.chat.message;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface DeepseekMessageInResponse extends DeepseekMessageBase<DeepseekMessageInResponse> {
    static DeepseekMessageInResponse wrap(JsonObject jsonObject) {
        return new DeepseekMessageInResponseImpl(jsonObject);
    }

    /**
     * @return 仅适用于 deepseek-reasoner 模型。内容为 assistant 消息中在最终答案之前的推理内容。
     */
    default String getReasoningContent() {
        return readString("reasoning_content");
    }

    /**
     * @return 模型生成的 tool 调用，例如 function 调用。
     */
    default List<DeepseekToolCallInResponse> getToolCalls() {
        var a = readJsonObjectArray("tool_calls");
        if (a == null) {
            return List.of();
        }
        return a.stream().map(DeepseekToolCallInResponse::wrap).toList();
    }

    interface DeepseekToolCallInResponse extends UnmodifiableJsonifiableEntity {
        static DeepseekToolCallInResponse wrap(JsonObject jsonObject) {
            return new DeepseekToolCallInResponseImpl(jsonObject);
        }

        /**
         * @return tool 调用的 ID。
         */
        default String getId() {
            return readString("id");
        }

        /**
         * @return tool 的类型。目前仅支持 function。
         */
        default String getType() {
            return readString("type");
        }

        default FunctionCall getFunction() {
            JsonObject f = readJsonObject("function");
            if (f == null) {
                return null;
            }
            return FunctionCall.wrap(f);
        }

        interface FunctionCall extends UnmodifiableJsonifiableEntity {
            static FunctionCall wrap(JsonObject jsonObject) {
                return new DeepseekToolCallInResponseImpl.FunctionCallImpl(jsonObject);
            }

            /**
             * @return 模型调用的 function 名。
             */
            default String getName() {
                return readString("name");
            }

            /**
             * @return 要调用的 function 的参数，由模型生成，格式为 JSON。
             * 请注意，模型并不总是生成有效的 JSON，并且可能会臆造出你函数模式中未定义的参数。在调用函数之前，请在代码中验证这些参数。
             */
            default String getArguments() {
                return readString("arguments");
            }
        }
    }
}
