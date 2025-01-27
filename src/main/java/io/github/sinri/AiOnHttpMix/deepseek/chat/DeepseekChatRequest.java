package io.github.sinri.AiOnHttpMix.deepseek.chat;

import io.github.sinri.AiOnHttpMix.deepseek.chat.message.DeepseekMessageInRequest;
import io.github.sinri.AiOnHttpMix.deepseek.core.DeepseekModel;
import io.github.sinri.AiOnHttpMix.utils.FunctionToolDefinition;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

/**
 * @see <a href="https://api-docs.deepseek.com/zh-cn/api/create-chat-completion">对话补全</a>
 */
public interface DeepseekChatRequest extends JsonifiableEntity<DeepseekChatRequest> {
    static DeepseekChatRequest create() {
        return new DeepseekChatRequestImpl();
    }

    static DeepseekChatRequest wrap(@NotNull JsonObject jsonObject) {
        return new DeepseekChatRequestImpl(jsonObject);
    }

    default DeepseekChatRequest addMessage(DeepseekMessageInRequest message) {
        JsonArray jsonArray = toJsonObject().getJsonArray("messages");
        if (jsonArray == null) {
            jsonArray = new JsonArray();
            toJsonObject().put("messages", jsonArray);
        }
        jsonArray.add(message);
        return this;
    }

    default DeepseekChatRequest setModel(DeepseekModel model) {
        toJsonObject().put("model", model.getCode());
        return this;
    }

    /**
     * @param frequency_penalty 介于 -2.0 和 2.0 之间的数字。如果该值为正，那么新 token 会根据其在已有文本中的出现频率受到相应的惩罚，降低模型重复相同内容的可能性。
     */
    default DeepseekChatRequest setFrequencyPenalty(double frequency_penalty) {
        toJsonObject().put("frequency_penalty", frequency_penalty);
        return this;
    }

    /**
     * @param max_tokens 介于 1 到 8192 间的整数，限制一次请求中模型生成 completion 的最大 token 数。输入 token 和输出 token 的总长度受模型的上下文长度的限制。
     *                   如未指定 max_tokens参数，默认使用 4096。
     */
    default DeepseekChatRequest setMaxTokens(long max_tokens) {
        toJsonObject().put("max_tokens", max_tokens);
        return this;
    }

    /**
     * @param presence_penalty 介于 -2.0 和 2.0 之间的数字。如果该值为正，那么新 token 会根据其是否已在已有文本中出现受到相应的惩罚，从而增加模型谈论新主题的可能性。
     */
    default DeepseekChatRequest setPresencePenalty(double presence_penalty) {
        toJsonObject().put("presence_penalty", presence_penalty);
        return this;
    }

    /**
     * 一个 object，指定模型必须输出的格式。
     * 设置为 { "type": "json_object" } 以启用 JSON 模式，该模式保证模型生成的消息是有效的 JSON。
     * 注意: 使用 JSON 模式时，你还必须通过系统或用户消息指示模型生成 JSON。
     * 否则，模型可能会生成不断的空白字符，直到生成达到令牌限制，从而导致请求长时间运行并显得“卡住”。
     * 此外，如果 finish_reason="length"，这表示生成超过了 max_tokens 或对话超过了最大上下文长度，消息内容可能会被部分截断。
     *
     * @param response_format_type text, json_object
     */
    default DeepseekChatRequest setResponseFormatType(ResponseFormatType response_format_type) {
        toJsonObject().put("response_format", new JsonObject().put("type", response_format_type.name()));
        return this;
    }

    /**
     * @param stream 如果设置为 True，将会以 SSE（server-sent events）的形式以流式发送消息增量。消息流以 data: [DONE] 结尾。
     */
    default DeepseekChatRequest setStream(boolean stream) {
        toJsonObject().put("stream", stream);
        return this;
    }

    /*
        - stream_options as object, nullable: 流式输出相关选项。只有在 stream 参数为 true 时，才可设置此参数。
            - include_usage as boolean: 如果设置为 true，在流式消息最后的 data: [DONE] 之前将会传输一个额外的块。此块上的 usage 字段显示整个请求的 token 使用统计信息，而 choices 字段将始终是一个空数组。所有其他块也将包含一个 usage 字段，但其值为 null。
        - logprobs as boolean, nullable: 是否返回所输出 token 的对数概率。如果为 true，则在 message 的 content 中返回每个输出 token 的对数概率。
        - top_logprobs as integer, nullable: Possible values: <= 20 一个介于 0 到 20 之间的整数 N，指定每个输出位置返回输出概率 top N 的 token，且返回这些 token 的对数概率。指定此参数时，logprobs 必须为 true。
        - stop as object, nullable: 一个 string 或最多包含 16 个 string 的 list，在遇到这些词时，API 将停止生成更多的 token。
     */

    /**
     * @param temperature 采样温度，介于 0 和 2 之间。更高的值，如 0.8，会使输出更随机，而更低的值，如 0.2，会使其更加集中和确定。 我们通常建议可以更改这个值或者更改 top_p，但不建议同时对两者进行修改。
     */
    default DeepseekChatRequest setTemperature(double temperature) {
        toJsonObject().put("temperature", temperature);
        return this;
    }

    /**
     * @param top_p 作为调节采样温度的替代方案，模型会考虑前 top_p 概率的 token 的结果。所以 0.1 就意味着只有包括在最高 10% 概率中的 token 会被考虑。 我们通常建议修改这个值或者更改 temperature，但不建议同时对两者进行修改。
     */
    default DeepseekChatRequest setTopP(long top_p) {
        toJsonObject().put("top_p", top_p);
        return this;
    }

    /**
     * 模型可能会调用的 tool 的列表。
     * 目前，仅支持 function 作为工具。使用此参数来提供以 JSON 作为输入参数的 function 列表。最多支持 128 个 function。
     */
    default DeepseekChatRequest addTool(ToolDefinition toolDefinition) {
        JsonArray jsonArray = toJsonObject().getJsonArray("tools");
        if (jsonArray == null) {
            jsonArray = new JsonArray();
            toJsonObject().put("tools", jsonArray);
        }
        jsonArray.add(toolDefinition.toJsonObject());
        return this;
    }

    /**
     * 控制模型调用 tool 的行为。
     * none 意味着模型不会调用任何 tool，而是生成一条消息。
     * auto 意味着模型可以选择生成一条消息或调用一个或多个 tool。
     * required 意味着模型必须调用一个或多个 tool。
     * 当没有 tool 时，默认值为 none。如果有 tool 存在，默认值为 auto。
     */
    default DeepseekChatRequest setToolChoice(ChatCompletionToolChoice choice) {
        toJsonObject().put("tool_choice", choice.name());
        return this;
    }

    default DeepseekChatRequest setToolChoice(String functionName) {
        toJsonObject().put("tool_choice", new JsonObject()
                .put("type", "function")
                .put("function", new JsonObject().put("name", functionName))
        );
        return this;
    }

    enum ResponseFormatType {
        text, json_object
    }

    enum ChatCompletionToolChoice {
        none,// 意味着模型不会调用任何 tool，而是生成一条消息。
        auto,// 意味着模型可以选择生成一条消息或调用一个或多个 tool。
        required,// 意味着模型必须调用一个或多个 tool。
    }

    interface ToolDefinition extends JsonifiableEntity<ToolDefinition> {
        static ToolDefinition create() {
            return new ToolDefinitionImpl();
        }

        static ToolDefinition wrap(JsonObject jsonObject) {
            return new ToolDefinitionImpl(jsonObject);
        }

        enum Type {
            function
        }

        class Builder extends FunctionToolDefinition.FunctionToolDefinitionBuilder<ToolDefinition.Builder, ToolDefinition> {

            @Override
            public ToolDefinition build() {
                return ToolDefinition.wrap(toJsonObject());
            }

            @Override
            public @NotNull ToolDefinition.Builder getImplementation() {
                return this;
            }
        }
    }
}
