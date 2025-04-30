package io.github.sinri.AiOnHttpMix.azure.openai.gptimage;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonObject;

@Deprecated
public class EditImageRequestAsJsonObject extends SimpleJsonifiableEntity {
    public EditImageRequestAsJsonObject(JsonObject jsonObject) {
        super(jsonObject);
    }

    public EditImageRequestAsJsonObject() {
        this(new JsonObject());
    }

    /**
     * The input image to edit.
     * Must be a valid image URL or base64-encoded image.
     *
     * @param image a valid image URL or base64-encoded image
     */
    public EditImageRequestAsJsonObject setImage(String image) {
        this.jsonObject.put("image", image);
        return this;
    }

    /**
     * A mask image to define the area of the input image that the model should edit,
     * using fully transparent pixels (alpha of zero) in those areas.
     * Must be a valid image URL or base64-encoded image.
     *
     * @param mask a valid image URL or base64-encoded image
     */
    public EditImageRequestAsJsonObject setMask(String mask) {
        this.jsonObject.put("mask", mask);
        return this;
    }

    /**
     * A text description of how the input image should be edited.
     * The maximum length is 4000 characters.
     */
    public EditImageRequestAsJsonObject setPrompt(String prompt) {
        this.jsonObject.put("prompt", prompt);
        return this;
    }

    /**
     * The number of images to generate.
     * By default, n is 1.
     */
    public EditImageRequestAsJsonObject setNumber(int n) {
        this.jsonObject.put("n", n);
        return this;
    }

    /**
     * There are three options for image quality: low, medium, and high.Lower quality images can be generated
     * faster.
     * The default value is high.
     *
     * @param quality "low", "medium", and "high"
     */
    public EditImageRequestAsJsonObject setQuality(Quality quality) {
        this.jsonObject.put("quality", quality.name());
        return this;
    }

    /**
     * Specify the size of the generated images.
     * Must be one of 1024x1024, 1024x1536, or 1536x1024 for GPT-image-1 models.
     * Square images are faster to generate.
     *
     * @param size "1024x1024", "1024x1536", or "1536x1024"
     */
    public EditImageRequestAsJsonObject setSize(Size size) {
        this.jsonObject.put("size", size.getSizeExpression());
        return this;
    }

    /**
     * Use the user parameter to specify a unique identifier for the user making the request.
     * This is useful for tracking and monitoring usage patterns.
     *
     * @param user The value can be any string, such as a user ID or email address.
     */
    public EditImageRequestAsJsonObject setUser(String user) {
        this.jsonObject.put("user", user);
        return this;
    }

    /**
     * Use the output_compression parameter to specify the compression level for the generated image.
     * Input an integer between 0 and 100, where 0 is no compression and 100 is maximum compression.
     * The default is 100.
     *
     * @param output_compression [0,100]
     */
    public EditImageRequestAsJsonObject setOutputCompression(int output_compression) {
        this.jsonObject.put("output_compression", output_compression);
        return this;
    }
}
