package io.github.sinri.AiOnHttpMix.mix;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.AiOnHttpMix.azure.openai.core.AzureOpenAIServiceMeta;
import io.github.sinri.AiOnHttpMix.dashscope.core.DashscopeServiceMeta;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.utils.ServiceMeta;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.github.sinri.AiOnHttpMix.volces.core.VolcesServiceMeta;
import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public class AnyLLMKit {
    private SupportedModel model;
    private ServiceMeta serviceMeta;

    public AnyLLMKit useChatGPT(AzureOpenAIServiceMeta azureOpenAIServiceMeta) {
        this.model = SupportedModel.ChatGPT;
        this.serviceMeta = azureOpenAIServiceMeta;
        return this;
    }

    public AnyLLMKit useChatGPT(AzureOpenAIServiceMeta azureOpenAIServiceMeta, SupportedModel model) {
        if (model.getSeries() != azureOpenAIServiceMeta.getSupportedModelSeries()) {
            throw new IllegalArgumentException("model is not belong to this series");
        }
        this.model = model;
        this.serviceMeta = azureOpenAIServiceMeta;
        return this;
    }

    public AnyLLMKit useQwen(DashscopeServiceMeta dashscopeServiceMeta) {
        this.model = SupportedModel.QwenPlus;
        this.serviceMeta = dashscopeServiceMeta;
        return this;
    }

    public AnyLLMKit useQwen(DashscopeServiceMeta dashscopeServiceMeta, SupportedModel model) {
        if (model.getSeries() != dashscopeServiceMeta.getSupportedModelSeries()) {
            throw new IllegalArgumentException("model is not belong to this series");
        }
        this.model = SupportedModel.QwenPlus;
        this.serviceMeta = dashscopeServiceMeta;
        return this;
    }

    public AnyLLMKit useVolces(VolcesServiceMeta volcesServiceMeta) {
        this.model = SupportedModel.Volces;
        this.serviceMeta = volcesServiceMeta;
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

    public Future<AnyLLMResponse> request(Handler<AnyLLMRequest> requestHandler) {
        AnyLLMRequest anyLLMRequest = AnyLLMRequest.create();
        requestHandler.handle(anyLLMRequest);
        return request(anyLLMRequest);
    }

    public Future<AnyLLMResponse> request(AnyLLMRequest request) {
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
    }
}