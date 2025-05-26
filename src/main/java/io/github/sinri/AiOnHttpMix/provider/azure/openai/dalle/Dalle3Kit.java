package io.github.sinri.AiOnHttpMix.provider.azure.openai.dalle;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIConfigElement;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.dalle.v3.Dalle3Parameters;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.dalle.v3.Dalle3Response;
import io.github.sinri.AiOnHttpMix.utils.AbnormalResponse;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class Dalle3Kit {

    private final OpenAIConfigElement openAIConfigElement;

    public Dalle3Kit(OpenAIConfigElement openAIConfigElement) {
        this.openAIConfigElement = openAIConfigElement;
    }

    public Future<JsonObject> draw(JsonObject parameters, String requestId) {
        return request(parameters, requestId);
    }

    public Future<Dalle3Response> draw(Dalle3Parameters parameters, String requestId) {
        return this.draw(parameters.toJsonObject(), requestId)
                   .compose(jsonObject -> {
                       return Future.succeededFuture(Dalle3Response.wrap(jsonObject));
                   });
    }

    public Future<Dalle3Response> draw(Handler<Dalle3Parameters> parametersHandler, String requestId) {
        Dalle3Parameters parameters = Dalle3Parameters.create();
        parametersHandler.handle(parameters);
        return this.draw(parameters, requestId);
    }

    /**
     * 生成 Azure OpenAI 服务主机名。
     *
     * @return 主机名
     */
    private String generateHost(OpenAIConfigElement configElement) {
        return configElement.getResourceName() + ".openai.azure.com";
    }

    /**
     * 生成 API 路径。
     *
     * @param api API 路径（以 / 开头）
     * @return 完整 URI
     */
    private String generateUri(OpenAIConfigElement configElement, String api) {
        return "/openai/deployments/" + configElement.getDeployment() + api + "?api-version=" + configElement.getApiVersion();
    }

    /**
     * 生成完整请求 URL。
     *
     * @param api API 路径（以 / 开头）
     * @return 完整 URL
     */
    private String generateUrl(OpenAIConfigElement configElement, String api) {
        return "https://" + generateHost(configElement) + generateUri(configElement, api);
    }

    private Future<JsonObject> request(JsonObject requestPayload, String requestId) {
        OpenAIConfigElement configElement = this.openAIConfigElement;

        var url = generateUrl(configElement, "/images/generations");
        AigcMix.getVerboseLogger().info(x -> x
                .message("Start AzureOpenAIServiceMeta.request")
                .context(
                        j -> j
                                .put("api", url)
                                .put("input", requestPayload)
                                .put("requestId", requestId)
                )
        );

        return Keel.useWebClient(webClient -> webClient
                .postAbs(url)
                .putHeader("Content-Type", "application/json")
                .putHeader("api-key", configElement.getApiKey())
                .sendJsonObject(requestPayload)
                .compose(bufferHttpResponse -> {
                    JsonObject entries = bufferHttpResponse.bodyAsJsonObject();
                    if (bufferHttpResponse.statusCode() != 200 || entries == null) {
                        throw new AbnormalResponse(bufferHttpResponse);
                    }
                    AigcMix.getVerboseLogger().info(x -> x
                            .message("bufferHttpResponse in AzureOpenAIServiceMeta.request")
                            .context(j -> j
                                    .put("output", entries)
                                    .put("requestId", requestId)
                            )
                    );
                    return Future.succeededFuture(entries);
                }));
    }

    public enum Dalle3Size {
        LANDSCAPE("1792x1024"),

        SQUARE("1024x1024"),

        PORTRAIT("1024x1792");
        private final String size;

        Dalle3Size(String size) {
            this.size = size;
        }

        public String size() {
            return size;
        }
    }

    public enum Dalle3Quality {
        hd, standard
    }

    public enum Dalle3Style {
        natural, vivid
    }

}
