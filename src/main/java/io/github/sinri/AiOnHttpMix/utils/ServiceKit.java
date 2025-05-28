package io.github.sinri.AiOnHttpMix.utils;

import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.function.Function;

/**
 * 服务套件。
 * 
 * @param <I> 请求实体类，即Request
 * @param <O> 响应实体类，即Response
 * @param <C> 流式报文传输中的数据块实体类，即Chunk
 */
public interface ServiceKit<I extends UnmodifiableJsonifiableEntity, O extends UnmodifiableJsonifiableEntity, C extends UnmodifiableJsonifiableEntity> {
        /**
         * 获取服务适配器。
         * 
         * @return 服务适配器
         */
        ServiceAdapter getServiceAdapter();

        /**
         * 发送文本聊天请求。
         * 
         * @param chatModel  聊天模型
         * @param rawRequest 原始请求
         * @param requestId  请求ID
         * @return 响应Future
         */
        default Future<JsonObject> chat(
                        ChatModel chatModel,
                        JsonObject rawRequest,
                        String requestId) {
                return getServiceAdapter().request(
                                chatModel,
                                rawRequest,
                                requestId);
        }

        /**
         * 发送文本聊天请求。
         * 
         * @param chatModel 聊天模型
         * @param request   请求
         * @param requestId 请求ID
         * @return 响应Future
         */
        Future<O> chat(
                        ChatModel chatModel,
                        I request,
                        String requestId);

        /**
         * 发送文本聊天请求。
         * 
         * @param chatModel         聊天模型
         * @param requestPayload    请求体
         * @param cutterProcessFunc 流式报文原始文本处理函数
         * @param cutterTimeout     流式报文处理超时时间
         * @param requestId         请求ID
         * @return 响应Future
         */
        Future<Void> chatStream(
                        ChatModel chatModel,
                        JsonObject requestPayload,
                        Function<String, Future<Void>> cutterProcessFunc,
                        long cutterTimeout,
                        String requestId);

        /**
         * 发送文本聊天请求。
         * 
         * @param chatModel         聊天模型
         * @param request           请求
         * @param cutterProcessFunc 流式报文原始文本处理函数
         * @param cutterTimeout     流式报文处理超时时间
         * @param requestId         请求ID
         * @return 响应Future
         */
        Future<Void> chatStream(
                        ChatModel chatModel,
                        I request,
                        Function<C, Future<Void>> cutterProcessFunc,
                        long cutterTimeout,
                        String requestId);

        /**
         * 发送文本聊天请求。
         * 
         * @param chatModel     聊天模型
         * @param request       请求
         * @param cutterTimeout 流式报文处理超时时间
         * @param requestId     请求ID
         * @return 响应Future
         */
        Future<O> chatStream(
                        ChatModel chatModel,
                        I request,
                        long cutterTimeout,
                        String requestId);
}
