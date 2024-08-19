package io.github.sinri.AiOnHttpMix.volces.chat;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public class ChatCompletionsSSEResponse extends SimpleJsonifiableEntity {
    public ChatCompletionsSSEResponse() {
        super();
    }

    public ChatCompletionsSSEResponse(JsonObject jsonObject) {
        super(jsonObject);
    }

}
