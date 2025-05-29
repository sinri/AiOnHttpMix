package io.github.sinri.AiOnHttpMix.mix.chat;

import io.github.sinri.AiOnHttpMix.mix.chat.request.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.chat.response.MixChatResponse;
import io.github.sinri.AiOnHttpMix.mix.chat.response.stream.MixChatResponseChunk;
import io.github.sinri.AiOnHttpMix.mix.service.MixServiceAdapter;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.function.Function;

/**
 * MixChatKit 是混合聊天服务的工具类，封装了与 MixServiceAdapter 的交互，
 * 提供了标准的聊天请求、流式聊天请求等方法，便于上层调用。
 * <p>
 * 典型用法：
 * 
 * <pre>
 * MixChatKit kit = new MixChatKit().setAdapter(adapter);
 * Future<MixChatResponse> response = kit.chat(request);
 * </pre>
 */
public class MixChatKit {
    private MixServiceAdapter adapter;

    /**
     * 构造方法。
     */
    public MixChatKit() {

    }

    /**
     * 获取当前绑定的 MixServiceAdapter。
     * 
     * @return 当前的 MixServiceAdapter 实例
     */
    public MixServiceAdapter getAdapter() {
        return adapter;
    }

    /**
     * 设置 MixServiceAdapter。
     * 
     * @param adapter 需要绑定的 MixServiceAdapter 实例
     * @return 当前 MixChatKit 实例，便于链式调用
     */
    public MixChatKit setAdapter(MixServiceAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

    /**
     * 发起一次标准的聊天请求。
     * 
     * @param request 聊天请求参数
     * @return 异步返回 MixChatResponse
     */
    public Future<MixChatResponse> chat(MixChatRequest request) {
        Objects.requireNonNull(adapter, "adapter is not set");
        return adapter.request(request);
    }

    /**
     * 发起一次流式聊天请求，处理每个片段数据。
     * 
     * @param request             聊天请求参数，方法内会自动设置为流式
     * @param fragmentDataHandler 处理每个流式片段的回调函数，参数为 JsonObject，返回 Future<Void>
     * @return 异步返回 Void，表示流式处理完成
     */
    public Future<Void> chatStreamRaw(MixChatRequest request, Function<JsonObject, Future<Void>> fragmentDataHandler) {
        Objects.requireNonNull(adapter, "adapter is not set");
        request.setStream(true);
        return adapter.requestStreamRaw(request, fragmentDataHandler);
    }

    public Future<Void> chatStream(MixChatRequest request, Function<MixChatResponseChunk, Future<Void>> chunkHandler) {
        Objects.requireNonNull(adapter, "adapter is not set");
        request.setStream(true);
        return adapter.requestStream(request, chunkHandler);
    }

    /**
     * 发起一次流式聊天请求，返回完整响应。
     * 
     * @param request 聊天请求参数，方法内会自动设置为流式
     * @return 异步返回 MixChatResponse，包含完整响应内容
     */
    public Future<MixChatResponse> chatStream(MixChatRequest request) {
        Objects.requireNonNull(adapter, "adapter is not set");
        request.setStream(true);
        return adapter.requestStreamRaw(request);
    }
}
