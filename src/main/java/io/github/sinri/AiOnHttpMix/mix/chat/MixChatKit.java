package io.github.sinri.AiOnHttpMix.mix.chat;

import io.github.sinri.AiOnHttpMix.mix.chat.request.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.chat.response.MixChatResponse;
import io.github.sinri.AiOnHttpMix.mix.service.MixServiceAdapter;
import io.vertx.core.Future;

import java.util.function.Function;

public class MixChatKit {
    private MixServiceAdapter adapter;

    public MixChatKit() {

    }

    public MixServiceAdapter getAdapter() {
        return adapter;
    }

    public MixChatKit setAdapter(MixServiceAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

    public Future<MixChatResponse> chat(MixChatRequest request) {
        return adapter.request(request);
    }

    public Future<Void> chatStream(MixChatRequest request, Function<String, Future<Void>> fragmentHandler) {
        request.setStream(true);
        return adapter.requestStream(request, fragmentHandler);
    }

    public Future<MixChatResponse> chatStream(MixChatRequest request) {
        request.setStream(true);
        return adapter.requestStream(request);
    }
}
