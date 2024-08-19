package io.github.sinri.AiOnHttpMix.volces.chat;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ChatCompletionsResponse extends SimpleJsonifiableEntity {
    public ChatCompletionsResponse(JsonObject jsonObject) {
        super(jsonObject);
    }

    /**
     * @return 一次 chat completion 接口调用的唯一标识。
     */
    public String id() {
        return readString("id");
    }

    /**
     * @return 本次对话生成时间戳（秒）。
     */
    public Integer created() {
        return readInteger("created");
    }

    /**
     * @return 实际使用的模型名称和版本。
     */
    public String model() {
        return readString("model");
    }

    /**
     * @return 固定为 chat.completion。
     */
    public String object() {
        return readString("object");
    }

    /**
     * @return 本次 chat 结果列表。长度固定为 1。
     */
    public List<Choice> choices() {
        List<JsonObject> choices = readJsonObjectArray("choices");
        if (choices == null) return new ArrayList<>();
        return choices.stream().map(Choice::new).toList();
    }

    /**
     * prompt_tokens as integer: 本次请求中输入的 token 数量。
     * completion_tokens as integer: 模型生成的 token 数量。
     * total_tokens as integer: 总的 token 数量。
     *
     * @return 本次请求的 tokens 用量。
     */
    public JsonObject rawUsage() {
        return readJsonObject("usage");
    }

    public static class Choice extends SimpleJsonifiableEntity {
        public Choice(JsonObject jsonObject) {
            super(jsonObject);
        }

        /**
         * @return 该元素在 choices 列表的索引。
         */
        public Integer index() {
            return readInteger("index");
        }

        /**
         * @return 模型输出的消息内容
         */
        public JsonObject rawMessage() {
            return readJsonObject("message");
        }

        /**
         * @return 模型生成结束原因.
         * stop表示正常生成结束;
         * length 表示已经到了生成的最大 token 数量;
         * content_filter 表示命中审核提前终止。
         */
        public String finishReason() {
            return readString("finish_reason");
        }

        /**
         * @return 该输出结果的概率信息。
         * <p>
         * 其只有一个 content 字段，类型为 array，表示message列表中每个元素content token的概率信息，content 元素子字段说明如下：
         * token [string]: 对应 token；
         * logprob [number]：token的概率；
         * bytes [array]：表示 token 的 UTF-8 字节表示的整数列表。在字符由多个 token 表示，并且它们的字节表示必须组合以生成正确的文本表示的情况下(表情符号或特殊字符)非常有用。如果 token 没有 byte 表示，则可以为空。
         * top_logprobs [array]：最可能的token列表及其在此 token位置的对数概率：
         * token [string]: 对应token；
         * logprob [number]：token的概率；
         * bytes [array]：表示 token 的 UTF-8 字节表示的整数列表。在字符由多个 token 表示，并且它们的字节表示必须组合以生成正确的文本表示的情况下(表情符号或特殊字符)非常有用。如果 token 没有 byte 表示，则可以为空。
         * </p>
         */
        public JsonObject logprobs() {
            return readJsonObject("logprobs");
        }
    }
}
