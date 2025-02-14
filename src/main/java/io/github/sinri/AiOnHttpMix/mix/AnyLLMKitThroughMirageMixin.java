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
}
