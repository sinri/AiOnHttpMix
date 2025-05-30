package io.github.sinri.AiOnHttpMix.mix.service;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.mix.chat.request.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.chat.response.MixChatResponse;
import io.github.sinri.AiOnHttpMix.mix.chat.response.stream.MixChatResponseBuffer;
import io.github.sinri.AiOnHttpMix.mix.chat.response.stream.MixChatResponseChunk;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import java.util.function.Function;

/**
 * MixServiceAdapter 是 Mix 服务适配器的抽象基类，
 * 定义了 Mix 服务的基本请求和响应处理接口。
 * <p>
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
     * 以标准格式发送同步请求，并获取以标准格式化后的响应对象。
     *
     * @param request 标准格式请求
     * @return 响应对象的 Future
     */
    abstract public Future<MixChatResponse> request(MixChatRequest request);

    /**
     * 以标准格式发送同步请求，并获取以标准格式化后的响应对象。
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
     * 以标准格式发送流式请求，并针对各模型分类处理每个SSE报文片段中的Fragment Data解析得的Json Object对象。
     *
     * @param request             标准格式请求
     * @param fragmentDataHandler 针对一个已经格式化好的SSE Chunk的JSON对象字符串表达的处理器，每种模型返回的Fragment Data格式不同
     */
    abstract public Future<Void> requestStreamRaw(MixChatRequest request,
                                                  Function<JsonObject, Future<Void>> fragmentDataHandler);

    /**
     * 以标准格式发送流式请求，并获取针对各模型分类处理聚合后的标准格式化后的响应对象。
     *
     * @param requestHandler 请求处理器
     */
    public Future<MixChatResponse> requestStreamRaw(Handler<MixChatRequest> requestHandler) {
        MixChatRequest request = MixChatRequest.create();
        requestHandler.handle(request);
        return requestStreamRaw(request);
    }

    /**
     * 以标准格式发送流式请求，并获取针对各模型分类处理聚合后的标准格式化后的响应对象。
     *
     * @param request 标准格式请求
     * @return 响应对象的 Future
     */
    abstract public Future<MixChatResponse> requestStreamRaw(MixChatRequest request);

    /**
     * 以标准格式发送流式请求，并将每个SSE报文片段中的Fragment Data解析得的Json Object对象统一为标准Chunk格式实体后进行处理。
     *
     * @param request      标准格式请求
     * @param chunkHandler 针对一个已经格式化好的SSE Chunk的JSON对象字符串表达的处理器，以标准Chunk格式统一每种模型返回的不同Fragment Data格式
     */
    public abstract Future<Void> requestStream(MixChatRequest request, Function<MixChatResponseChunk, Future<Void>> chunkHandler);

    /**
     * 以标准格式发送流式请求，并获取按照统一格式化处理后的Chunk们聚合后的标准格式化后的响应对象。
     *
     * @param request 标准格式请求
     */
    public Future<MixChatResponse> requestStream(MixChatRequest request) {
        MixChatResponseBuffer buffer = new MixChatResponseBuffer();
        return this.requestStream(request, chunk -> {
                       AigcMix.getVerboseLogger().debug("MixServiceAdapter.requestStream handle chunk", chunk.toJsonObject());
                       buffer.accept(chunk);
                       return Future.succeededFuture();
                   })
                   .compose(v -> {
                       return Future.succeededFuture(buffer.build());
                   });
    }
}
