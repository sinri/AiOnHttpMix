package io.github.sinri.AiOnHttpMix.moonshot.chat;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class ChatCompletionsRequest extends SimpleJsonifiableEntity {
    public static final String ModelAsMoonshotFirstVersion8k = "moonshot-v1-8k";
    public static final String ModelAsMoonshotFirstVersion32k = "moonshot-v1-32k";
    public static final String ModelAsMoonshotFirstVersion128k = "moonshot-v1-128k";

    public ChatCompletionsRequest(JsonObject jsonObject) {
        super(jsonObject);
        if (!this.jsonObject.containsKey("messages")) {
            this.jsonObject.put("messages", new JsonArray());
        }
        if (!this.jsonObject.containsKey("tools")) {
            this.jsonObject.put("tools", new JsonArray());
        }
    }

    public ChatCompletionsRequest() {
        super();
        this.jsonObject.put("messages", new JsonArray());
        this.jsonObject.put("tools", new JsonArray());
    }

    public ChatCompletionsRequest setModel(String model) {
        this.jsonObject.put("model", model);
        return this;
    }

    public ChatCompletionsRequest addMessage(@NotNull Role role, @NotNull String content) {
        return addMessage(new Message(role, content));
    }

    public ChatCompletionsRequest addMessage(@NotNull Message message) {
        this.jsonObject.getJsonArray("messages").add(message.toJsonObject());
        return this;
    }

    /**
     * 聊天完成时生成的最大 token 数。
     * 如果到生成了最大 token 数个结果仍然没有结束，finish reason 会是 "length", 否则会是 "stop"。
     *
     * @param maxTokens 这个值建议按需给个合理的值，如果不给的话，我们会给一个不错的整数比如 1024。特别要注意的是，这个 max_tokens 是指您期待我们返回的 token 长度，而不是输入 + 输出的总长度。比如对一个 moonshot-v1-8k 模型，它的最大输入 + 输出总长度是 8192，当输入 messages 总长度为 4096 的时候，您最多只能设置为 4096，否则我们服务会返回不合法的输入参数（ invalid_request_error ），并拒绝回答。如果您希望获得“输入的精确 token 数”，可以使用下面的“计算 Token” API 使用我们的计算器获得计数。
     */
    public ChatCompletionsRequest setMaxTokens(int maxTokens) {
        this.jsonObject.put("max_tokens", maxTokens);
        return this;
    }

    /**
     * 使用什么采样温度，介于 0 和 1 之间。
     * 较高的值（如 0.7）将使输出更加随机，而较低的值（如 0.2）将使其更加集中和确定性。
     *
     * @param temperature 如果设置，值域须为 [0, 1] 我们推荐 0.3，以达到较合适的效果。
     */
    public ChatCompletionsRequest setTemperature(float temperature) {
        this.jsonObject.put("temperature", temperature);
        return this;
    }

    /**
     * 另一种采样温度。
     *
     * @param topP 默认 1.0。
     */
    public ChatCompletionsRequest setTopP(float topP) {
        this.jsonObject.put("top_p", topP);
        return this;
    }

    /**
     * 为每条输入消息生成多少个结果。
     *
     * @param n 默认 1，不得大于 5 特别的，当 temperature 非常小靠近 0 的时候，我们只能返回 1 个结果，如果这个时候 n 设置并 > 1，我们服务会返回不合法的输入参数（ invalid_request_error ）。
     */
    public ChatCompletionsRequest setN(int n) {
        this.jsonObject.put("n", n);
        return this;
    }

    /**
     * 存在惩罚，介于-2.0到2.0之间的数字。正值会根据新生成的词汇是否出现在文本中来进行惩罚，增加模型讨论新话题的可能性。
     *
     * @param presence_penalty 默认为 0。
     */
    public ChatCompletionsRequest setPresencePenalty(float presence_penalty) {
        this.jsonObject.put("presence_penalty", presence_penalty);
        return this;
    }

    /**
     * 频率惩罚，介于-2.0到2.0之间的数字。
     * 正值会根据新生成的词汇在文本中现有的频率来进行惩罚，减少模型一字不差重复同样话语的可能性。
     *
     * @param frequency_penalty 默认为 0。
     */
    public ChatCompletionsRequest setFrequencyPenalty(float frequency_penalty) {
        this.jsonObject.put("frequency_penalty", frequency_penalty);
        return this;
    }

    public ChatCompletionsRequest setStop(@NotNull String stop) {
        this.jsonObject.put("stop", stop);
        return this;
    }

    public ChatCompletionsRequest setStop(@NotNull Collection<String> stop) {
        var a = new JsonArray();
        stop.forEach(x -> {
            if (x != null) {
                a.add(x);
            }
        });
        this.jsonObject.put("stop", a);
        return this;
    }

    public ChatCompletionsRequest setStream(boolean stream) {
        this.jsonObject.put("stream", stream);
        return this;
    }

    public ChatCompletionsRequest addToolFunction(ToolMetaAsFunction toolMetaAsFunction) {
        JsonArray tools = this.jsonObject.getJsonArray("tools");
        tools.add(toolMetaAsFunction.toJsonObject());
        return this;
    }
}
