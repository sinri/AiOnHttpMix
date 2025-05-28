package io.github.sinri.AiOnHttpMix.mix.service;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.mirage.MirageConfigElement;
import io.github.sinri.AiOnHttpMix.mirage.MirageKit;
import io.github.sinri.AiOnHttpMix.mix.chat.request.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.chat.response.MixChatResponse;
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
    public Future<Void> requestStream(MixChatRequest request, Function<JsonObject, Future<Void>> fragmentDataHandler) {
        return mirageKit.requestStream(true, request, fragmentData -> {
            AigcMix.getVerboseLogger()
                   .debug("MirageMixServiceAdapter.requestStream with fragment data: \n" + fragmentData);
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
    public Future<MixChatResponse> requestStream(MixChatRequest request) {
        ChatModel chatModel = request.getChatModel();
        if (chatModel instanceof GPTModelSpecification) {
            GPTResponseBuffer buffer = new GPTResponseBuffer();
            return mirageKit.requestStream(
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
            return mirageKit.requestStream(
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
            return mirageKit.requestStream(
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
