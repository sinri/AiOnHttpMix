package io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.txt;

import io.vertx.core.json.JsonObject;

public interface QwenChatResponseChunkMixin {

    Integer getId();

    String getEvent();

    Integer getHttpStatus();

    String getDataAsString();

    JsonObject toJsonObject();


}
