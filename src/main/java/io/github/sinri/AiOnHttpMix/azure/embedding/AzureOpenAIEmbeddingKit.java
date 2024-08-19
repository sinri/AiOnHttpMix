package io.github.sinri.AiOnHttpMix.azure.embedding;

import io.github.sinri.AiOnHttpMix.azure.AzureOpenAIKit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class AzureOpenAIEmbeddingKit extends AzureOpenAIKit {
    public AzureOpenAIEmbeddingKit(String serviceName) {
        super(serviceName);
    }

    public Future<EmbeddingResponse> call(String input) {
//        String url = "https://" + getResourceName() + ".openai.azure.com/openai/deployments/" + getDeployment() + "/embeddings?api-version=" + getApiVersion();

        return postRequest("/embeddings", new JsonObject().put("input", input))
                .compose(jsonObject -> {
                    return Future.succeededFuture(new EmbeddingResponse(jsonObject));
                });
    }
}
