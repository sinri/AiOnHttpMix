package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.response.stream;

import io.vertx.core.json.JsonObject;

public interface QwenResponseFragment {
    static QwenResponseFragment wrap(String s) {
        return new QwenResponseFragmentImpl(s);
    }

    String getId();

    String getEvent();

    String getRawData();

    JsonObject getData();
}
