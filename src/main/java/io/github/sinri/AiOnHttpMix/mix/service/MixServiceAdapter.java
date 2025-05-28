package io.github.sinri.AiOnHttpMix.mix.service;

import io.github.sinri.AiOnHttpMix.mix.chat.request.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.chat.response.MixChatResponse;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

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
     * @param fragmentDataHandler 针对一个已经格式化好的SSE Chunk的JSON对象字符串表达的处理器
     * @since 1.1.3
     */
    abstract public Future<Void> requestStream(MixChatRequest request, Function<JsonObject, Future<Void>> fragmentDataHandler);

    public Future<MixChatResponse> requestStream(Handler<MixChatRequest> requestHandler) {
        MixChatRequest request = MixChatRequest.create();
        requestHandler.handle(request);
        return requestStream(request);
    }

    abstract public Future<MixChatResponse> requestStream(MixChatRequest request);
}
