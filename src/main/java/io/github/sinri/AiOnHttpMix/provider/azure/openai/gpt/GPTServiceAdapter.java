package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIConfigElement;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.specification.GPTModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;

import java.util.Map;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class GPTServiceAdapter extends OpenAIServiceAdapter {
    public GPTServiceAdapter(Map<String, OpenAIConfigElement> deploymentConfigMap) {
        super(deploymentConfigMap);
    }

    @Override
    public boolean isModelCompatible(ChatModel chatModel) {
        return isModelCompatible((ModelSpecification) chatModel);
    }

    @Override
    public boolean isModelCompatible(ModelSpecification modelSpecification) {
        return Keel.reflectionHelper().isClassAssignable(GPTModelSpecification.class, modelSpecification.getClass());
    }
}
