package io.github.sinri.AiOnHttpMix.mix;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk.OpenAIChatGptStreamBuffer;
import io.github.sinri.AiOnHttpMix.azure.openai.core.AzureOpenAIServiceMeta;
import io.github.sinri.AiOnHttpMix.dashscope.core.DashscopeServiceMeta;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.chunk.QwenStreamBuffer;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.deepseek.DeepseekKit;
import io.github.sinri.AiOnHttpMix.deepseek.chat.chunk.DeepseekStreamBuffer;
import io.github.sinri.AiOnHttpMix.mirage.MirageSDK;
import io.github.sinri.AiOnHttpMix.utils.LLMStreamBuffer;
import io.github.sinri.AiOnHttpMix.utils.ServiceMeta;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.github.sinri.AiOnHttpMix.utils.SupportedProvider;
import io.github.sinri.AiOnHttpMix.volces.core.VolcesServiceMeta;
import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.github.sinri.AiOnHttpMix.volces.v3.chunk.VolcesChatStreamBuffer;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @since 1.1.0
 * @since 1.1.12 add Pure Chat DeepSeek on Volces
 */
public class AnyLLMKit implements AnyLLMKitThroughSDKMixin<AnyLLMKit>, AnyLLMKitThroughMirageMixin<AnyLLMKit> {
    /**
     * @since 1.1.2
     */
    private final Map<String, FunctionCallAdapter> fcMap = new HashMap<>();
    /**
     * Used by both SDK or Mirage.
     */
    private SupportedModel model;
    /**
     * Used by SDK only.
     */
    private ServiceMeta serviceMeta;
    /**
     * Used by Mirage only.
     */
    private MirageSDK mirageSDK;
    /**
     * Used by Mirage only.
     */
    private String mirageModel;
    /**
     * Used by Mirage only.
     */
    private String mirageService;

    @Override
    public AnyLLMKit useAzure(AzureOpenAIServiceMeta azureOpenAIServiceMeta, SupportedModel model) {
        if (model.getProvider() != azureOpenAIServiceMeta.getSupportedProvider()) {
            throw new IllegalArgumentException("model is not belong to this provider");
        }
        this.model = model;
        this.serviceMeta = azureOpenAIServiceMeta;
        return this;
    }

    @Override
    public AnyLLMKit useDataScope(DashscopeServiceMeta dashscopeServiceMeta, SupportedModel model) {
        if (model.getProvider() != dashscopeServiceMeta.getSupportedProvider()) {
            throw new IllegalArgumentException("model is not belong to this provider");
        }
        this.model = SupportedModel.QwenPlus;
        this.serviceMeta = dashscopeServiceMeta;
        return this;
    }

    @Override
    public AnyLLMKit useVolces(VolcesServiceMeta volcesServiceMeta, SupportedModel model) {
        if (model.getProvider() != volcesServiceMeta.getSupportedProvider()) {
            throw new IllegalArgumentException("model is not belong to this provider");
        }
        this.model = model;
        this.serviceMeta = volcesServiceMeta;
        return this;
    }

    /**
     * @since 1.1.5
     */
    private AnyLLMKit throughMirage(@NotNull MirageSDK mirageSDK, @NotNull SupportedModel model) {
        this.mirageSDK = mirageSDK;
        this.model = model;
        this.mirageModel = model.name();
        switch (model) {
            case ChatGPT:
//                this.mirageModel = "ChatGPT";
                this.mirageService = "gpt-4-o";
                break;
            case QwenPlus:
//                this.mirageModel = "QwenPlus";
                this.mirageService = null;
                break;
            case QwenMax:
//                this.mirageModel = "QwenMax";
                this.mirageService = null;
                break;
            case Doubao:
//                this.mirageModel = "Doubao";
                this.mirageService = "doubao-pro-128k";
                break;
            case DeepSeekReasonerOnVolces:
//                this.mirageModel = "DeepSeekReasonerOnVolces";
                this.mirageService = "DeepSeek-R1";
                break;
            default:
                throw new IllegalArgumentException("Unknown model");
        }
        return this;
    }

    /**
     * @since 1.1.2
     */
    public AnyLLMKit registerFunction(@NotNull FunctionCallAdapter functionCallAdapter) {
        fcMap.put(functionCallAdapter.getFunctionName(), functionCallAdapter);
        return this;
    }

    /**
     * @since 1.1.2
     */
    public AnyLLMKit unregisterFunction(@NotNull String functionName) {
        fcMap.remove(functionName);
        return this;
    }

    /**
     * @since 1.1.2
     */
    public AnyLLMKit unregisterAllFunctions() {
        fcMap.clear();
        return this;
    }

    /**
     * @since 1.1.2
     */
    public @Nullable FunctionCallAdapter getRegisteredFunction(@NotNull String functionName) {
        return fcMap.get(functionName);
    }

    /**
     * @since 1.1.2
     */
    public Future<Object> callRegisterFunction(AnyLLMResponseToolFunctionCall anyLLMResponseToolFunctionCall) {
        FunctionCallAdapter registeredFunction = this.getRegisteredFunction(anyLLMResponseToolFunctionCall.getFunctionName());
        if (registeredFunction == null) {
            return Future.failedFuture(new UnsupportedOperationException("Function not registered"));
        }
        return registeredFunction.callFunction(new JsonObject(anyLLMResponseToolFunctionCall.getFunctionArguments()));
    }

    public Future<AnyLLMResponse> request(Handler<AnyLLMRequest> requestHandler) {
        AnyLLMRequest anyLLMRequest = AnyLLMRequest.create();
        requestHandler.handle(anyLLMRequest);
        return request(anyLLMRequest);
    }

    public Future<AnyLLMResponse> request(AnyLLMRequest request) {
        if (this.mirageSDK == null) {
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
                case QwenPlus, QwenMax -> new QwenKit()
                        .chatForMessageResponse(
                                (DashscopeServiceMeta) serviceMeta,
                                request.toQwenRequest()
                                        .setModel(model.asQwenModel()),
                                request.getRequestId()
                        )
                        .compose(resp -> {
                            AnyLLMResponse anyLLMResponse = AnyLLMResponse.from(resp);
                            return Future.succeededFuture(anyLLMResponse);
                        });
                case Doubao -> new VolcesKit()
                        .chat(
                                (VolcesServiceMeta) serviceMeta,
                                request.toVolcesChatRequest(),
                                request.getRequestId()
                        )
                        .compose(resp -> {
                            AnyLLMResponse anyLLMResponse = AnyLLMResponse.from(resp);
                            return Future.succeededFuture(anyLLMResponse);
                        });
                case DeepSeekReasonerOnVolces -> new DeepseekKit()
                        .chat(
                                (VolcesServiceMeta) serviceMeta,
                                request.toDeepseekChatRequest(),
                                request.getRequestId()
                        )
                        .compose(resp -> {
                            AnyLLMResponse anyLLMResponse = AnyLLMResponse.from(resp);
                            return Future.succeededFuture(anyLLMResponse);
                        });
                default -> Future.failedFuture(new UnsupportedOperationException("Not supported!"));
            };
        } else {
            return this.mirageSDK.requestSync(
                    this.mirageModel,
                    this.mirageService,
                    true,
                    request.toMirageRequestEntity()
            );
        }
    }

    /**
     * @param request         请求
     * @param fragmentHandler 针对一个已经格式化好的SSE Chunk的JSON对象字符串表达的处理器
     * @since 1.1.3
     */
    public Future<Void> request(AnyLLMRequest request, Handler<String> fragmentHandler) {
        if (mirageSDK == null) {
            return switch (model) {
                case ChatGPT -> new ChatGPTKit()
                        .chatStream(
                                (AzureOpenAIServiceMeta) serviceMeta,
                                request.toChatGptRequest(),
                                chunk -> {
                                    fragmentHandler.handle(chunk.cloneAsJsonObject().toString());
                                },
                                request.getRequestId()
                        );
                case QwenPlus, QwenMax -> new QwenKit()
                        .chatStreamWithChunkHandler(
                                (DashscopeServiceMeta) serviceMeta,
                                request.toQwenRequest()
                                        .setModel(model.asQwenModel())
                                        .handleParameters(p -> p
                                                .setResultFormat(QwenRequest.Parameters.ResultFormat.message)
                                                .setIncrementalOutput(true)
                                        ),
                                chunk -> {
                                    fragmentHandler.handle(chunk.cloneAsJsonObject().toString());
                                },
                                request.getRequestId()
                        );
                case Doubao -> new VolcesKit()
                        .chatStreamWithChunkHandler(
                                (VolcesServiceMeta) serviceMeta,
                                request.toVolcesChatRequest(),
                                chunk -> {
                                    fragmentHandler.handle(chunk.cloneAsJsonObject().toString());
                                },
                                request.getRequestId()
                        );
                case DeepSeekReasonerOnVolces -> new DeepseekKit()
                        .chatStreamWithChunkHandler(
                                (VolcesServiceMeta) serviceMeta,
                                request.toDeepseekChatRequest(),
                                chunk -> {
                                    fragmentHandler.handle(chunk.cloneAsJsonObject().toString());
                                },
                                request.getRequestId()
                        );
                default -> Future.failedFuture(new UnsupportedOperationException("Not supported!"));
            };
        } else {
            return this.mirageSDK.requestStream(
                    this.mirageModel,
                    this.mirageService,
                    true,
                    request.toMirageRequestEntity(),
                    fragmentHandler
            );
        }
    }

    public Future<AnyLLMResponse> requestWithStreamBuffer(Handler<AnyLLMRequest> requestHandler) {
        AnyLLMRequest anyLLMRequest = AnyLLMRequest.create();
        requestHandler.handle(anyLLMRequest);
        return requestWithStreamBuffer(anyLLMRequest);
    }

    public Future<AnyLLMResponse> requestWithStreamBuffer(AnyLLMRequest request) {
        if (mirageSDK == null) {
            return switch (model) {
                case ChatGPT -> new ChatGPTKit()
                        .chatStream(
                                (AzureOpenAIServiceMeta) serviceMeta,
                                request.toChatGptRequest(),
                                request.getRequestId()
                        )
                        .compose(resp -> {
                            AnyLLMResponse anyLLMResponse = AnyLLMResponse.from(resp);
                            return Future.succeededFuture(anyLLMResponse);
                        });
                case QwenPlus, QwenMax -> new QwenKit()
                        .chatStreamWithBuffer(
                                (DashscopeServiceMeta) serviceMeta,
                                request.toQwenRequest()
                                        .setModel(model.asQwenModel())
                                        .handleParameters(p -> p
                                                .setResultFormat(QwenRequest.Parameters.ResultFormat.message)
                                                .setIncrementalOutput(true)
                                        ),
                                request.getRequestId()
                        )
                        .compose(resp -> {
                            AnyLLMResponse anyLLMResponse = AnyLLMResponse.from(resp);
                            return Future.succeededFuture(anyLLMResponse);
                        });
                case Doubao -> new VolcesKit()
                        .chatStreamWithBuffer(
                                (VolcesServiceMeta) serviceMeta,
                                request.toVolcesChatRequest(),
                                request.getRequestId()
                        )
                        .compose(resp -> {
                            AnyLLMResponse anyLLMResponse = AnyLLMResponse.from(resp);
                            return Future.succeededFuture(anyLLMResponse);
                        });
                case DeepSeekReasonerOnVolces -> new DeepseekKit()
                        .chatSSEWithBuffer(
                                (VolcesServiceMeta) serviceMeta,
                                request.toDeepseekChatRequest(),
                                request.getRequestId()
                        )
                        .compose(resp -> {
                            AnyLLMResponse anyLLMResponse = AnyLLMResponse.from(resp);
                            return Future.succeededFuture(anyLLMResponse);
                        });
//                default -> Future.failedFuture(new UnsupportedOperationException("Not supported!"));
            };
        } else {
            Handler<String> fragmentHandler;
            LLMStreamBuffer buffer;

            switch (model) {
                case ChatGPT:
                    buffer = new OpenAIChatGptStreamBuffer();
                    fragmentHandler = ChatGPTKit.getStreamBufferFragmentHandler((OpenAIChatGptStreamBuffer) buffer, request.getRequestId());
                    break;
                case QwenPlus, QwenMax:
                    buffer = new QwenStreamBuffer();
                    fragmentHandler = QwenKit.getStreamBufferFragmentHandler((QwenStreamBuffer) buffer, request.getRequestId());
                    break;
                case Doubao:
                    buffer = new VolcesChatStreamBuffer();
                    fragmentHandler = VolcesKit.getStreamBufferFragmentHandler((VolcesChatStreamBuffer) buffer, request.getRequestId());
                    break;
                case DeepSeekReasonerOnVolces:
                    buffer = new DeepseekStreamBuffer();
                    fragmentHandler = DeepseekKit.getStreamBufferFragmentHandler((DeepseekStreamBuffer) buffer, request.getRequestId());
                    break;
                default:
                    throw new UnsupportedOperationException("Not supported!");
            }

            return this.mirageSDK.requestStream(
                            this.mirageModel,
                            this.mirageService,
                            true,
                            request.toMirageRequestEntity(),
                            s -> {
                                AigcMix.getVerboseLogger().debug("io.github.sinri.AiOnHttpMix.mix.AnyLLMKit.requestWithStreamBuffer::component | " + s);
                                /*
                                {"output":{"choices":[{"message":{"content":"筑","role":"assistant"},"finish_reason":"null"}]},"usage":{"total_tokens":58,"input_tokens":54,"output_tokens":4},"request_id":"ab856b62-68aa-928d-ade3-ae7f9312d5ee"}
                                 */
                                fragmentHandler.handle(s);
                            }
                    )
                    .compose(fin -> {
                        return Future.succeededFuture(buffer.toAnyLLMResponse());
                    });
        }
    }

    @Override
    public @NotNull AnyLLMKit getImplementation() {
        return this;
    }

    @Override
    public AnyLLMKit useAzure(MirageSDK mirageSDK, SupportedModel model) {
        if (model.getProvider() != SupportedProvider.AzureOpenAI) {
            throw new IllegalArgumentException("Only ChatGPT supported");
        }
        return throughMirage(mirageSDK, model);
    }

    @Override
    public AnyLLMKit useDataScope(MirageSDK mirageSDK, SupportedModel model) {
        if (model.getProvider() != SupportedProvider.DataScope) {
            throw new IllegalArgumentException("Only Qwen supported");
        }
        return throughMirage(mirageSDK, model);
    }

    @Override
    public AnyLLMKit useVolces(MirageSDK mirageSDK, SupportedModel model) {
        if (model.getProvider() != SupportedProvider.Volces) {
            throw new IllegalArgumentException("Only Volces supported");
        }
        return throughMirage(mirageSDK, model);
    }
}