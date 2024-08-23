package io.github.sinri.AiOnHttpMix.azure.openai.core;

import io.github.sinri.AiOnHttpMix.azure.openai.core.impl.ServiceMetaImpl;
import io.github.sinri.keel.core.cutter.CutterOnString;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public interface AzureOpenAIServiceMeta {
    static AzureOpenAIServiceMeta create(String apiKey, String resourceName, String deployment, String apiVersion) {
        return new ServiceMetaImpl(apiKey, resourceName, deployment, apiVersion);
    }

    String generateHost();

    String generateUri(String api);

    /**
     * @param api start with a slash `/`
     */
    String generateUrl(String api);

    Future<JsonObject> postRequest(
            String api,
            JsonObject requestBody,
            String requestId
    );

    void postRequestSSE(
            String api,
            @NotNull JsonObject parameters,
            Promise<Void> promise,
            CutterOnString cutter,
            String requestId
    );
}
