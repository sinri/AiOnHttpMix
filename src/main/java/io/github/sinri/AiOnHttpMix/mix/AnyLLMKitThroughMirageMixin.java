package io.github.sinri.AiOnHttpMix.mix;

import io.github.sinri.AiOnHttpMix.mirage.MirageSDK;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.github.sinri.keel.core.SelfInterface;
import io.github.sinri.keel.core.TechnicalPreview;

public interface AnyLLMKitThroughMirageMixin<S> extends SelfInterface<S> {
    /**
     * @since 1.2.2 if test passed, deprecated other detailed use-methods.
     */
    @TechnicalPreview(since = "1.2.2")
    S useMirageSDK(MirageSDK mirageSDK, SupportedModel supportedModel);

    /**
     * Once named as `useChatGPT`.
     */
    S useAzure(MirageSDK mirageSDK, SupportedModel model);

    /**
     * Once named as `useChatGPT`.
     */
    @Deprecated(since = "1.2.2")
    default S useAzure(MirageSDK mirageSDK) {
        return useAzure(mirageSDK, SupportedModel.ChatGPT);
    }

    /**
     * Once named as `useQwen`.
     */
    S useDashScope(MirageSDK mirageSDK, SupportedModel model);

    /**
     * Once named as `useQwen`.
     */
    @Deprecated(since = "1.2.2")
    default S useDashScope(MirageSDK mirageSDK) {
        return useDashScope(mirageSDK, SupportedModel.QwenPlus);
    }

    S useVolces(MirageSDK mirageSDK, SupportedModel model);

    @Deprecated(since = "1.2.2")
    default S useVolces(MirageSDK mirageSDK) {
        return useVolces(mirageSDK, SupportedModel.Doubao);
    }

    /**
     * @since 1.2.2
     */
    S useDeepSeek(MirageSDK mirageSDK, SupportedModel model);

    /**
     * @since 1.2.2
     */
    @Deprecated(since = "1.2.2")
    default S useDeepSeek(MirageSDK mirageSDK) {
        return useDeepSeek(mirageSDK, SupportedModel.DeepSeekReasoner);
    }
}
