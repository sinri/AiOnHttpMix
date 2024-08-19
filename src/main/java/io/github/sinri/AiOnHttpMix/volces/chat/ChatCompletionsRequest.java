package io.github.sinri.AiOnHttpMix.volces.chat;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class ChatCompletionsRequest extends SimpleJsonifiableEntity {
    public ChatCompletionsRequest() {
        super();
    }

    public ChatCompletionsRequest(JsonObject jsonObject) {
        super(jsonObject);
    }


    /**
     * @param model 以 endpoint_id 索引对应的模型接入点。
     */
    public ChatCompletionsRequest setModel(String model) {
        this.jsonObject.put("model", model);
        return this;
    }

    public ChatCompletionsRequest setMessages(List<ChatMessage> messages) {
        JsonArray a = new JsonArray();
        messages.forEach(message -> a.add(message.toJsonObject()));
        this.jsonObject.put("messages", a);
        return this;
    }

    public ChatCompletionsRequest addMessage(ChatMessage message) {
        JsonArray messages = this.jsonObject.getJsonArray("messages");
        if (messages == null) {
            messages = new JsonArray();
            this.jsonObject.put("messages", messages);
        }
        messages.add(message.toJsonObject());
        return this;
    }

    /**
     * 如果为正，会根据新 token 在文本中的出现频率对其进行惩罚，从而降低模型重复相同内容的可能性。
     *
     * @param frequency_penalty -2.0 到 2.0 之间的数字。
     */
    public ChatCompletionsRequest setFrequencyPenalty(double frequency_penalty) {
        this.jsonObject.put("frequency_penalty", frequency_penalty);
        return this;
    }

    // logit_bias as map: 修改指定 token 在模型输出内容中出现的概率。 接受一个 map，该对象将 token(token id 使用 tokenization 接口获取)映射到从-100到100的关联偏差值。每个模型的效果有所不同，但-1和1之间的值会减少或增加选择的可能性；-100或100应该导致禁止或排他选择相关的 token。
    // logprobs as boolean: 是否返回输出 tokens 的 logprobs。如果为 true，则返回 message (content) 中每个输出 token 的 logprobs。
    // top_logprobs as integer: 0 到 20 之间的整数，指定每个 token 位置最有可能返回的token数量，每个token 都有关联的对数概率。 如果使用此参数，则 logprobs 必须设置为 true
    // max_tokens as integer: 模型最大输出 token 数。输入 token 和输出 token 的总长度还受模型的上下文长度限制。
    // stop as string/array: 用于指定模型在生成响应时应停止的词语。当模型生成的响应中包含这些词汇时，生成过程将停止。

    /**
     * 是否流式返回。如果为 true，则按 SSE 协议返回数据。
     */
    public ChatCompletionsRequest setStream(boolean stream) {
        this.jsonObject.put("stream", stream);
        return this;
    }

    // stream_options as object: { include_usage as boolean: 如果设置，则在data: [DONE]消息之前会返回一个额外的块。此块上的 usage 字段显示了整个请求的 token 用量，其 choices 字段是一个空数组。所有其他块也将包含usage字段，但值为 null。 }

    /**
     * 较高的值(如0.8)将使输出更加随机，而较低的值(如0.2)将使输出更加集中和确定。
     * 通常建议修改 temperature 或 top_p，但不建议两者都修改。
     *
     * @param temperature 采样温度,在0到2之间。
     */
    public ChatCompletionsRequest setTemperature(double temperature) {
        this.jsonObject.put("temperature", temperature);
        return this;
    }

    /**
     * temperature 抽样的另一种选择，称为核抽样，其中模型考虑具有 top_p 概率质量的 token。
     * 所以 0.1 意味着只考虑包含前 10% 概率质量的标记。
     * 一般建议修改 top_p 或 temperature，但不建议两者都修改
     */
    public ChatCompletionsRequest setTopP(double top_p) {
        this.jsonObject.put("top_p", top_p);
        return this;
    }
}
