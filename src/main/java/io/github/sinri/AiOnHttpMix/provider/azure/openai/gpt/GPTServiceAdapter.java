package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIConfigElement;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;

import java.util.Map;

public class GPTServiceAdapter extends OpenAIServiceAdapter {
    public GPTServiceAdapter(Map<String, OpenAIConfigElement> deploymentConfigMap) {
        super(deploymentConfigMap);
    }

    @Override
    public ModelSpecification getSpecification() {
        return ModelSpecification.chatgpt;
    }
}
