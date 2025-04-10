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

    /**
     * @param request         请求
     * @param fragmentHandler 针对一个已经格式化好的SSE Chunk的JSON对象字符串表达的处理器
     * @since 1.1.3
     */
    Future<Void> request(AnyVLLMRequest request, Handler<String> fragmentHandler);

    default Future<AnyVLLMResponse> requestWithStreamBuffer(Handler<AnyVLLMRequest> requestHandler) {
        AnyVLLMRequest anyVLLMRequest = AnyVLLMRequest.create();
        requestHandler.handle(anyVLLMRequest);
        return requestWithStreamBuffer(anyVLLMRequest);
    }

    Future<AnyVLLMResponse> requestWithStreamBuffer(AnyVLLMRequest request);
}
