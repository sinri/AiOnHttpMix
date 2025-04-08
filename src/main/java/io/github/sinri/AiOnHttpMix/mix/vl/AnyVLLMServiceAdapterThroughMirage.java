package io.github.sinri.AiOnHttpMix.mix.vl;

import io.vertx.core.Future;

/**
 * @since 1.3.0
 */
public class AnyVLLMServiceAdapterThroughMirage implements AnyVLLMServiceAdapter {
    @Override
    public Future<AnyVLLMResponse> request(AnyVLLMRequest request) {
        // todo
        throw new RuntimeException("not implemented");
    }
}
