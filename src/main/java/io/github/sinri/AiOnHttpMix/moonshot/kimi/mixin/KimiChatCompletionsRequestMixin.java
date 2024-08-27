package io.github.sinri.AiOnHttpMix.moonshot.kimi.mixin;

import io.github.sinri.AiOnHttpMix.moonshot.kimi.KimiKit;
import io.github.sinri.keel.core.SelfInterface;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonArray;

import java.util.List;

public interface KimiChatCompletionsRequestMixin<E> extends JsonifiableEntity<E>, SelfInterface<E> {
    default E setModel(KimiKit.Model model) {
        this.toJsonObject().put("model", model.getModelCode());
        return getImplementation();
    }

    default KimiKit.Model getModel() {
        String model = readString("model");
        if (model == null) return null;
        return KimiKit.Model.fromModelCode(model);
    }

    default E addMessage(KimiKit.Message message) {
        JsonArray array=toJsonObject().getJsonArray("messages");
        if(array==null) {
            array=new JsonArray();
            toJsonObject().put("messages", array);
        }
        array.add(message.toJsonObject());
        return getImplementation();
    }

    default E addToolDefinition(KimiKit.ToolDefinition toolDefinition) {
        JsonArray array=toJsonObject().getJsonArray("tools");
        if(array==null) {
            array=new JsonArray();
            toJsonObject().put("tools", array);
        }
        array.add(toolDefinition.toJsonObject());
        return getImplementation();
    }

    /**
     * 聊天完成时生成的最大 token 数。
     * 如果到生成了最大 token 数个结果仍然没有结束，finish reason 会是 "length", 否则会是 "stop"
     * 这个值建议按需给个合理的值，如果不给的话，我们会给一个不错的整数比如 1024。
     * 特别要注意的是，这个 max_tokens 是指您期待我们返回的 token 长度，而不是输入 + 输出的总长度。
     * 比如对一个 moonshot-v1-8k 模型，它的最大输入 + 输出总长度是 8192，当输入 messages 总长度为 4096 的时候，您最多只能设置为 4096，否则我们服务会返回不合法的输入参数（ invalid_request_error ），并拒绝回答。
     * 如果您希望获得“输入的精确 token 数”，可以使用下面的“计算 Token” API 使用我们的计算器获得计数
     */
    default E setMaxTokens(int max_tokens) {
        this.toJsonObject().put("max_tokens", max_tokens);
        return getImplementation();
    }

    /**
     * 使用什么采样温度，介于 0 和 1 之间。
     * 较高的值（如 0.7）将使输出更加随机，而较低的值（如 0.2）将使其更加集中和确定性.
     * 默认为 0，如果设置，值域须为 [0, 1] .
     * 我们推荐 0.3，以达到较合适的效果.
     */
    default E setTemperature(float temperature) {
        this.toJsonObject().put("temperature", temperature);
        return getImplementation();
    }

    /**
     * 另一种采样方法，即模型考虑概率质量为 top_p 的标记的结果。
     * 因此，0.1 意味着只考虑概率质量最高的 10% 的标记。
     * 一般情况下，我们建议改变这一点或温度，但不建议同时改变.
     *
     * @param top_p 默认 1.0
     */
    default E setTopP(float top_p) {
        this.toJsonObject().put("top_p", top_p);
        return getImplementation();
    }

    /**
     * 为每条输入消息生成多少个结果	int	默认为 1，不得大于 5。
     * 特别的，当 temperature 非常小靠近 0 的时候，我们只能返回 1 个结果，如果这个时候 n 已经设置并且 > 1，我们的服务会返回不合法的输入参数(invalid_request_error)
     */
    default E setN(int n) {
        this.toJsonObject().put("n", n);
        return getImplementation();
    }

    /**
     * 存在惩罚，介于-2.0到2.0之间的数字。
     * 正值会根据新生成的词汇是否出现在文本中来进行惩罚，增加模型讨论新话题的可能性.
     * 默认为 0.
     */
    default E setPresencePenalty(float presence_penalty) {
        this.toJsonObject().put("presence_penalty", presence_penalty);
        return getImplementation();
    }

    /**
     * 频率惩罚，介于-2.0到2.0之间的数字。
     * 正值会根据新生成的词汇在文本中现有的频率来进行惩罚，减少模型一字不差重复同样话语的可能性.
     * 默认为 0.
     */
    default E setFrequencyPenalty(float frequency_penalty) {
        this.toJsonObject().put("frequency_penalty", frequency_penalty);
        return getImplementation();
    }

    /**
     * 设置为 {"type": "json_object"} 可启用 JSON 模式，从而保证模型生成的信息是有效的 JSON。
     * 当你将 response_format 设置为 {"type": "json_object"} 时，你需要在 prompt 中明确地引导模型输出 JSON 格式的内容，并告知模型该 JSON 的具体格式，否则将可能导致不符合预期的结果。
     * 默认为 {"type": "text"}
     */
    default E setResponseFormat(ResponseFormat response_format) {
        this.toJsonObject().put("response_format", response_format.name());
        return getImplementation();
    }

    enum ResponseFormat {
        json_object, text
    }

    /**
     * 停止词，当全匹配这个（组）词后会停止输出，这个（组）词本身不会输出。
     * 最多不能超过 5 个字符串，每个字符串不得超过 32 字节.
     */
    default E setStop(String stop) {
        this.toJsonObject().put("stop", stop);
        return getImplementation();
    }

    /**
     * 停止词，当全匹配这个（组）词后会停止输出，这个（组）词本身不会输出。
     * 最多不能超过 5 个字符串，每个字符串不得超过 32 字节.
     */
    default E setStop(List<String> stop) {
        JsonArray array = new JsonArray(stop);
        this.toJsonObject().put("stop", array);
        return getImplementation();
    }

    /**
     * 是否流式返回
     */
    default E setStream(boolean stream) {
        this.toJsonObject().put("stream", stream);
        return getImplementation();
    }
}
