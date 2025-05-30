package io.github.sinri.AiOnHttpMix.mix.chat;

import io.github.sinri.AiOnHttpMix.mix.chat.request.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.chat.response.MixChatResponse;
import io.github.sinri.AiOnHttpMix.mix.chat.response.stream.MixChatResponseChunk;
import io.github.sinri.AiOnHttpMix.mix.service.MixServiceAdapter;
import io.github.sinri.AiOnHttpMix.mix.tools.MixFunctionAdapter;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

class MixChatKitImpl implements MixChatKit {
    private final Map<String, MixFunctionAdapter> functionAdapterMap = new HashMap<>();
    private MixServiceAdapter adapter;

    public MixChatKitImpl() {
        super();
    }

    public MixChatKitImpl(MixServiceAdapter adapter) {
        super();
        this.adapter = adapter;
    }

    /**
     * 获取当前绑定的 MixServiceAdapter。
     *
     * @return 当前的 MixServiceAdapter 实例
     */
    @Nonnull
    @Override
    public MixServiceAdapter getAdapter() {
        Objects.requireNonNull(adapter, "The adapter is not set yet.");
        return adapter;
    }

    /**
     * 设置 MixServiceAdapter。
     *
     * @param adapter 需要绑定的 MixServiceAdapter 实例
     * @return 当前 MixChatKit 实例，便于链式调用
     */
    @Override
    public MixChatKit setAdapter(@Nonnull MixServiceAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

    /**
     * 发起一次标准的聊天请求。
     *
     * @param request 聊天请求参数
     * @return 异步返回 MixChatResponse
     */
    @Override
    public Future<MixChatResponse> chat(MixChatRequest request) {
        return getAdapter().request(request);
    }

    /**
     * 发起一次流式聊天请求，处理每个片段数据。
     *
     * @param request             聊天请求参数，方法内会自动设置为流式
     * @param fragmentDataHandler 处理每个流式片段的回调函数，参数为 JsonObject，返回 Future<Void>
     * @return 异步返回 Void，表示流式处理完成
     */
    @Override
    public Future<Void> chatStreamRaw(MixChatRequest request, Function<JsonObject, Future<Void>> fragmentDataHandler) {
        request.setStream(true);
        return getAdapter().requestStreamRaw(request, fragmentDataHandler);
    }

    @Override
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
    @Override
    public Future<MixChatResponse> chatStream(MixChatRequest request) {
        request.setStream(true);
        return getAdapter().requestStream(request);
    }

    @Override
    public Future<MixChatResponse> chatStreamRaw(MixChatRequest request) {
        request.setStream(true);
        return getAdapter().requestStreamRaw(request);
    }

    @Override
    public MixChatKit registerFunction(MixFunctionAdapter functionAdapter) {
        functionAdapterMap.put(functionAdapter.getFunctionName(), functionAdapter);
        return this;
    }

    @Override
    @Nullable
    public MixFunctionAdapter getRegisteredFunction(String functionName) {
        return functionAdapterMap.get(functionName);
    }

    @Override
    public MixChatKit clearRegisterFunctions() {
        functionAdapterMap.clear();
        return this;
    }
}
