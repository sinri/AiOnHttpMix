package io.github.sinri.AiOnHttpMix.provider.azure.openai.gptimage;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

/**
 * @since 1.3.3
 */
public class GptImageResultDatum extends UnmodifiableJsonifiableEntityImpl {

    public GptImageResultDatum(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }

    public String getB64Json() {
        return readString("b64_json");
    }

    public Buffer transformToBuffer() {
        return Buffer.buffer(Keel.stringHelper().decodeWithBase64ToBytes(getB64Json()));
    }
}
