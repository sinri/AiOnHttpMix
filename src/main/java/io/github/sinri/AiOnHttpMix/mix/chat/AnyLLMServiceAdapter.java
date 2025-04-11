package io.github.sinri.AiOnHttpMix.mix.chat;

import io.github.sinri.AiOnHttpMix.mirage.chat.MirageSDK;
import io.github.sinri.AiOnHttpMix.utils.ServiceMeta;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.vertx.core.Future;
import io.vertx.core.Handler;

/**
 * @since 1.3.0
 */
public interface AnyLLMServiceAdapter {
    static AnyLLMServiceAdapter throughMirage(MirageSDK mirageSDK, SupportedModel supportedModel) {
        return new AnyLLMServiceAdapterThroughMirage(mirageSDK, supportedModel);
    }

    static AnyLLMServiceAdapter throughSDK(ServiceMeta serviceMeta, SupportedModel supportedModel) {
        return new AnyLLMServiceAdapterThroughSDK(serviceMeta, supportedModel);
    }

    Future<AnyLLMResponse> request(AnyLLMRequest request);

    /**
     * @param request         请求
     * @param fragmentHandler 针对一个已经格式化好的SSE Chunk的JSON对象字符串表达的处理器
     * @since 1.1.3
     */
    Future<Void> request(AnyLLMRequest request, Handler<String> fragmentHandler);

    default Future<AnyLLMResponse> requestWithStreamBuffer(Handler<AnyLLMRequest> requestHandler) {
        AnyLLMRequest anyLLMRequest = AnyLLMRequest.create();
        requestHandler.handle(anyLLMRequest);
        return requestWithStreamBuffer(anyLLMRequest);
    }

    Future<AnyLLMResponse> requestWithStreamBuffer(AnyLLMRequest request);
}
