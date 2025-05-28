package io.github.sinri.AiOnHttpMix.utils;

import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;
import io.github.sinri.keel.core.cutter.IntravenouslyCutterOnString;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Function;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

/**
 * 大语言模型服务适配器。
 * 一个大语言模型服务适配器针对特定的提供服务的大语言模型在线服务提供商，支持若干相关大语言模型服务接口规格，基于HTTP接口实现大模型推理服务。
 *
 * @since 2.0.0
 */
public interface ServiceAdapter {

    @Nullable
    static String extractFragmentData(String fragment) {
        var lines = fragment.split("[\r\n]+");
        for (var line : lines) {
            var pair = line.split(":\\s*", 2);
            if (pair.length == 2) {
                if (Objects.equals(pair[0], "data")) {
                    return pair[1];
                }
            }
        }
        return null;
    }

    static Future<Void> callStreamWithCutter(
            HttpClientOptions httpClientOptions,
            Function<HttpClient, Future<HttpClientResponse>> requestFunction,
            Function<String, Future<Void>> cutterProcessFunc,
            long cutterTimeout
    ) {
        return Keel.useHttpClient(
                httpClientOptions,
                client -> Future.succeededFuture()
                                .compose(v -> requestFunction.apply(client))
                                .compose(httpClientResponse -> {
                                    IntravenouslyCutterOnString cutter = new IntravenouslyCutterOnString(cutterProcessFunc::apply, cutterTimeout);
                                    httpClientResponse
                                            .handler(cutter::acceptFromStream)
                                            .endHandler(ended -> cutter.stopHere())
                                            .exceptionHandler(cutter::stopHere);
                                    return cutter.waitForAllHandled();
                                }));
    }

    /**
     * @param chatModel 一个模型（系列）
     * @return 指定的模型（系列）是否与本大语言模型服务适配器兼容
     */
    boolean isModelCompatible(ChatModel chatModel);

    /**
     * @param modelSpecification 一个大语言模型接口规格
     * @return 指定的大语言模型接口规格是否与本大语言模型服务适配器兼容
     */
    boolean isModelCompatible(ModelSpecification modelSpecification);

    /**
     * 给定一个模型（系列）或一个大语言模型接口规格，如果其与本大语言模型服务适配器不兼容，则抛出IllegalArgumentException。
     *
     * @param chatModel 一个模型（系列）或一个大语言模型接口规格
     * @throws IllegalArgumentException 不兼容的模型
     */
    default void assertModelCompatible(ChatModel chatModel) {
        if (!isModelCompatible(chatModel)) {
            throw new IllegalArgumentException("Model " + chatModel.getModelName() + " is not compatible with this service adapter.");
        }
    }

    /**
     * 发起调用请求进行大模型推理，并同步获取大模型推理结果。
     *
     * @param chatModel      指定模型
     * @param requestPayload 请求体
     * @param requestId      请求ID
     * @return 大模型推理结果
     */
    Future<JsonObject> request(
            ChatModel chatModel,
            JsonObject requestPayload,
            String requestId
    );

    /**
     * 发起调用请求进行大模型推理，并通过SSE的方式，流式获取大模型推理结果。
     *
     * @param chatModel         指定模型
     * @param requestPayload    请求体
     * @param cutterProcessFunc SSE流式报文的异步接收处理方法
     * @param cutterTimeout     超时时间（毫秒）
     * @param requestId         请求ID
     * @return 大模型推理结束
     */
    Future<Void> requestStream(
            ChatModel chatModel,
            JsonObject requestPayload,
            Function<String, Future<Void>> cutterProcessFunc,
            long cutterTimeout,
            String requestId
    );

}
