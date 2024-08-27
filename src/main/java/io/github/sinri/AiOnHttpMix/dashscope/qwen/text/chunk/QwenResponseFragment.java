package io.github.sinri.AiOnHttpMix.dashscope.qwen.text.chunk;

import io.vertx.core.json.JsonObject;

public interface QwenResponseFragment {
    static QwenResponseFragment parse(String string) {
        return new QwenResponseFragmentImpl(string);
    }


    Integer getId();

    String getEvent();

    Integer getHttpStatus();

    String getDataAsString();

    JsonObject toJsonObject();


}
