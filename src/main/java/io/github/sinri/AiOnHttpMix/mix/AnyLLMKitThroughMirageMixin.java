package io.github.sinri.AiOnHttpMix.mix;

import io.github.sinri.AiOnHttpMix.mirage.MirageSDK;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.github.sinri.keel.core.SelfInterface;

public interface AnyLLMKitThroughMirageMixin<S> extends SelfInterface<S> {
    /**
     * Once named as `useChatGPT`.
     */
    S useAzure(MirageSDK mirageSDK, SupportedModel model);

    /**
     * Once named as `useChatGPT`.
     */
    default S useAzure(MirageSDK mirageSDK) {
        return useAzure(mirageSDK, SupportedModel.ChatGPT);
    }

    /**
     * Once named as `useQwen`.
     */
    S useDataScope(MirageSDK mirageSDK, SupportedModel model);

    /**
     * Once named as `useQwen`.
     */
    default S useDataScope(MirageSDK mirageSDK) {
        return useDataScope(mirageSDK, SupportedModel.QwenPlus);
    }

    S useVolces(MirageSDK mirageSDK, SupportedModel model);

    default S useVolces(MirageSDK mirageSDK) {
        return useVolces(mirageSDK, SupportedModel.Doubao);
    }
}
