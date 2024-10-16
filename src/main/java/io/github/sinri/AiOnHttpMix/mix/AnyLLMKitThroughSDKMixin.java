package io.github.sinri.AiOnHttpMix.mix;

import io.github.sinri.AiOnHttpMix.azure.openai.core.AzureOpenAIServiceMeta;
import io.github.sinri.AiOnHttpMix.dashscope.core.DashscopeServiceMeta;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.github.sinri.AiOnHttpMix.volces.core.VolcesServiceMeta;
import io.github.sinri.keel.core.SelfInterface;

public interface AnyLLMKitThroughSDKMixin<S> extends SelfInterface<S> {
    S useChatGPT(AzureOpenAIServiceMeta azureOpenAIServiceMeta, SupportedModel model);

    default S useChatGPT(AzureOpenAIServiceMeta azureOpenAIServiceMeta) {
        return useChatGPT(azureOpenAIServiceMeta, SupportedModel.ChatGPT);
    }

    S useQwen(DashscopeServiceMeta dashscopeServiceMeta, SupportedModel model);

    default S useQwen(DashscopeServiceMeta dashscopeServiceMeta) {
        return useQwen(dashscopeServiceMeta, SupportedModel.QwenPlus);
    }

    S useVolces(VolcesServiceMeta volcesServiceMeta);
}
