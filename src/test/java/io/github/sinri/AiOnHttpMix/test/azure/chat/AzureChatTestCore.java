package io.github.sinri.AiOnHttpMix.test.azure.chat;

import io.github.sinri.AiOnHttpMix.azure.openai.core.AzureOpenAIServiceMeta;
import io.github.sinri.keel.logger.KeelLogLevel;
import io.github.sinri.keel.tesuto.KeelTest;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class AzureChatTestCore extends KeelTest {
    private AzureOpenAIServiceMeta serviceMeta;

    @Override
    protected @NotNull Future<Void> starting() {
        Keel.getConfiguration().loadPropertiesFile("config.properties");

        var serviceName = "gpt-4-o";

        String apiKey = Keel.config("azure.openai." + serviceName + ".apiKey");
        String resourceName = Keel.config("azure.openai." + serviceName + ".resourceName");
        String deployment = Keel.config("azure.openai." + serviceName + ".deployment");
        String apiVersion = Keel.config("azure.openai." + serviceName + ".apiVersion");

        this.serviceMeta = new AzureOpenAIServiceMeta(apiKey, resourceName, deployment, apiVersion);

        getLogger().setVisibleLevel(KeelLogLevel.DEBUG);

        return Future.succeededFuture();
    }

    protected AzureOpenAIServiceMeta getServiceMeta() {
        return serviceMeta;
    }
}
