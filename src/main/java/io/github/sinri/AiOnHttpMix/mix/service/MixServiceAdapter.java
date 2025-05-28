package io.github.sinri.AiOnHttpMix.mix.service;

import io.github.sinri.AiOnHttpMix.mix.chat.request.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.chat.response.MixChatResponse;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import java.util.function.Function;

/**
 * MixServiceAdapter 是 Mix 服务适配器的抽象基类，
 * 定义了 Mix 服务的基本请求和响应处理接口。
 * 
 * 子类需要实现具体的请求和响应处理逻辑。
 * 
 * @since 2.0.0
 */
public abstract class MixServiceAdapter {
    private final KeelConfigElement config;

    /**
     * 构造函数，初始化配置。
     *
     * @param config 配置元素
     */
    public MixServiceAdapter(KeelConfigElement config) {
        this.config = config;
    }

    /**
     * 获取配置元素。
     *
     * @return 配置元素
     */
    protected KeelConfigElement getConfig() {
        return config;
    }

    /**
     * 发送同步请求并获取响应对象。
     *
     * @param request 请求对象
     * @return 响应对象的 Future
     */
    abstract public Future<MixChatResponse> request(MixChatRequest request);

    /**
     * 发送同步请求，并获取响应对象。
     * 
     * @param requestHandler 请求处理器
     * @return 响应对象的 Future
     */
    public Future<MixChatResponse> request(Handler<MixChatRequest> requestHandler) {
        MixChatRequest request = MixChatRequest.create();
        requestHandler.handle(request);
        return request(request);
    }

    /**
     * 发送流式请求，并处理每个SSE报文片段中的Fragment Data解析得的Json Object对象。
     * 
     * @param request             请求
     * @param fragmentDataHandler 针对一个已经格式化好的SSE Chunk的JSON对象字符串表达的处理器
     */
    abstract public Future<Void> requestStream(MixChatRequest request,
            Function<JsonObject, Future<Void>> fragmentDataHandler);

    /**
     * 发送流式请求，并获取聚合后的响应对象。
     *
     * @param requestHandler 请求处理器
     */
    public Future<MixChatResponse> requestStream(Handler<MixChatRequest> requestHandler) {
        MixChatRequest request = MixChatRequest.create();
        requestHandler.handle(request);
        return requestStream(request);
    }

    /**
     * 发送流式请求，并获取聚合后的响应对象。
     *
     * @param request 请求对象
     * @return 响应对象的 Future
     */
    abstract public Future<MixChatResponse> requestStream(MixChatRequest request);
}
