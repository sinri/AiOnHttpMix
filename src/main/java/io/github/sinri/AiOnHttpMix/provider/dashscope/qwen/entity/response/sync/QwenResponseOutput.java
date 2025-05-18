package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.response.sync;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.List;
/**
 * @since 2.0.0
 */
public interface QwenResponseOutput extends UnmodifiableJsonifiableEntity {
    static QwenResponseOutput wrap(JsonObject jsonObject) {
        return new QwenResponseOutputImpl(jsonObject);
    }

    /**
     * 模型生成的回复。
     * 当设置输入参数result_format为text时将回复内容返回到该字段。
     */
    default String getText() {
        return readString("text");
    }

    /**
     * 当设置输入参数result_format为text时该参数不为空。
     * <p>
     * 有四种情况：<br>
     * 正在生成时为{@code null}；<br>
     * 因模型输出自然结束，或触发输入参数中的stop条件而结束时为{@code stop}；<br>
     * 因生成长度过长而结束为{@code length}；<br>
     * 因发生工具调用为{@code tool_calls}。<br>
     * </p>
     */
    default String getFinishReason() {
        return readString("finish_reason");
    }

    /**
     * 模型的输出信息。
     * 当result_format为message时返回choices参数。
     */
    default List<QwenResponseOutputChoice> getChoices() {
        List<JsonObject> list = readJsonObjectArray("choices");
        if (list == null) {
            return List.of();
        }
        return list.stream().map(QwenResponseOutputChoice::wrap).toList();
    }

    default QwenResponseOutputSearchInfo getSearchInfo() {
        var x = readJsonObject("search_info");
        if (x == null) return null;
        return QwenResponseOutputSearchInfo.wrap(x);
    }

}
