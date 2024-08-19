package io.github.sinri.AiOnHttpMix.azure.completion;

import io.github.sinri.AiOnHttpMix.azure.AzureOpenAIKit;
import io.vertx.core.Future;

public class AzureOpenAICompletionKit extends AzureOpenAIKit {

    public AzureOpenAICompletionKit(String serviceName) {
        super(serviceName);
    }

    public Future<CompletionResponse> call(CompletionsParameters parameters) {
//        String url = "https://" + getResourceName() + ".openai.azure.com/openai/deployments/" + getDeployment() + "/completions?api-version=" + getApiVersion();
        return postRequest("/completions", parameters.toJsonObject())
                .compose(jsonObject -> {
                    return Future.succeededFuture(new CompletionResponse(jsonObject));
                });
    }

}
