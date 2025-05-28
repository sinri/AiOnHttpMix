package io.github.sinri.AiOnHttpMix.mix.service;

import io.github.sinri.AiOnHttpMix.mirage.MirageConfigElement;
import io.github.sinri.AiOnHttpMix.mirage.MirageKit;
import io.github.sinri.AiOnHttpMix.mix.chat.request.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.chat.response.MixChatResponse;
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
        //        MirageRequestEntity mirageRequestEntity = new MirageRequestEntity();
        //
        //        request.getMessages().forEach(mixChatMessage -> {
        //            mixChatMessage.getRole();
        //        });
        //
        //        return mirageKit.requestSync(
        //                request.getSupportedModelEnum().name(),
        //                true,
        //                mirageRequestEntity
        //        );
        // todo
        throw new RuntimeException("TODO");
    }

    @Override
    public Future<Void> requestStream(MixChatRequest request, Function<String, Future<Void>> fragmentHandler) {
        // todo
        throw new RuntimeException("TODO");
    }

    @Override
    public Future<MixChatResponse> requestStream(MixChatRequest request) {
        // todo
        throw new RuntimeException("TODO");
    }
}
