package io.github.sinri.AiOnHttpMix.mix.service;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.mirage.MirageConfigElement;
import io.github.sinri.AiOnHttpMix.mirage.MirageKit;
import io.github.sinri.AiOnHttpMix.mix.chat.request.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.chat.response.MixChatResponse;
import io.github.sinri.AiOnHttpMix.mix.chat.response.stream.MixChatResponseChunk;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.stream.GPTResponseBuffer;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.stream.GPTResponseChunk;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.stream.QwenResponseBuffer;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.stream.QwenResponseChunk;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.stream.DoubaoResponseBuffer;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.stream.DoubaoResponseChunk;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.specification.DashscopeModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.GPTModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.VolcesModelSpecification;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.function.Function;

/**
 * MirageMixServiceAdapter 适配器类，用于将 MirageKit 集成到混合服务中。
 * <p>
 * 该类继承自 MixServiceAdapter，负责将 MirageKit 的能力适配为统一的聊天请求处理接口，
 * 支持同步和流式的聊天请求，兼容多种模型（如 GPT、Volces、Dashscope）。
 * <ul>
 *   <li>构造时从配置中提取 mirage 配置并初始化 MirageKit。</li>
 *   <li>request 方法用于同步请求，直接调用 MirageKit 的 requestSync。</li>
 *   <li>requestStream 方法支持两种重载：
 *     <ul>
 *       <li>一种接收分片数据处理函数，适合自定义流式处理。</li>
 *       <li>一种根据模型类型自动组装完整响应，适合直接获取最终响应对象。</li>
 *     </ul>
 *   </li>
 * </ul>
 * <b>注意：</b>仅支持 GPT、Volces、Dashscope 相关模型，否则抛出 IllegalArgumentException。
 */
public class MirageMixServiceAdapter extends MixServiceAdapter {
    private final MirageKit mirageKit;

    /**
     * 构造函数，根据配置初始化 MirageKit。
     *
     * @param config 配置对象，需包含 mirage 配置项
     * @throws NullPointerException 如果 mirage 配置不存在
     */
    public MirageMixServiceAdapter(KeelConfigElement config) {
        super(config);
        KeelConfigElement x = config.extract("mirage");
        Objects.requireNonNull(x);
        MirageConfigElement mirageConfigElement = new MirageConfigElement(x);
        this.mirageKit = new MirageKit(mirageConfigElement);
    }

    /**
     * 同步请求接口，直接调用 MirageKit 的 requestSync。
     *
     * @param request 聊天请求对象
     * @return 返回 MixChatResponse 的 Future
     */
    @Override
    public Future<MixChatResponse> request(MixChatRequest request) {
        return mirageKit.requestSync(true, request);
    }

    /**
     * 流式请求接口，支持自定义分片数据处理。
     *
     * @param request             聊天请求对象
     * @param fragmentDataHandler 分片数据处理函数，接收 JsonObject 类型的分片数据
     * @return 返回处理完成的 Future<Void>
     */
    @Override
    public Future<Void> requestStreamRaw(MixChatRequest request, Function<JsonObject, Future<Void>> fragmentDataHandler) {
        return mirageKit.requestStreamRaw(true, request, fragmentData -> {
            AigcMix.getVerboseLogger()
                   .debug("MirageMixServiceAdapter.requestStreamRaw with fragment data: \n" + fragmentData);
            try {
                Objects.requireNonNull(fragmentData);
                var j = new JsonObject(fragmentData);
                return fragmentDataHandler.apply(j);
            } catch (Throwable throwable) {
                AigcMix.getVerboseLogger().exception(throwable);
                return Future.succeededFuture();
            }
        });
    }

    @Override
    public Future<Void> requestStream(MixChatRequest request, Function<MixChatResponseChunk, Future<Void>> chunkHandler) {
        return mirageKit.requestStream(true, request, chunk -> {
            AigcMix.getVerboseLogger()
                   .debug("MirageMixServiceAdapter.requestStream with chunk", chunk.toJsonObject());
            return chunkHandler.apply(chunk);
        });
    }

    /**
     * 流式请求接口，根据模型类型自动组装完整响应。
     * <p>
     * 支持 GPT、Volces、Dashscope 相关模型，自动组装响应内容。
     *
     * @param request 聊天请求对象
     * @return 返回 MixChatResponse 的 Future
     * @throws IllegalArgumentException 如果模型类型不被支持
     */
    @Override
    public Future<MixChatResponse> requestStreamRaw(MixChatRequest request) {
        ChatModel chatModel = request.getChatModel();
        if (chatModel instanceof GPTModelSpecification) {
            GPTResponseBuffer buffer = new GPTResponseBuffer();
            return mirageKit.requestStreamRaw(
                                    true,
                                    request,
                                    fragmentData -> {
                                        GPTResponseChunk chunk = GPTResponseChunk.wrap(new JsonObject(fragmentData));
                                        buffer.accept(chunk);
                                        return Future.succeededFuture();
                                    }
                            )
                            .compose(v -> {
                                var x = MixChatResponse.from(buffer.build());
                                return Future.succeededFuture(x);
                            });
        } else if (chatModel instanceof VolcesModelSpecification) {
            DoubaoResponseBuffer buffer = new DoubaoResponseBuffer();
            return mirageKit.requestStreamRaw(
                                    true,
                                    request,
                                    fragmentData -> {
                                        DoubaoResponseChunk chunk = DoubaoResponseChunk.wrap(new JsonObject(fragmentData));
                                        buffer.accept(chunk);
                                        return Future.succeededFuture();
                                    }
                            )
                            .compose(v -> {
                                var x = MixChatResponse.from(buffer.build());
                                return Future.succeededFuture(x);
                            });
        } else if (chatModel instanceof DashscopeModelSpecification) {
            QwenResponseBuffer buffer = new QwenResponseBuffer();
            return mirageKit.requestStreamRaw(
                                    true,
                                    request,
                                    fragmentData -> {
                                        QwenResponseChunk chunk = QwenResponseChunk.wrap(new JsonObject(fragmentData));
                                        buffer.accept(chunk);
                                        return Future.succeededFuture();
                                    }
                            )
                            .compose(v -> {
                                var x = MixChatResponse.from(buffer.build());
                                return Future.succeededFuture(x);
                            });
        } else {
            throw new IllegalArgumentException("model is not supported");
        }
    }
}
