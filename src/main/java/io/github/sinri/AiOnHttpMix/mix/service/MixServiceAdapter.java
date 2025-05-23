package io.github.sinri.AiOnHttpMix.mix.service;

import io.github.sinri.AiOnHttpMix.mix.chat.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.chat.MixChatResponse;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.function.Function;

public abstract class MixServiceAdapter {
    private final KeelConfigElement config;

    public MixServiceAdapter(KeelConfigElement config) {
        this.config = config;
    }

    protected KeelConfigElement getConfig() {
        return config;
    }

    abstract public Future<MixChatResponse> request(MixChatRequest request);

    /**
     * @param request         请求
     * @param fragmentHandler 针对一个已经格式化好的SSE Chunk的JSON对象字符串表达的处理器
     * @since 1.1.3
     */
    abstract public Future<Void> requestStream(MixChatRequest request, Function<String, Future<Void>> fragmentHandler);

    public Future<MixChatResponse> requestStream(Handler<MixChatRequest> requestHandler) {
        MixChatRequest request = MixChatRequest.create();
        requestHandler.handle(request);
        return requestStream(request);
    }

    abstract public Future<MixChatResponse> requestStream(MixChatRequest request);
}
