package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.request.parameters;

import io.vertx.core.json.JsonObject;

public interface QwenRequestParameters
        extends QwenRequestParametersThinkMixin<QwenRequestParameters>,
        QwenRequestParametersVLMixin<QwenRequestParameters>,
        QwenRequestParametersOCRMixin<QwenRequestParameters>,
        QwenRequestParametersToolCallMixin<QwenRequestParameters>,
        QwenRequestParametersTranslateMixin<QwenRequestParameters>,
        QwenRequestParametersSearchMixin<QwenRequestParameters> {

    static QwenRequestParameters create() {
        return new QwenRequestParametersImpl();
    }

    static QwenRequestParameters wrap(JsonObject jsonObject) {
        return new QwenRequestParametersImpl(jsonObject);
    }
}
