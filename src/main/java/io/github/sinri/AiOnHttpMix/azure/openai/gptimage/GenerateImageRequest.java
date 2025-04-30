package io.github.sinri.AiOnHttpMix.azure.openai.gptimage;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public class GenerateImageRequest extends SimpleJsonifiableEntity {
    public GenerateImageRequest(JsonObject jsonObject) {
        super(jsonObject);
    }

    public GenerateImageRequest() {
        this(new JsonObject());
    }

    public GenerateImageRequest setPrompt(String prompt) {
        this.jsonObject.put("prompt", prompt);
        return this;
    }

    /**
     * Specify the size of the generated images.
     * Must be one of 1024x1024, 1024x1536, or 1536x1024 for GPT-image-1 models.
     * Square images are faster to generate.
     *
     * @param size "1024x1024", "1024x1536", or "1536x1024"
     */
    public GenerateImageRequest setSize(Size size) {
        this.jsonObject.put("size", size.getSizeExpression());
        return this;
    }

    /**
     * There are three options for image quality: low, medium, and high.Lower quality images can be generated faster.
     * The default value is high.
     *
     * @param quality "low", "medium", and "high"
     */
    public GenerateImageRequest setQuality(Quality quality) {
        this.jsonObject.put("quality", quality.name());
        return this;
    }

    /**
     * You can generate between one and 10 images in a single API call.
     * The default value is 1.
     *
     * @param n [1,10]
     */
    public GenerateImageRequest setNumber(int n) {
        this.jsonObject.put("n", n);
        return this;
    }

    /**
     * Use the user parameter to specify a unique identifier for the user making the request.
     * This is useful for tracking and monitoring usage patterns.
     *
     * @param user The value can be any string, such as a user ID or email address.
     */
    public GenerateImageRequest setUser(String user) {
        this.jsonObject.put("user", user);
        return this;
    }

    /**
     * Use the output_format parameter to specify the format of the generated image.
     * Supported formats are PNG and JPEG.
     * The default is PNG.
     *
     * @param outputFormat PNG or JPEG
     */
    public GenerateImageRequest setOutputFormat(OutputFormat outputFormat) {
        this.jsonObject.put("output_format", outputFormat.name());
        return this;
    }

    /**
     * Use the output_compression parameter to specify the compression level for the generated image.
     * Input an integer between 0 and 100, where 0 is no compression and 100 is maximum compression.
     * The default is 100.
     *
     * @param output_compression [0,100]
     */
    public GenerateImageRequest setOutputCompression(int output_compression) {
        this.jsonObject.put("output_compression", output_compression);
        return this;
    }


}
