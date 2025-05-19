package io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.sync;

import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.DoubaoMessageInResponse;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface DoubaoResponseChoice extends UnmodifiableJsonifiableEntity {
    static DoubaoResponseChoice wrap(JsonObject jsonObject) {
        return new DoubaoResponseChoiceImpl(jsonObject);
    }

    /**
     * @return 当前元素在 choices 列表的索引。
     */
    default Integer getIndex() {
        return readInteger("index");
    }

    /**
     * @return <p>模型停止生成 token 的原因。</p><p>取值范围：
     *         stop：模型输出自然结束，或因命中请求参数 stop 中指定的字段而被截断。
     *         length：模型输出因达到模型输出限制而被截断，有以下原因：
     *         触发max_token限制（回答内容的长度限制）。
     *         触发max_completion_tokens限制（思维链内容+回答内容的长度限制）。
     *         触发context_window限制（输入内容+思维链内容+回答内容的长度限制）。
     *         content_filter：模型输出被内容审核拦截。
     *         tool_calls：模型调用了工具。</p>
     */
    default String getFinishReason() {
        return readString("finish_reason");
    }

    /**
     * @return 模型输出的内容。
     */
    default DoubaoMessageInResponse getMessage() {
        JsonObject j = readJsonObject("message");
        return DoubaoMessageInResponse.wrap(j);
    }

    /**
     * @return 当前内容的对数概率信息。
     */
    default JsonObject getLogProbs() {
        return readJsonObject("logprobs");
    }

    /**
     * 注意：当前只有视觉理解模型支持返回该字段，且只有在方舟控制台接入点配置页面或者 CreateEndpoint 接口中，将内容护栏方案（ModerationStrategy）设置为基础方案（Basic）时，才会返回风险分类标签。
     *
     * @return 模型输出文字含有敏感信息时，会返回模型输出文字命中的风险分类标签。
     *         返回值及含义：
     *         severe_violation：模型输出文字涉及严重违规。
     *         violence：模型输出文字涉及激进行为。
     */
    default String getModerationHitType() {
        return readString("moderation_hit_type");
    }

    /**
     * @return 本次请求的 token 用量。
     */
    default JsonObject getUsage() {
        return readJsonObject("usage");
    }
}
