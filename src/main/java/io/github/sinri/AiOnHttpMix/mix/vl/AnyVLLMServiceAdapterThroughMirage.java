package io.github.sinri.AiOnHttpMix.mix.vl;

import io.vertx.core.Future;
import io.vertx.core.Handler;

/**
 * @since 1.3.0
 */
public class AnyVLLMServiceAdapterThroughMirage implements AnyVLLMServiceAdapter {
    @Override
    public Future<AnyVLLMResponse> request(AnyVLLMRequest request) {
        // todo
        throw new RuntimeException("not implemented");
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
