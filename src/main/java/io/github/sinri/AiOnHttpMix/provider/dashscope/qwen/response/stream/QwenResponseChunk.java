package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.stream;

import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.sync.QwenResponseOutput;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface QwenResponseChunk extends UnmodifiableJsonifiableEntity {
    static QwenResponseChunk wrap(JsonObject jsonObject) {
        return new QwenResponseChunkImpl(jsonObject);
    }

    default QwenResponseOutput getOutput() {
        return QwenResponseOutput.wrap(readJsonObject("output"));
    }

    default String getRequestId() {
        return readString("request_id");
    }

    default JsonObject getUsage() {
        return readJsonObject("usage");
    }
}
