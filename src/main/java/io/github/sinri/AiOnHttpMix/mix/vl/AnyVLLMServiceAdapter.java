package io.github.sinri.AiOnHttpMix.mix.vl;

import io.github.sinri.AiOnHttpMix.utils.ServiceMeta;
import io.github.sinri.AiOnHttpMix.utils.SupportedVLModel;
import io.vertx.core.Future;
import io.vertx.core.Handler;

/**
 * @since 1.3.0
 */
public interface AnyVLLMServiceAdapter {
    static AnyVLLMServiceAdapter throughSDK(ServiceMeta serviceMeta, SupportedVLModel vlModel) {
        return new AnyVLLMServiceAdapterThroughSDK(serviceMeta, vlModel);
    }

    // todo through Mirage

    Future<AnyVLLMResponse> request(AnyVLLMRequest request);

    default Future<AnyVLLMResponse> request(Handler<AnyVLLMRequest> requestHandler) {
        AnyVLLMRequest anyVLLMRequest = AnyVLLMRequest.create();
        requestHandler.handle(anyVLLMRequest);
        return request(anyVLLMRequest);
    }
}
