package io.github.sinri.AiOnHttpMix.azure.openai.gptimage;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

/**
 * @see <a href="https://learn.microsoft.com/en-us/azure/ai-services/openai/how-to/dall-e?tabs=gpt-image-1">How to
 *         use Azure OpenAI image generation models</a>
 * @since 1.3.1
 */
public class GPTImageKit {
    private final String resourceName;
    private final String deployment;
    private final String apiVersion;
    private final String apiKey;

    public GPTImageKit(String resourceName, String deployment, String apiVersion, String apiKey) {
        this.resourceName = resourceName;
        this.deployment = deployment;
        this.apiVersion = apiVersion;
        this.apiKey = apiKey;
    }

    private String getUrlToGenerateImage() {
        return "https://" + resourceName + ".cognitiveservices.azure.com/openai/deployments/" + deployment + "/images/generations?api-version=" + apiVersion;
    }

    private String getUrlToEditImage() {
        return "https://" + resourceName + ".cognitiveservices.azure.com/openai/deployments/" + deployment + "/images/edits?api-version=" + apiVersion;
    }

    public Future<GenerateImageResponse> generateImage(GenerateImageRequest request) {
        return Keel.useWebClient(webClient -> webClient
                           .postAbs(getUrlToGenerateImage())
                           .putHeader("api-key", apiKey)
                           .sendJsonObject(request.toJsonObject())
                   )
                   .compose(bufferHttpResponse -> {
                       JsonObject body = bufferHttpResponse.bodyAsJsonObject();
                       GenerateImageResponse response = new GenerateImageResponse(body);
                       return Future.succeededFuture(response);
                   });
    }

    public Future<EditImageResponse> editImage(EditImageRequest request) {
        return Keel.useWebClient(webClient -> webClient
                           .postAbs(getUrlToEditImage())
                           .putHeader("api-key", apiKey)
                           .sendMultipartForm(request.toMultipartForm())
                   )
                   .compose(bufferHttpResponse -> {
                       EditImageResponse response = new EditImageResponse(bufferHttpResponse.bodyAsJsonObject());
                       return Future.succeededFuture(response);
                   });
    }

}
