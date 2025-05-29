package io.github.sinri.AiOnHttpMix.mix.service;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.mix.chat.request.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.chat.response.MixChatResponse;
import io.github.sinri.AiOnHttpMix.mix.chat.response.stream.MixChatResponseBuffer;
import io.github.sinri.AiOnHttpMix.mix.chat.response.stream.MixChatResponseChunk;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIServiceAdapter;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.GPTKit;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.QwenServiceAdapter;
import io.github.sinri.AiOnHttpMix.provider.volces.VolcesKit;
import io.github.sinri.AiOnHttpMix.provider.volces.VolcesServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.specification.DashscopeModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.GPTModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.VolcesModelSpecification;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.function.Function;

/**
 * NativeMixServiceAdapter 是 MixServiceAdapter 的具体实现，
 * 用于根据不同的模型规范（如 GPT、Volces、Dashscope）
 * 适配并分发聊天请求到对应的服务提供方。
 * <p>
 * 该适配器支持同步和流式的聊天请求，
 * 并根据模型类型自动选择合适的 ServiceAdapter 和 Kit 进行处理。
 * </p>
 *
 * <ul>
 *   <li>支持 GPT（Azure OpenAI）、Volces、Dashscope Qwen 等模型</li>
 *   <li>自动从配置中提取对应服务的配置片段</li>
 *   <li>支持普通请求、流式请求和流式分片处理</li>
 * </ul>
 *
 * @author sinri
 */
public class NativeMixServiceAdapter extends MixServiceAdapter {
    public NativeMixServiceAdapter(KeelConfigElement config) {
        super(config);
    }

    /**
     * 处理普通聊天请求，根据模型类型分发到对应的服务。
     *
     * @param request 聊天请求，包含模型、消息等信息
     * @return Future，异步返回 MixChatResponse
     * @throws IllegalArgumentException 如果模型类型不被支持
     */
    @Override
    public Future<MixChatResponse> request(MixChatRequest request) {
        var chatModel = request.getChatModel();
        if (chatModel instanceof GPTModelSpecification) {
            var serviceAdapter = chatModel.buildServiceAdapter(getConfig().extract("azure", "openai"));
            var serviceKit = new GPTKit((OpenAIServiceAdapter) serviceAdapter);
            return serviceKit.chat(chatModel, request.toGPTRequest(), request.getRequestId())
                             .compose(resp -> {
                                 var x = MixChatResponse.from(resp);
                                 return Future.succeededFuture(x);
                             });
        } else if (chatModel instanceof VolcesModelSpecification) {
            var serviceAdapter = chatModel.buildServiceAdapter(getConfig().extract("volces"));
            var serviceKit = new VolcesKit((VolcesServiceAdapter) serviceAdapter);
            return serviceKit.chat(chatModel, request.toDoubaoRequest(), request.getRequestId())
                             .compose(resp -> {
                                 var x = MixChatResponse.from(resp);
                                 return Future.succeededFuture(x);
                             });
        } else if (chatModel instanceof DashscopeModelSpecification) {
            var serviceAdapter = chatModel.buildServiceAdapter(getConfig().extract("dashscope", "qwen"));
            var serviceKit = new QwenKit((QwenServiceAdapter) serviceAdapter);
            return serviceKit.chat(chatModel, request.toQwenRequest(), request.getRequestId())
                             .compose(resp -> {
                                 var x = MixChatResponse.from(resp);
                                 return Future.succeededFuture(x);
                             });
        } else {
            throw new IllegalArgumentException("model is not supported");
        }
    }

    /**
     * 处理流式聊天请求，将每个分片数据通过 fragmentDataHandler 处理。
     *
     * @param request             聊天请求，包含模型、消息等信息
     * @param fragmentDataHandler 分片数据处理函数，接收每个分片的 JsonObject，返回 Future<Void>
     * @return Future，异步返回处理完成的信号
     * @throws IllegalArgumentException 如果模型类型不被支持
     */
    @Override
    public Future<Void> requestStreamRaw(MixChatRequest request, Function<JsonObject, Future<Void>> fragmentDataHandler) {
        var chatModel = request.getChatModel();
        if (chatModel instanceof GPTModelSpecification) {
            var serviceAdapter = chatModel.buildServiceAdapter(getConfig().extract("azure", "openai"));
            var serviceKit = new GPTKit((OpenAIServiceAdapter) serviceAdapter);
            return serviceKit.chatStream(
                    chatModel,
                    request.toGPTRequest().toJsonObject(),
                    fragment -> {
                        return GPTKit.parseStreamFragmentToChunk(fragment)
                                     .compose(chunk -> {
                                         if (chunk != null) {
                                             return fragmentDataHandler.apply(chunk.cloneAsJsonObject());
                                         } else {
                                             AigcMix.getVerboseLogger()
                                                    .warning("chunk parsed from this fragment is null: " + fragment);
                                         }
                                         return Future.succeededFuture();
                                     });
                    },
                    request.getTimeout(),
                    request.getRequestId()
            );
        } else if (chatModel instanceof VolcesModelSpecification) {
            var serviceAdapter = chatModel.buildServiceAdapter(getConfig().extract("volces"));
            var serviceKit = new VolcesKit((VolcesServiceAdapter) serviceAdapter);
            return serviceKit.chatStream(
                    chatModel,
                    request.toDoubaoRequest().toJsonObject(),
                    fragment -> {
                        return VolcesKit.parseStreamFragmentToChunk(fragment)
                                        .compose(chunk -> {
                                            if (chunk != null) {
                                                return fragmentDataHandler.apply(chunk.cloneAsJsonObject());
                                            }
                                            return Future.succeededFuture();
                                        });
                    },
                    request.getTimeout(),
                    request.getRequestId()
            );
        } else if (chatModel instanceof DashscopeModelSpecification) {
            var serviceAdapter = chatModel.buildServiceAdapter(getConfig().extract("dashscope", "qwen"));
            var serviceKit = new QwenKit((QwenServiceAdapter) serviceAdapter);
            return serviceKit.chatStream(
                    chatModel,
                    request.toQwenRequest().toJsonObject(),
                    fragment -> {
                        return QwenKit.parseStreamFragmentToChunk(fragment)
                                      .compose(chunk -> {
                                          if (chunk != null) {
                                              return fragmentDataHandler.apply(chunk.cloneAsJsonObject());
                                          }
                                          return Future.succeededFuture();
                                      });
                    },
                    request.getTimeout(),
                    request.getRequestId()
            );
        } else {
            throw new IllegalArgumentException("model is not supported");
        }
    }

    @Override
    public Future<Void> requestStream(MixChatRequest request, Function<MixChatResponseChunk, Future<Void>> chunkHandler) {
        var chatModel = request.getChatModel();
        if (chatModel instanceof GPTModelSpecification) {
            var serviceAdapter = chatModel.buildServiceAdapter(getConfig().extract("azure", "openai"));
            var serviceKit = new GPTKit((OpenAIServiceAdapter) serviceAdapter);
            return serviceKit.chatStream(
                    chatModel,
                    request.toGPTRequest().toJsonObject(),
                    fragment -> {
                        return GPTKit.parseStreamFragmentToChunk(fragment)
                                     .compose(chunk -> {
                                         if (chunk != null) {
                                             MixChatResponseChunk mixChatResponseChunk = MixChatResponseChunk.from(chunk);
                                             return chunkHandler.apply(mixChatResponseChunk);
                                         } else {
                                             AigcMix.getVerboseLogger()
                                                    .warning("chunk parsed from this fragment is null: " + fragment);
                                         }
                                         return Future.succeededFuture();
                                     });
                    },
                    request.getTimeout(),
                    request.getRequestId()
            );
        } else if (chatModel instanceof VolcesModelSpecification) {
            var serviceAdapter = chatModel.buildServiceAdapter(getConfig().extract("volces"));
            var serviceKit = new VolcesKit((VolcesServiceAdapter) serviceAdapter);
            return serviceKit.chatStream(
                    chatModel,
                    request.toDoubaoRequest().toJsonObject(),
                    fragment -> {
                        return VolcesKit.parseStreamFragmentToChunk(fragment)
                                        .compose(chunk -> {
                                            if (chunk != null) {
                                                MixChatResponseChunk mixChatResponseChunk = MixChatResponseChunk.from(chunk);
                                                return chunkHandler.apply(mixChatResponseChunk);
                                            }
                                            return Future.succeededFuture();
                                        });
                    },
                    request.getTimeout(),
                    request.getRequestId()
            );
        } else if (chatModel instanceof DashscopeModelSpecification) {
            var serviceAdapter = chatModel.buildServiceAdapter(getConfig().extract("dashscope", "qwen"));
            var serviceKit = new QwenKit((QwenServiceAdapter) serviceAdapter);
            return serviceKit.chatStream(
                    chatModel,
                    request.toQwenRequest().toJsonObject(),
                    fragment -> {
                        return QwenKit.parseStreamFragmentToChunk(fragment)
                                      .compose(chunk -> {
                                          if (chunk != null) {
                                              MixChatResponseChunk mixChatResponseChunk = MixChatResponseChunk.from(chunk);
                                              return chunkHandler.apply(mixChatResponseChunk);
                                          }
                                          return Future.succeededFuture();
                                      });
                    },
                    request.getTimeout(),
                    request.getRequestId()
            );
        } else {
            throw new IllegalArgumentException("model is not supported");
        }
    }



    /**
     * 处理流式聊天请求，最终返回完整的 MixChatResponse。
     *
     * @param request 聊天请求，包含模型、消息等信息
     * @return Future，异步返回 MixChatResponse
     * @throws IllegalArgumentException 如果模型类型不被支持
     */
    @Override
    public Future<MixChatResponse> requestStreamRaw(MixChatRequest request) {
        var chatModel = request.getChatModel();
        if (chatModel instanceof GPTModelSpecification) {
            var serviceAdapter = chatModel.buildServiceAdapter(getConfig().extract("azure", "openai"));
            var serviceKit = new GPTKit((OpenAIServiceAdapter) serviceAdapter);
            return serviceKit.chatStream(
                                     chatModel,
                                     request.toGPTRequest(),
                                     request.getTimeout(),
                                     request.getRequestId()
                             )
                             .compose(resp -> {
                                 var x = MixChatResponse.from(resp);
                                 return Future.succeededFuture(x);
                             });
        } else if (chatModel instanceof VolcesModelSpecification) {
            var serviceAdapter = chatModel.buildServiceAdapter(getConfig().extract("volces"));
            var serviceKit = new VolcesKit((VolcesServiceAdapter) serviceAdapter);
            return serviceKit.chatStream(
                                     chatModel,
                                     request.toDoubaoRequest(),
                                     request.getTimeout(),
                                     request.getRequestId()
                             )
                             .compose(resp -> {
                                 var x = MixChatResponse.from(resp);
                                 return Future.succeededFuture(x);
                             });
        } else if (chatModel instanceof DashscopeModelSpecification) {
            var serviceAdapter = chatModel.buildServiceAdapter(getConfig().extract("dashscope", "qwen"));
            var serviceKit = new QwenKit((QwenServiceAdapter) serviceAdapter);
            return serviceKit.chatStream(
                                     chatModel,
                                     request.toQwenRequest(),
                                     request.getTimeout(),
                                     request.getRequestId()
                             )
                             .compose(resp -> {
                                 var x = MixChatResponse.from(resp);
                                 return Future.succeededFuture(x);
                             });
        } else {
            throw new IllegalArgumentException("model is not supported");
        }
    }

}
