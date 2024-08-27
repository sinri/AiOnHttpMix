package io.github.sinri.AiOnHttpMix.dashscope.qwen.embedding;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface DashscopeTextEmbeddingGenerateResponse extends UnmodifiableJsonifiableEntity {
    static DashscopeTextEmbeddingGenerateResponse wrap(JsonObject response) {
        return new DashscopeTextEmbeddingGenerateResponseImpl(response);
    }

    default DashscopeTextEmbeddingGenerateResponseOutput getOutput() {
        var x = readJsonObject("output");
        if (x == null) return null;
        return DashscopeTextEmbeddingGenerateResponseOutput.wrap(x);
    }

    default DashscopeTextEmbeddingGenerateResponseUsage getUsage() {
        var x = readJsonObject("usage");
        if (x == null) return null;
        return DashscopeTextEmbeddingGenerateResponseUsage.wrap(x);
    }

    default String getRequestId() {
        return readString("requestId");
    }

}
