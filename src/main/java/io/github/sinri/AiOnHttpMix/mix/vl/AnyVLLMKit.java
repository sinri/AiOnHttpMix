package io.github.sinri.AiOnHttpMix.mix.vl;

import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

/**
 * @since 1.3.0
 */
public class AnyVLLMKit implements AnyVLLMServiceAdapter {

    private final AnyVLLMServiceAdapter serviceAdapter;

    public AnyVLLMKit(@NotNull AnyVLLMServiceAdapter serviceAdapter) {
        this.serviceAdapter = serviceAdapter;
    }

    public AnyVLLMServiceAdapter getServiceAdapter() {
        return serviceAdapter;
    }

    @Override
    public Future<AnyVLLMResponse> request(AnyVLLMRequest request) {
        return this.getServiceAdapter().request(request);
    }
}
