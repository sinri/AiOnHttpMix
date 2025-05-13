package io.github.sinri.AiOnHttpMix.mix.chat;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.AiOnHttpMix.azure.openai.core.AzureOpenAIServiceMeta;
import io.github.sinri.AiOnHttpMix.dashscope.core.DashscopeServiceMeta;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.deepseek.DeepseekKit;
import io.github.sinri.AiOnHttpMix.deepseek.core.DeepseekServiceMeta;
import io.github.sinri.AiOnHttpMix.utils.ServiceMeta;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.github.sinri.AiOnHttpMix.volces.core.VolcesServiceMeta;
import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.vertx.core.Future;
import io.vertx.core.Handler;

/**
 * @since 1.3.0
 */
public class AnyLLMServiceAdapterThroughSDK implements AnyLLMServiceAdapter {
    /**
     * Used by both SDK or Mirage.
     */
    private final SupportedModel model;
    /**
     * Used by SDK only.
     */
    private final ServiceMeta serviceMeta;

    AnyLLMServiceAdapterThroughSDK(ServiceMeta serviceMeta, SupportedModel supportedModel) {
        if (!serviceMeta.isModelSupported(supportedModel)) {
            throw new IllegalArgumentException("The model " + supportedModel
                    + " is not supported by the service meta (" + serviceMeta.getClass().getName() + ")");
        }
        this.model = supportedModel;
        this.serviceMeta = serviceMeta;
    }

    public ServiceMeta getServiceMeta() {
        return serviceMeta;
    }

    public SupportedModel getModel() {
        return model;
    }

    @Override
    public Future<AnyLLMResponse> request(AnyLLMRequest request) {
        return switch (model) {
            case ChatGPT -> new ChatGPTKit()
                    .chat(
                            (AzureOpenAIServiceMeta) serviceMeta,
                            request.toChatGptRequest(),
                            request.getRequestId()
                    )
                    .compose(resp -> {
                        AnyLLMResponse anyLLMResponse = AnyLLMResponse.from(resp);
                        return Future.succeededFuture(anyLLMResponse);
                    });
            case QwenPlus, QwenMax, QwenTurbo, QwenLong,
                 QwenPlusLatest, QwenMaxLatest, QwenTurboLatest,
                 DeepSeekReasonerOnDashScope, DeepSeekChatOnDashScope -> new QwenKit()
                    .chatForMessageResponse(
                            (DashscopeServiceMeta) serviceMeta,
                            request.toQwenRequest()
                                   .setModel(model.getMappedModelCode()),
                            request.getRequestId()
                    )
                    .compose(resp -> {
                        AnyLLMResponse anyLLMResponse = AnyLLMResponse.from(resp);
                        return Future.succeededFuture(anyLLMResponse);
                    });
            case Doubao, KimiOnVolces -> new VolcesKit()
                    .chat(
                            (VolcesServiceMeta) serviceMeta,
                            request.toVolcesChatRequest(),
                            request.getRequestId()
                    )
                    .compose(resp -> {
                        AnyLLMResponse anyLLMResponse = AnyLLMResponse.from(resp);
                        return Future.succeededFuture(anyLLMResponse);
                    });
            case DeepSeekReasonerOnVolces, DeepSeekChatOnVolces -> new VolcesKit()
                    .chatForDeepSeekV3(
                            (VolcesServiceMeta) serviceMeta,
                            request.toDeepseekChatRequest(),
                            request.getRequestId()
                    )
                    .compose(resp -> {
                        AnyLLMResponse anyLLMResponse = AnyLLMResponse.from(resp);
                        return Future.succeededFuture(anyLLMResponse);
                    });
            case DeepSeekChat, DeepSeekReasoner -> new DeepseekKit()
                    .chat(
                            (DeepseekServiceMeta) serviceMeta,
                            request.toDeepseekChatRequest()
                                   .setModel(model.getMappedModelCode()),
                            request.getRequestId()
                    )
                    .compose(resp -> {
                        AnyLLMResponse anyLLMResponse = AnyLLMResponse.from(resp);
                        return Future.succeededFuture(anyLLMResponse);
                    });
            //default -> Future.failedFuture(new UnsupportedOperationException("Not supported!"));
        };

    }

    @Override
    public Future<Void> request(AnyLLMRequest request, Handler<String> fragmentHandler) {
        return switch (model) {
            case ChatGPT -> new ChatGPTKit()
                    .chatStream(
                            (AzureOpenAIServiceMeta) serviceMeta,
                            request.toChatGptRequest(),
                            chunk -> {
                                fragmentHandler.handle(chunk.cloneAsJsonObject().toString());
                            },
                            request.getMaxExecutionSeconds(),
                            request.getRequestId()
                    );
            case QwenPlus, QwenMax, QwenTurbo, QwenLong,
                 QwenPlusLatest, QwenMaxLatest, QwenTurboLatest,
                 DeepSeekReasonerOnDashScope, DeepSeekChatOnDashScope -> new QwenKit()
                    .chatStreamWithChunkHandler(
                            (DashscopeServiceMeta) serviceMeta,
                            request.toQwenRequest()
                                   .setModel(model.getMappedModelCode())
                                   .handleParameters(p -> p
                                           .setResultFormat(QwenRequest.Parameters.ResultFormat.message)
                                           .setIncrementalOutput(true)
                                   ),
                            chunk -> {
                                fragmentHandler.handle(chunk.cloneAsJsonObject().toString());
                            },
                            request.getMaxExecutionSeconds(),
                            request.getRequestId()
                    );
            case Doubao, KimiOnVolces -> new VolcesKit()
                    .chatStreamWithChunkHandler(
                            (VolcesServiceMeta) serviceMeta,
                            request.toVolcesChatRequest(),
                            chunk -> {
                                fragmentHandler.handle(chunk.cloneAsJsonObject().toString());
                            },
                            request.getMaxExecutionSeconds(),
                            request.getRequestId()
                    );
            case DeepSeekReasonerOnVolces, DeepSeekChatOnVolces -> new VolcesKit()
                    .chatStreamWithChunkHandlerForDeepSeekV3(
                            (VolcesServiceMeta) serviceMeta,
                            request.toDeepseekChatRequest(),
                            chunk -> {
                                fragmentHandler.handle(chunk.cloneAsJsonObject().toString());
                            },
                            request.getMaxExecutionSeconds(),
                            request.getRequestId()
                    );
            case DeepSeekChat, DeepSeekReasoner -> new DeepseekKit()
                    .chatStreamWithChunkHandler(
                            (DeepseekServiceMeta) serviceMeta,
                            request.toDeepseekChatRequest(),
                            chunk -> {
                                fragmentHandler.handle(chunk.cloneAsJsonObject().toString());
                            },
                            request.getMaxExecutionSeconds(),
                            request.getRequestId()
                    );
        };
    }

    @Override
    public Future<AnyLLMResponse> requestWithStreamBuffer(AnyLLMRequest request) {
        return switch (model) {
            case ChatGPT -> new ChatGPTKit()
                    .chatStream(
                            (AzureOpenAIServiceMeta) serviceMeta,
                            request.toChatGptRequest(),
                            request.getMaxExecutionSeconds(),
                            request.getRequestId()
                    )
                    .compose(resp -> {
                        AnyLLMResponse anyLLMResponse = AnyLLMResponse.from(resp);
                        return Future.succeededFuture(anyLLMResponse);
                    });
            case QwenPlus, QwenMax, QwenTurbo, QwenLong,
                 QwenPlusLatest, QwenMaxLatest, QwenTurboLatest,
                 DeepSeekReasonerOnDashScope, DeepSeekChatOnDashScope -> new QwenKit()
                    .chatStreamWithBuffer(
                            (DashscopeServiceMeta) serviceMeta,
                            request.toQwenRequest()
                                   .setModel(model.getMappedModelCode())
                                   .handleParameters(p -> p
                                           .setResultFormat(QwenRequest.Parameters.ResultFormat.message)
                                           .setIncrementalOutput(true)
                                   ),
                            request.getMaxExecutionSeconds(),
                            request.getRequestId()
                    )
                    .compose(resp -> {
                        AnyLLMResponse anyLLMResponse = AnyLLMResponse.from(resp);
                        return Future.succeededFuture(anyLLMResponse);
                    });
            case Doubao, KimiOnVolces -> new VolcesKit()
                    .chatStreamWithBuffer(
                            (VolcesServiceMeta) serviceMeta,
                            request.toVolcesChatRequest(),
                            request.getMaxExecutionSeconds(),
                            request.getRequestId()
                    )
                    .compose(resp -> {
                        AnyLLMResponse anyLLMResponse = AnyLLMResponse.from(resp);
                        return Future.succeededFuture(anyLLMResponse);
                    });
            case DeepSeekReasonerOnVolces, DeepSeekChatOnVolces -> new VolcesKit()
                    .chatSSEWithBufferForDeepSeekV3(
                            (VolcesServiceMeta) serviceMeta,
                            request.toDeepseekChatRequest(),
                            request.getMaxExecutionSeconds(),
                            request.getRequestId()
                    )
                    .compose(resp -> {
                        AnyLLMResponse anyLLMResponse = AnyLLMResponse.from(resp);
                        return Future.succeededFuture(anyLLMResponse);
                    });
            case DeepSeekChat, DeepSeekReasoner -> new DeepseekKit()
                    .chatStreamWithBuffer(
                            (DeepseekServiceMeta) serviceMeta,
                            request.toDeepseekChatRequest()
                                   .setModel(model.getMappedModelCode()),
                            request.getMaxExecutionSeconds(),
                            request.getRequestId()
                    )
                    .compose(resp -> {
                        AnyLLMResponse anyLLMResponse = AnyLLMResponse.from(resp);
                        return Future.succeededFuture(anyLLMResponse);
                    });
            //                default -> Future.failedFuture(new UnsupportedOperationException("Not supported!"));
        };
    }

}
