package io.github.sinri.AiOnHttpMix.mix;

import io.github.sinri.AiOnHttpMix.azure.openai.core.AzureOpenAIServiceMeta;
import io.github.sinri.AiOnHttpMix.dashscope.core.DashscopeServiceMeta;
import io.github.sinri.AiOnHttpMix.deepseek.core.DeepseekServiceMeta;
import io.github.sinri.AiOnHttpMix.utils.ServiceMeta;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.github.sinri.AiOnHttpMix.volces.core.VolcesServiceMeta;
import io.github.sinri.keel.core.SelfInterface;
import io.github.sinri.keel.core.TechnicalPreview;

public interface AnyLLMKitThroughSDKMixin<S> extends SelfInterface<S> {
    /**
     * @since 1.2.2 if test passed, deprecated other detailed use-methods.
     */
    @TechnicalPreview(since = "1.2.2")
    S useServiceMeta(ServiceMeta serviceMeta, SupportedModel supportedModel);

    /**
     * Once named as `useChatGPT`.
     */
    S useAzure(AzureOpenAIServiceMeta azureOpenAIServiceMeta, SupportedModel model);

    /**
     * Once named as `useChatGPT`.
     */
    @Deprecated(since = "1.2.2")
    default S useAzure(AzureOpenAIServiceMeta azureOpenAIServiceMeta) {
        return useAzure(azureOpenAIServiceMeta, SupportedModel.ChatGPT);
    }

    /**
     * Once named as `useQwen`.
     */
    S useDashScope(DashscopeServiceMeta dashscopeServiceMeta, SupportedModel model);

    /**
     * Once named as `useQwen`.
     */
    @Deprecated(since = "1.2.2")
    default S useDashScope(DashscopeServiceMeta dashscopeServiceMeta) {
        return useDashScope(dashscopeServiceMeta, SupportedModel.QwenPlus);
    }

    S useVolces(VolcesServiceMeta volcesServiceMeta, SupportedModel model);

    @Deprecated(since = "1.2.2")
    default S useVolces(VolcesServiceMeta volcesServiceMeta) {
        return useVolces(volcesServiceMeta, SupportedModel.Doubao);
    }

    /**
     * @since 1.2.2
     */
    S useDeepSeek(DeepseekServiceMeta deepseekServiceMeta, SupportedModel model);

    /**
     * @since 1.2.2
     */
    @Deprecated(since = "1.2.2")
    default S useDeepSeek(DeepseekServiceMeta deepseekServiceMeta) {
        return useDeepSeek(deepseekServiceMeta, SupportedModel.DeepSeekReasoner);
    }

}
