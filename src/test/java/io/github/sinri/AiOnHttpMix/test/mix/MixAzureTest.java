package io.github.sinri.AiOnHttpMix.test.mix;

import io.github.sinri.AiOnHttpMix.azure.openai.core.AzureOpenAIServiceMeta;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMKit;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class MixAzureTest extends MixTestCore {

    @Override
    protected @NotNull Future<Void> starting() {
        return super.starting()
                .compose(v -> {
                    var serviceName = "gpt-4-o";

                    String apiKey = Keel.config("azure.openai." + serviceName + ".apiKey");
                    String resourceName = Keel.config("azure.openai." + serviceName + ".resourceName");
                    String deployment = Keel.config("azure.openai." + serviceName + ".deployment");
                    String apiVersion = Keel.config("azure.openai." + serviceName + ".apiVersion");

                    var serviceMeta = new AzureOpenAIServiceMeta(apiKey, resourceName, deployment, apiVersion);

                    anyLLMKit = new AnyLLMKit().useChatGPT(serviceMeta);

                    return Future.succeededFuture();
                });
    }
}
