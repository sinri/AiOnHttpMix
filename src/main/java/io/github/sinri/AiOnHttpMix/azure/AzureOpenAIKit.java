package io.github.sinri.AiOnHttpMix.azure;

import io.github.sinri.keel.logger.event.KeelEventLogger;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

abstract public class AzureOpenAIKit {
    private final String apiKey;
    private final String resourceName;
    private final String deployment;
    private final String apiVersion;


    public AzureOpenAIKit(String serviceName) {
        this.apiKey = Keel.config("azure.openai." + serviceName + ".apiKey");
        this.resourceName = Keel.config("azure.openai." + serviceName + ".resourceName");
        this.deployment = Keel.config("azure.openai." + serviceName + ".deployment");
        this.apiVersion = Keel.config("azure.openai." + serviceName + ".apiVersion");

    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public String getDeployment() {
        return deployment;
    }

    public String getResourceName() {
        return resourceName;
    }


    public String generateHost() {
        return getResourceName() + ".openai.azure.com";
    }

    public String generateUri(String api) {
        return "/openai/deployments/" + getDeployment() + api + "?api-version=" + getApiVersion();
    }

    /**
     * @param api start with a slash `/`
     */
    public String generateUrl(String api) {
        return "https://" + generateHost() + generateUri(api);
    }

    protected Future<JsonObject> postRequest(String api, JsonObject requestBody) {
        var url = generateUrl(api);
        return WebClient.create(Keel.getVertx())
                .postAbs(url)
                .putHeader("Content-Type", "application/json")
                .putHeader("api-key", getApiKey())
                .sendJsonObject(requestBody)
//                .onFailure(throwable -> {
//                    this.getLogger().exception(throwable, log -> log
//                            .message("Api Failed")
//                            .context(c -> c
//                                    .put("url", url)
//                                    .put("input", requestBody)
//                            )
//                    );
//                })
                .compose(bufferHttpResponse -> {
                    JsonObject entries = bufferHttpResponse.bodyAsJsonObject();
                    if (bufferHttpResponse.statusCode() != 200 || entries == null) {
//                        this.getLogger().error(log -> log.message("Api Responded " + bufferHttpResponse.statusCode())
//                                .context(c -> c
//                                        .put("url", url)
//                                        .put("input", requestBody)
//                                        .put("output", bufferHttpResponse.bodyAsString())
//                                )
//                        );
                        return Future.failedFuture(new Exception("status code is " + bufferHttpResponse.statusCode() + " body is " + bufferHttpResponse.bodyAsString()));
                    }
//                    this.getLogger().info(log -> log.message("Api Responded " + bufferHttpResponse.statusCode())
//                            .context(c -> c
//                                    .put("url", url)
//                                    .put("input", requestBody)
//                                    .put("output", entries)
//                            )
//                    );
                    return Future.succeededFuture(entries);
                });
    }
}
