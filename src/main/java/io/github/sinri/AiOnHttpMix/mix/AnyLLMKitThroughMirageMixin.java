package io.github.sinri.AiOnHttpMix.mix;

import io.github.sinri.AiOnHttpMix.mirage.MirageSDK;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.github.sinri.keel.core.SelfInterface;

public interface AnyLLMKitThroughMirageMixin<S> extends SelfInterface<S> {
    S useChatGPT(MirageSDK mirageSDK, SupportedModel model);

    default S useChatGPT(MirageSDK mirageSDK) {
        return useChatGPT(mirageSDK, SupportedModel.ChatGPT);
    }

    S useQwen(MirageSDK mirageSDK, SupportedModel model);

    default S useQwen(MirageSDK mirageSDK) {
        return useQwen(mirageSDK, SupportedModel.QwenPlus);
    }

    S useVolces(MirageSDK mirageSDK);
}
