package io.github.sinri.AiOnHttpMix.provider.azure.openai.o;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIConfigElement;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;

import java.util.Map;

public class OServiceAdapter extends OpenAIServiceAdapter {
    public OServiceAdapter(Map<String, OpenAIConfigElement> deploymentConfigMap) {
        super(deploymentConfigMap);
    }

    @Override
    public ModelSpecification getSpecification() {
        return ModelSpecification.o;
    }
}
