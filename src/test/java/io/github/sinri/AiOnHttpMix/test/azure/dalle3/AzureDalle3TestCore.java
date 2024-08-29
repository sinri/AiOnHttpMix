package io.github.sinri.AiOnHttpMix.test.azure.dalle3;

import io.github.sinri.AiOnHttpMix.azure.openai.core.AzureOpenAIServiceMeta;
import io.github.sinri.keel.logger.KeelLogLevel;
import io.github.sinri.keel.tesuto.KeelTest;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class AzureDalle3TestCore extends KeelTest {
    private String apiKey;
    private String resourceName;
    private String deployment;
    private String apiVersion;
    private AzureOpenAIServiceMeta serviceMeta;

    @Override
    protected @NotNull Future<Void> starting() {
        Keel.getConfiguration().loadPropertiesFile("config.properties");

        var serviceName = "dalle3";

        this.apiKey = Keel.config("azure.openai." + serviceName + ".apiKey");
        this.resourceName = Keel.config("azure.openai." + serviceName + ".resourceName");
        this.deployment = Keel.config("azure.openai." + serviceName + ".deployment");
        this.apiVersion = Keel.config("azure.openai." + serviceName + ".apiVersion");

        this.serviceMeta = new AzureOpenAIServiceMeta(apiKey, resourceName, deployment, apiVersion);

        getLogger().setVisibleLevel(KeelLogLevel.DEBUG);

        return Future.succeededFuture();
    }

    protected AzureOpenAIServiceMeta getServiceMeta() {
        return serviceMeta;
    }
}
