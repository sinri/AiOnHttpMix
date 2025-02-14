package io.github.sinri.AiOnHttpMix.mix;

import io.github.sinri.AiOnHttpMix.utils.ServiceMeta;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.github.sinri.keel.core.SelfInterface;
import io.github.sinri.keel.core.TechnicalPreview;

public interface AnyLLMKitThroughSDKMixin<S> extends SelfInterface<S> {
    /**
     * @since 1.2.2 if test passed, deprecated other detailed use-methods.
     */
    @TechnicalPreview(since = "1.2.2")
    S useServiceMeta(ServiceMeta serviceMeta, SupportedModel supportedModel);
}
