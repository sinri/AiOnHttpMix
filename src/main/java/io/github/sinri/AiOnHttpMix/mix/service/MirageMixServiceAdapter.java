package io.github.sinri.AiOnHttpMix.mix.service;

import io.github.sinri.AiOnHttpMix.mirage.MirageConfigElement;
import io.github.sinri.AiOnHttpMix.mirage.MirageKit;
import io.github.sinri.AiOnHttpMix.mix.chat.request.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.chat.response.MixChatResponse;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.GPTKit;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.stream.GPTResponseBuffer;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.stream.QwenResponseBuffer;
import io.github.sinri.AiOnHttpMix.provider.volces.VolcesKit;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.stream.DoubaoResponseBuffer;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.specification.DashscopeModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.GPTModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.VolcesModelSpecification;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import io.vertx.core.Future;

import java.util.Objects;
import java.util.function.Function;

public class MirageMixServiceAdapter extends MixServiceAdapter {
    private final MirageKit mirageKit;

    public MirageMixServiceAdapter(KeelConfigElement config) {
        super(config);
        KeelConfigElement x = config.extract("mirage");
        Objects.requireNonNull(x);
        MirageConfigElement mirageConfigElement = new MirageConfigElement(x);
        this.mirageKit = new MirageKit(mirageConfigElement);
    }

    @Override
    public Future<MixChatResponse> request(MixChatRequest request) {
        return mirageKit.requestSync(true, request);
    }

    @Override
    public Future<Void> requestStream(MixChatRequest request, Function<String, Future<Void>> fragmentHandler) {
        return mirageKit.requestStream(true, request, fragmentHandler);
    }

    @Override
    public Future<MixChatResponse> requestStream(MixChatRequest request) {
        ChatModel chatModel = request.getChatModel();
        if (chatModel instanceof GPTModelSpecification) {
            GPTResponseBuffer buffer = new GPTResponseBuffer();
            return requestStream(request, fragment -> GPTKit.handleStreamFragment(
                    fragment,
                    chunk -> {
                        buffer.accept(chunk);
                        return Future.succeededFuture();
                    }
            ))
                    .compose(v -> {
                        var x = MixChatResponse.from(buffer.build());
                        return Future.succeededFuture(x);
                    });
        } else if (chatModel instanceof VolcesModelSpecification) {
            DoubaoResponseBuffer buffer = new DoubaoResponseBuffer();
            return requestStream(request, fragment -> VolcesKit.handleStreamFragment(
                    fragment,
                    chunk -> {
                        buffer.accept(chunk);
                        return Future.succeededFuture();
                    }
            ))
                    .compose(v -> {
                        var x = MixChatResponse.from(buffer.build());
                        return Future.succeededFuture(x);
                    });
        } else if (chatModel instanceof DashscopeModelSpecification) {
            QwenResponseBuffer buffer = new QwenResponseBuffer();
            return requestStream(request, fragment -> QwenKit.handleStreamFragment(
                    fragment,
                    chunk -> {
                        buffer.accept(chunk);
                        return Future.succeededFuture();
                    }
            ))
                    .compose(v -> {
                        var x = MixChatResponse.from(buffer.build());
                        return Future.succeededFuture(x);
                    });
        } else {
            throw new IllegalArgumentException("model is not supported");
        }
    }
}
