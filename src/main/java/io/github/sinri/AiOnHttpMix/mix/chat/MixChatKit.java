package io.github.sinri.AiOnHttpMix.mix.chat;

import io.github.sinri.AiOnHttpMix.mix.chat.request.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.chat.response.MixChatResponse;
import io.github.sinri.AiOnHttpMix.mix.chat.response.stream.MixChatResponseChunk;
import io.github.sinri.AiOnHttpMix.mix.service.MixServiceAdapter;
import io.github.sinri.AiOnHttpMix.mix.tools.MixFunctionAdapter;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
public interface MixChatKit {

    static MixChatKit create() {
        return new MixChatKitImpl();
    }

    static MixChatKit create(MixServiceAdapter mixServiceAdapter) {
        return new MixChatKitImpl(mixServiceAdapter);
    }

    /**
     * 获取当前绑定的 MixServiceAdapter。
     *
     * @return 当前的 MixServiceAdapter 实例
     * @throws NullPointerException when adapter is null
     */
    @Nonnull
    MixServiceAdapter getAdapter();

    /**
     * 设置 MixServiceAdapter。
     *
     * @param adapter 需要绑定的 MixServiceAdapter 实例
     * @return 当前 MixChatKit 实例，便于链式调用
     */
    MixChatKit setAdapter(@Nonnull MixServiceAdapter adapter);

    /**
     * 发起一次标准的聊天请求。
     *
     * @param request 聊天请求参数
     * @return 异步返回 MixChatResponse
     */
    Future<MixChatResponse> chat(MixChatRequest request);

    default Future<MixChatResponse> chat(Handler<MixChatRequest> requestHandler) {
        MixChatRequest request = MixChatRequest.create();
        requestHandler.handle(request);
        return chat(request);
    }

    /**
     * 发起一次流式聊天请求，处理每个片段数据。
     *
     * @param request             聊天请求参数，方法内会自动设置为流式
     * @param fragmentDataHandler 处理每个流式片段的回调函数，参数为 JsonObject，返回 Future<Void>
     * @return 异步返回 Void，表示流式处理完成
     */
    Future<Void> chatStreamRaw(MixChatRequest request, Function<JsonObject, Future<Void>> fragmentDataHandler);

    default Future<Void> chatStreamRaw(Handler<MixChatRequest> requestHandler, Function<JsonObject, Future<Void>> fragmentDataHandler) {
        MixChatRequest request = MixChatRequest.create();
        requestHandler.handle(request);
        return chatStreamRaw(request, fragmentDataHandler);
    }

    Future<Void> chatStream(MixChatRequest request, Function<MixChatResponseChunk, Future<Void>> chunkHandler);

    default Future<Void> chatStream(Handler<MixChatRequest> requestHandler, Function<MixChatResponseChunk, Future<Void>> chunkHandler) {
        MixChatRequest request = MixChatRequest.create();
        requestHandler.handle(request);
        return chatStream(request, chunkHandler);
    }

    /**
     * 发起一次流式聊天请求，返回完整响应。
     *
     * @param request 聊天请求参数，方法内会自动设置为流式
     * @return 异步返回 MixChatResponse，包含完整响应内容
     */
    Future<MixChatResponse> chatStream(MixChatRequest request);

    default Future<MixChatResponse> chatStream(Handler<MixChatRequest> requestHandler) {
        MixChatRequest request = MixChatRequest.create();
        requestHandler.handle(request);
        return chatStream(request);
    }

    Future<MixChatResponse> chatStreamRaw(MixChatRequest request);

    default Future<MixChatResponse> chatStreamRaw(Handler<MixChatRequest> requestHandler) {
        MixChatRequest request = MixChatRequest.create();
        requestHandler.handle(request);
        return chatStreamRaw(request);
    }

    MixChatKit registerFunction(MixFunctionAdapter functionAdapter);

    @Nullable
    MixFunctionAdapter getRegisteredFunction(String functionName);

    MixChatKit clearRegisterFunctions();
}
