package io.github.sinri.AiOnHttpMix.mix;

import io.github.sinri.AiOnHttpMix.azure.openai.core.AzureOpenAIServiceMeta;
import io.github.sinri.AiOnHttpMix.dashscope.core.DashscopeServiceMeta;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.github.sinri.AiOnHttpMix.volces.core.VolcesServiceMeta;
import io.github.sinri.keel.core.SelfInterface;

public interface AnyLLMKitThroughSDKMixin<S> extends SelfInterface<S> {
    /**
     * Once named as `useChatGPT`.
     */
    S useAzure(AzureOpenAIServiceMeta azureOpenAIServiceMeta, SupportedModel model);

    /**
     * Once named as `useChatGPT`.
     */
    default S useAzure(AzureOpenAIServiceMeta azureOpenAIServiceMeta) {
        return useAzure(azureOpenAIServiceMeta, SupportedModel.ChatGPT);
    }

    /**
     * Once named as `useQwen`.
     */
    S useDataScope(DashscopeServiceMeta dashscopeServiceMeta, SupportedModel model);

    /**
     * Once named as `useQwen`.
     */
    default S useDataScope(DashscopeServiceMeta dashscopeServiceMeta) {
        return useDataScope(dashscopeServiceMeta, SupportedModel.QwenPlus);
    }

    S useVolces(VolcesServiceMeta volcesServiceMeta, SupportedModel model);

    default S useVolces(VolcesServiceMeta volcesServiceMeta) {
        return useVolces(volcesServiceMeta, SupportedModel.Doubao);
    }
}
