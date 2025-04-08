package io.github.sinri.AiOnHttpMix.mix.vl;

import io.vertx.core.Future;

import java.util.Objects;

/**
 * @since 1.3.0
 */
public class AnyVLLMKit {

    private AnyVLLMServiceAdapter serviceAdapter;

    public AnyVLLMKit() {
        serviceAdapter = null;
    }

    public AnyVLLMServiceAdapter getServiceAdapter() {
        Objects.requireNonNull(serviceAdapter, "Set service adapter before use!");
        return serviceAdapter;
    }

    public AnyVLLMKit setServiceAdapter(AnyVLLMServiceAdapter serviceAdapter) {
        this.serviceAdapter = serviceAdapter;
        return this;
    }

    public Future<AnyVLLMResponse> request(AnyVLLMRequest request) {
        return this.getServiceAdapter().request(request);
    }
}
