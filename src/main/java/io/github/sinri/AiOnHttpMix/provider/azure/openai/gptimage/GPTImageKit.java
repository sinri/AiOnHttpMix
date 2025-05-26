package io.github.sinri.AiOnHttpMix.provider.azure.openai.gptimage;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIConfigElement;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

/**
 * @see <a href="https://learn.microsoft.com/en-us/azure/ai-services/openai/how-to/dall-e?tabs=gpt-image-1">How to
 *         use Azure OpenAI image generation models</a>
 * @since 1.3.1
 */
public class GPTImageKit {

    private final OpenAIConfigElement openAIConfigElement;

    public GPTImageKit(OpenAIConfigElement openAIConfigElement) {
        this.openAIConfigElement = openAIConfigElement;
    }

    private String getUrlToGenerateImage() {
        return "https://" + openAIConfigElement.getResourceName() + ".cognitiveservices.azure.com/openai/deployments/" + openAIConfigElement.getDeployment() + "/images/generations?api-version=" + openAIConfigElement.getApiVersion();
    }

    private String getUrlToEditImage() {
        return "https://" + openAIConfigElement.getResourceName() + ".cognitiveservices.azure.com/openai/deployments/" + openAIConfigElement.getDeployment() + "/images/edits?api-version=" + openAIConfigElement.getApiVersion();
    }

    public Future<GenerateImageResponse> generateImage(GenerateImageRequest request) {
        return Keel.useWebClient(webClient -> webClient
                           .postAbs(getUrlToGenerateImage())
                           .putHeader("api-key", openAIConfigElement.getApiKey())
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
                           .putHeader("api-key", openAIConfigElement.getApiKey())
                           .sendMultipartForm(request.toMultipartForm())
                   )
                   .compose(bufferHttpResponse -> {
                       EditImageResponse response = new EditImageResponse(bufferHttpResponse.bodyAsJsonObject());
                       return Future.succeededFuture(response);
                   });
    }

}
