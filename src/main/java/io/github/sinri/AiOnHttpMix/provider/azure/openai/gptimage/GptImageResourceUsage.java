package io.github.sinri.AiOnHttpMix.provider.azure.openai.gptimage;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

/**
 * @since 1.3.3
 */
public class GptImageResourceUsage extends UnmodifiableJsonifiableEntityImpl {

    public GptImageResourceUsage(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }

    /*
    "usage": {
        "input_tokens": 364,
        "input_tokens_details": {
          "image_tokens": 323,
          "text_tokens": 41
        },
        "output_tokens": 408,
        "total_tokens": 772
      }
     */

    public Integer getInputTokens() {
        return readInteger("input_tokens");
    }

    public Integer getInputTokensForImage() {
        return readInteger("input_tokens_details", "image_tokens");
    }

    public Integer getInputTokensForText() {
        return readInteger("input_tokens_details", "text_tokens");
    }

    public Integer getOutputTokens() {
        return readInteger("output_tokens");
    }

    public Integer getTotalTokens() {
        return readInteger("total_tokens");
    }

}
