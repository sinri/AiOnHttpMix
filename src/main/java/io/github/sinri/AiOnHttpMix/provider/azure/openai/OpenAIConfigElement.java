package io.github.sinri.AiOnHttpMix.provider.azure.openai;

import io.github.sinri.AiOnHttpMix.utils.models.azure.openai.gpt.OTextModelSeries;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * One OpenAIConfigElement instance maps to one Azure OpenAI Service Deployment.
 */
public class OpenAIConfigElement extends KeelConfigElement {

    public OpenAIConfigElement(
            OTextModelSeries model,
            String apiKey,
            String resourceName,
            String deployment,
            String apiVersion
    ) {
        super(model.getModelName());
        this.ensureChild("apiKey").setValue(apiKey);
        this.ensureChild("resourceName").setValue(resourceName);
        this.ensureChild("deployment").setValue(deployment);
        this.ensureChild("apiVersion").setValue(apiVersion);
    }

    public OpenAIConfigElement(@Nonnull KeelConfigElement another) {
        super(another);
    }

    /**
     * @return Azure OpenAI API 密钥
     */
    public String getApiKey() {
        return readString(List.of("apiKey"));
    }

    /**
     * @return Azure 资源名称
     */
    public String getResourceName() {
        return readString(List.of("resourceName"));
    }

    /**
     * @return 部署名称
     */
    public String getDeployment() {
        return readString(List.of("deployment"));
    }

    /**
     * @return API 版本号
     */
    public String getApiVersion() {
        return readString(List.of("apiVersion"));
    }

}
