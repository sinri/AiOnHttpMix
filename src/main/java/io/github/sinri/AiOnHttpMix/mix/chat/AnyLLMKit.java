package io.github.sinri.AiOnHttpMix.mix.chat;

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
import io.github.sinri.AiOnHttpMix.deepseek.core.DeepseekServiceMeta;
import io.github.sinri.AiOnHttpMix.mirage.MirageSDK;
import io.github.sinri.AiOnHttpMix.mix.FunctionCallAdapter;
import io.github.sinri.AiOnHttpMix.utils.LLMStreamBuffer;
import io.github.sinri.AiOnHttpMix.utils.ServiceMeta;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
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

    /**
     * @since 1.2.2
     */
    @Override
    public AnyLLMKit useServiceMeta(ServiceMeta serviceMeta, SupportedModel supportedModel) {
        if (!serviceMeta.isModelSupported(supportedModel)) {
            throw new IllegalArgumentException("The model " + supportedModel
                    + " is not supported by the service meta (" + serviceMeta.getClass().getName() + ")");
        }
        this.model = supportedModel;
        this.serviceMeta = serviceMeta;
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
                this.mirageService = "gpt-4-o";
                break;
            case QwenPlus, QwenMax, QwenLong:
                this.mirageService = null;
                break;
            case Doubao:
                this.mirageService = "doubao-pro-128k";
                break;
            case DeepSeekReasonerOnVolces:
                this.mirageService = "DeepSeek-R1";
                break;
            case DeepSeekChatOnVolces:
                this.mirageService = "DeepSeek-V3";
                break;
            case DeepSeekReasoner, DeepSeekChat:
                this.mirageService = "main";
                break;
            //            default:
            //                throw new IllegalArgumentException("Unknown model");
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
     * @param anyLLMResponseToolFunctionCall as of 1.2.6, it should not be null.
     * @param fixedArgument                  as of 1.2.6, a new nullable fixed argument is added to this method to fit
     *                                       various context.
     * @since 1.1.2
     */
    public <R> Future<R> callRegisterFunction(
            @NotNull AnyLLMResponseToolFunctionCall anyLLMResponseToolFunctionCall,
            @Nullable JsonObject fixedArgument
    ) {
        FunctionCallAdapter registeredFunction =
                this.getRegisteredFunction(anyLLMResponseToolFunctionCall.getFunctionName());
        if (registeredFunction == null) {
            return Future.failedFuture(new UnsupportedOperationException("Function not registered"));
        }
        String functionArguments = anyLLMResponseToolFunctionCall.getFunctionArguments();
        JsonObject argsAsJsonObject;
        try {
            argsAsJsonObject = new JsonObject(functionArguments);
        } catch (Throwable e) {
            argsAsJsonObject = null;
        }
        return registeredFunction.callFunction(argsAsJsonObject, fixedArgument);
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
                case QwenPlus, QwenMax, QwenLong, DeepSeekReasonerOnDashScope, DeepSeekChatOnDashScope -> new QwenKit()
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
                                request.getMaxExecutionSeconds(),
                                request.getRequestId()
                        );
                case QwenPlus, QwenMax, QwenLong, DeepSeekChatOnDashScope, DeepSeekReasonerOnDashScope -> new QwenKit()
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
                //default -> Future.failedFuture(new UnsupportedOperationException("Not supported!"));
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
                                request.getMaxExecutionSeconds(),
                                request.getRequestId()
                        )
                        .compose(resp -> {
                            AnyLLMResponse anyLLMResponse = AnyLLMResponse.from(resp);
                            return Future.succeededFuture(anyLLMResponse);
                        });
                case QwenPlus, QwenMax, QwenLong, DeepSeekReasonerOnDashScope, DeepSeekChatOnDashScope -> new QwenKit()
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
        } else {
            Handler<String> fragmentHandler;
            LLMStreamBuffer buffer;

            fragmentHandler = switch (model) {
                case ChatGPT -> {
                    buffer = new OpenAIChatGptStreamBuffer();
                    yield ChatGPTKit.getStreamBufferFragmentHandler((OpenAIChatGptStreamBuffer) buffer,
                            request.getRequestId());
                }
                case QwenPlus, QwenMax, QwenLong, DeepSeekChatOnDashScope, DeepSeekReasonerOnDashScope -> {
                    buffer = new QwenStreamBuffer();
                    yield QwenKit.getStreamBufferFragmentHandler((QwenStreamBuffer) buffer,
                            request.getRequestId());
                }
                case Doubao, KimiOnVolces -> {
                    buffer = new VolcesChatStreamBuffer();
                    yield VolcesKit.getStreamBufferFragmentHandler((VolcesChatStreamBuffer) buffer,
                            request.getRequestId());
                }
                case DeepSeekReasonerOnVolces, DeepSeekChatOnVolces -> {
                    buffer = new DeepseekStreamBuffer();
                    yield DeepseekKit.getStreamBufferFragmentHandler((DeepseekStreamBuffer) buffer,
                            request.getRequestId());
                    //                default:
                    //                    throw new UnsupportedOperationException("Not supported!");
                }
                case DeepSeekChat, DeepSeekReasoner -> {
                    buffer = new DeepseekStreamBuffer();
                    yield DeepseekKit.getStreamBufferFragmentHandler((DeepseekStreamBuffer) buffer,
                            request.getRequestId());
                }
            };

            return this.mirageSDK.requestStream(
                               this.mirageModel,
                               this.mirageService,
                               true,
                               request.toMirageRequestEntity(),
                               request.getMaxExecutionSeconds() * 1000L,
                               s -> {
                                   AigcMix.getVerboseLogger()
                                          .debug("io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMKit" +
                                                  ".requestWithStreamBuffer::component | " + s);
                                /*
                                {"output":{"choices":[{"message":{"content":"筑","role":"assistant"},
                                "finish_reason":"null"}]},"usage":{"total_tokens":58,"input_tokens":54,
                                "output_tokens":4},"request_id":"ab856b62-68aa-928d-ade3-ae7f9312d5ee"}
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
    public AnyLLMKit useMirageSDK(@NotNull MirageSDK mirageSDK, @NotNull SupportedModel supportedModel) {
        return throughMirage(mirageSDK, supportedModel);
    }


}