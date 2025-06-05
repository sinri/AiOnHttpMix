package io.github.sinri.AiOnHttpMix.provider.azure.openai.stateful;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIConfigElement;
import io.vertx.core.Future;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

/**
 * @since 2.0.1
 */
public class OpenAIStatefulApiKit {
    private final Map<String, OpenAIConfigElement> deploymentConfigMap;

    public OpenAIStatefulApiKit(Map<String, OpenAIConfigElement> deploymentConfigMap) {
        this.deploymentConfigMap = deploymentConfigMap;
    }

    public Future<StatefulChatResponse> request(String deploymentCode, StatefulChatRequest request) {
        OpenAIConfigElement openAIConfigElement = this.deploymentConfigMap.get(deploymentCode);
        Objects.requireNonNull(openAIConfigElement, "OpenAIConfigElement cannot be null");

        var apiKey = openAIConfigElement.readString(List.of("apiKey"));
        var resourceName = openAIConfigElement.readString(List.of("resourceName"));
        var deployment = openAIConfigElement.readString(List.of("deployment"));

        String url = "https://" + resourceName + ".openai.azure.com/openai/v1/responses?api-version=preview";

        request.setModel(deployment);

        return Keel.useWebClient(webClient -> webClient
                           .postAbs(url)
                           .putHeader("Content-Type", "application/json")
                           .bearerTokenAuthentication(apiKey)
                           .sendJsonObject(request.toJsonObject()))
                   .compose(bufferHttpResponse -> {
                       var output = bufferHttpResponse.bodyAsJsonObject();
                       StatefulChatResponse resp = StatefulChatResponse.wrap(output);
                       return Future.succeededFuture(resp);
                   });
    }
}
