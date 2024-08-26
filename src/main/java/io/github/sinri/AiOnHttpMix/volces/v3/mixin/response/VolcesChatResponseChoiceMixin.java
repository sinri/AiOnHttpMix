package io.github.sinri.AiOnHttpMix.volces.v3.mixin.response;

import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface VolcesChatResponseChoiceMixin extends UnmodifiableJsonifiableEntity {
    /**
     * @return 该元素在 choices 列表的索引。
     */
    default Integer getIndex() {
        return readInteger("index");
    }

    /**
     * @return 模型输出的消息内容
     */
    default VolcesKit.ChatCompletionsResponse.Message getMessage() {
        var x = readJsonObject("message");
        return VolcesKit.ChatCompletionsResponse.Message.wrap(x);
    }

    /**
     * @return 模型生成结束原因.
     * stop表示正常生成结束;
     * length 表示已经到了生成的最大 token 数量;
     * content_filter 表示命中审核提前终止。
     */
    default String getFinishReason() {
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
    default JsonObject getLogprobs() {
        return readJsonObject("logprobs");
    }
}
