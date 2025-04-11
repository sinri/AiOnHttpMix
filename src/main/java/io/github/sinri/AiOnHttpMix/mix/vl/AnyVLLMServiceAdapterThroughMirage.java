package io.github.sinri.AiOnHttpMix.mix.vl;

import io.github.sinri.AiOnHttpMix.mirage.chat.MirageSDK;
import io.github.sinri.AiOnHttpMix.utils.SupportedVLModel;
import io.vertx.core.Future;
import io.vertx.core.Handler;

/**
 * @since 1.3.0
 */
public class AnyVLLMServiceAdapterThroughMirage implements AnyVLLMServiceAdapter {
    /**
     * Used by both SDK or Mirage.
     */
    private final SupportedVLModel model;
    /**
     * Used by Mirage only.
     */
    private final MirageSDK mirageSDK;
    /**
     * Used by Mirage only.
     */
    private final String mirageModel;
    /**
     * Used by Mirage only.
     */
    private final String mirageService;

    AnyVLLMServiceAdapterThroughMirage(MirageSDK mirageSDK, SupportedVLModel supportedModel) {
        this.mirageSDK = mirageSDK;
        this.model = supportedModel;
        this.mirageModel = model.name();
        this.mirageService = generateMirageService(supportedModel);
    }

    private String generateMirageService(SupportedVLModel supportedModel) {
        return switch (supportedModel) {
            case DoubaoVL -> "doubao-1.5-vision-pro-32k";
            case QwenVLPlus, QwenVLMax -> null;
        };
    }

    @Override
    public Future<AnyVLLMResponse> request(AnyVLLMRequest request) {
        // todo
        throw new RuntimeException("not implemented");
        //        mirageSDK.requestSync(request.)
    }

    @Override
    public Future<Void> request(AnyVLLMRequest request, Handler<String> fragmentHandler) {
        // todo
        throw new RuntimeException("not implemented");
    }

    @Override
    public Future<AnyVLLMResponse> requestWithStreamBuffer(AnyVLLMRequest request) {
        // todo
        throw new RuntimeException("not implemented");
    }
}
