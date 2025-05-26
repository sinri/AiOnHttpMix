package io.github.sinri.AiOnHttpMix.provider.azure.openai.gptimage;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

/**
 * @since 1.3.3
 */
public class GptImageApiError extends UnmodifiableJsonifiableEntityImpl {

    public GptImageApiError(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }

    public String getMessage() {
        return readString("message");
    }

    public String getType() {
        return readString("type");
    }

    public Object getParam() {
        return readValue("param");
    }

    public String getCode() {
        return readString("code");
    }
}
