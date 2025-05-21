package io.github.sinri.AiOnHttpMix.provider.azure.openai.o;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIConfigElement;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.OModelSpecification;

import java.util.Map;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class OServiceAdapter extends OpenAIServiceAdapter {
    public OServiceAdapter(Map<String, OpenAIConfigElement> deploymentConfigMap) {
        super(deploymentConfigMap);
    }

    @Override
    public boolean isModelCompatible(ChatModel chatModel) {
        return isModelCompatible((ModelSpecification) chatModel);
    }

    @Override
    public boolean isModelCompatible(ModelSpecification modelSpecification) {
        return Keel.reflectionHelper().isClassAssignable(OModelSpecification.class, modelSpecification.getClass());
    }
}
