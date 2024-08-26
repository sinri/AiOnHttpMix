package io.github.sinri.AiOnHttpMix.volces.v3.mixin.response;

import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public interface VolcesChatCompletionsResponseMixin extends UnmodifiableJsonifiableEntity {


    /**
     * @return 一次 chat completion 接口调用的唯一标识。
     */
    default String getId() {
        return readString("id");
    }

    /**
     * @return 本次对话生成时间戳（秒）。
     */
    default Integer getCreated() {
        return readInteger("created");
    }

    /**
     * @return 实际使用的模型名称和版本。
     */
    default String getModel() {
        return readString("model");
    }

    /**
     * @return 固定为 chat.completion。
     */
    default String getObject() {
        return readString("object");
    }

    /**
     * @return 本次请求的 tokens 用量
     */
    default VolcesKit.Usage getUsage() {
        return VolcesKit.Usage.wrap(readJsonObject("usage"));
    }

    /**
     *
     * @return 本次请求的模型输出内容
     */
    default List<VolcesKit.ChatCompletionsResponse.Choice> getChoices() {
        List<JsonObject> a = readJsonObjectArray("choices");
        if (a == null) return null;
        List<VolcesKit.ChatCompletionsResponse.Choice> list = new ArrayList<>();
        a.forEach(x -> {
            VolcesKit.ChatCompletionsResponse.Choice y = VolcesKit.ChatCompletionsResponse.Choice.wrap(x);
            list.add(y);
        });
        return list;
    }

}