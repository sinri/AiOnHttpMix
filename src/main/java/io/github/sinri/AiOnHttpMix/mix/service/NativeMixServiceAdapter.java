package io.github.sinri.AiOnHttpMix.mix.service;

import io.github.sinri.AiOnHttpMix.mix.chat.text.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.chat.text.MixChatResponse;
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

import java.util.function.Function;

public class NativeMixServiceAdapter extends MixServiceAdapter {
    public NativeMixServiceAdapter(KeelConfigElement config) {
        super(config);
    }

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

    @Override
    public Future<Void> requestStream(MixChatRequest request, Function<String, Future<Void>> fragmentHandler) {
        var chatModel = request.getChatModel();
        if (chatModel instanceof GPTModelSpecification) {
            var serviceAdapter = chatModel.buildServiceAdapter(getConfig().extract("azure", "openai"));
            var serviceKit = new GPTKit((OpenAIServiceAdapter) serviceAdapter);
            return serviceKit.chatStream(
                    chatModel,
                    request.toGPTRequest().toJsonObject(),
                    fragmentHandler,
                    request.getTimeout(),
                    request.getRequestId()
            );
        } else if (chatModel instanceof VolcesModelSpecification) {
            var serviceAdapter = chatModel.buildServiceAdapter(getConfig().extract("volces"));
            var serviceKit = new VolcesKit((VolcesServiceAdapter) serviceAdapter);
            return serviceKit.chatStream(
                    chatModel,
                    request.toDoubaoRequest().toJsonObject(),
                    fragmentHandler,
                    request.getTimeout(),
                    request.getRequestId()
            );
        } else if (chatModel instanceof DashscopeModelSpecification) {
            var serviceAdapter = chatModel.buildServiceAdapter(getConfig().extract("dashscope", "qwen"));
            var serviceKit = new QwenKit((QwenServiceAdapter) serviceAdapter);
            return serviceKit.chatStream(
                    chatModel,
                    request.toQwenRequest().toJsonObject(),
                    fragmentHandler,
                    request.getTimeout(),
                    request.getRequestId()
            );
        } else {
            throw new IllegalArgumentException("model is not supported");
        }
    }

    @Override
    public Future<MixChatResponse> requestStream(MixChatRequest request) {
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
