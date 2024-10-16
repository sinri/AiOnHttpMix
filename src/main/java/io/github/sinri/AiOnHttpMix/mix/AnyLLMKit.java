package io.github.sinri.AiOnHttpMix.mix;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk.OpenAIChatGptStreamBuffer;
import io.github.sinri.AiOnHttpMix.azure.openai.core.AzureOpenAIServiceMeta;
import io.github.sinri.AiOnHttpMix.dashscope.core.DashscopeServiceMeta;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.chunk.QwenStreamBuffer;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.mirage.MirageSDK;
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
    public AnyLLMKit useChatGPT(AzureOpenAIServiceMeta azureOpenAIServiceMeta, SupportedModel model) {
        if (model.getSeries() != azureOpenAIServiceMeta.getSupportedModelSeries()) {
            throw new IllegalArgumentException("model is not belong to this series");
        }
        this.model = model;
        this.serviceMeta = azureOpenAIServiceMeta;
        return this;
    }

    @Override
    public AnyLLMKit useQwen(DashscopeServiceMeta dashscopeServiceMeta, SupportedModel model) {
        if (model.getSeries() != dashscopeServiceMeta.getSupportedModelSeries()) {
            throw new IllegalArgumentException("model is not belong to this series");
        }
        this.model = SupportedModel.QwenPlus;
        this.serviceMeta = dashscopeServiceMeta;
        return this;
    }

    @Override
    public AnyLLMKit useVolces(VolcesServiceMeta volcesServiceMeta) {
        this.model = SupportedModel.Volces;
        this.serviceMeta = volcesServiceMeta;
        return this;
    }

    /**
     * @since 1.1.5
     */
    private AnyLLMKit throughMirage(@NotNull MirageSDK mirageSDK, @NotNull SupportedModel model) {
        this.mirageSDK = mirageSDK;
        this.model = model;
        switch (model) {
            case ChatGPT:
                this.mirageModel = "ChatGPT";
                this.mirageService = "gpt-4-o";
                break;
            case QwenPlus:
                this.mirageModel = "QwenPlus";
                this.mirageService = null;
                break;
            case QwenMax:
                this.mirageModel = "QwenMax";
                this.mirageService = null;
                break;
            case Volces:
                this.mirageModel = "Volces";
                this.mirageService = "doubao-pro-128k";
                break;
            default:
                throw new IllegalArgumentException("Unknown model");
        }
        return this;
    }

    public AnyLLMKit useVolces(VolcesServiceMeta volcesServiceMeta, SupportedModel model) {
        if (model.getSeries() != volcesServiceMeta.getSupportedModelSeries()) {
            throw new IllegalArgumentException("model is not belong to this series");
        }
        this.model = SupportedModel.Volces;
        this.serviceMeta = volcesServiceMeta;
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
            return switch (model.getSeries()) {
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
                case Qwen -> new QwenKit()
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
                case Volces -> new VolcesKit()
                        .chat(
                                (VolcesServiceMeta) serviceMeta,
                                request.toVolcesChatRequest(),
                                request.getRequestId()
                        )
                        .compose(resp -> {
                            AnyLLMResponse anyLLMResponse = AnyLLMResponse.from(resp);
                            return Future.succeededFuture(anyLLMResponse);
                        });
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
            return switch (model.getSeries()) {
                case ChatGPT -> new ChatGPTKit()
                        .chatStream(
                                (AzureOpenAIServiceMeta) serviceMeta,
                                request.toChatGptRequest(),
                                chunk -> {
                                    fragmentHandler.handle(chunk.cloneAsJsonObject().toString());
                                },
                                request.getRequestId()
                        );
                case Qwen -> new QwenKit()
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
                case Volces -> new VolcesKit()
                        .chatStreamWithChunkHandler(
                                (VolcesServiceMeta) serviceMeta,
                                request.toVolcesChatRequest(),
                                chunk -> {
                                    fragmentHandler.handle(chunk.cloneAsJsonObject().toString());
                                },
                                request.getRequestId()
                        );
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
            return switch (model.getSeries()) {
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
                case Qwen -> new QwenKit()
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
                case Volces -> new VolcesKit()
                        .chatStreamWithBuffer(
                                (VolcesServiceMeta) serviceMeta,
                                request.toVolcesChatRequest(),
                                request.getRequestId()
                        )
                        .compose(resp -> {
                            AnyLLMResponse anyLLMResponse = AnyLLMResponse.from(resp);
                            return Future.succeededFuture(anyLLMResponse);
                        });
            };
        } else {
            Handler<String> fragmentHandler;
            LLMStreamBuffer buffer;

            switch (model.getSeries()) {
                case ChatGPT:
                    buffer = new OpenAIChatGptStreamBuffer();
                    fragmentHandler = ChatGPTKit.getStreamBufferFragmentHandler((OpenAIChatGptStreamBuffer) buffer, request.getRequestId());
                    break;
                case Qwen:
                    buffer = new QwenStreamBuffer();
                    fragmentHandler = QwenKit.getStreamBufferFragmentHandler((QwenStreamBuffer) buffer, request.getRequestId());
                    break;
                case Volces:
                    buffer = new VolcesChatStreamBuffer();
                    fragmentHandler = VolcesKit.getStreamBufferFragmentHandler((VolcesChatStreamBuffer) buffer, request.getRequestId());
                    break;
                default:
                    throw new IllegalArgumentException("Unknown series " + model.getSeries());
            }

            return this.mirageSDK.requestStream(
                            this.mirageModel,
                            this.mirageService,
                            true,
                            request.toMirageRequestEntity(),
                            s -> {
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
    public AnyLLMKit useChatGPT(MirageSDK mirageSDK, SupportedModel model) {
        return throughMirage(mirageSDK, model);
    }

    @Override
    public AnyLLMKit useQwen(MirageSDK mirageSDK, SupportedModel model) {
        return throughMirage(mirageSDK, model);
    }

    @Override
    public AnyLLMKit useVolces(MirageSDK mirageSDK) {
        return throughMirage(mirageSDK, model);
    }
}