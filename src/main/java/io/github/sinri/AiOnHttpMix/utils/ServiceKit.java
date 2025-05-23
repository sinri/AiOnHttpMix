package io.github.sinri.AiOnHttpMix.utils;

import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.function.Function;

public interface ServiceKit<
        I extends UnmodifiableJsonifiableEntity,
        O extends UnmodifiableJsonifiableEntity,
        C extends UnmodifiableJsonifiableEntity
        > {
    ServiceAdapter getServiceAdapter();

    default Future<JsonObject> chat(
            ChatModel chatModel,
            JsonObject rawRequest,
            String requestId
    ) {
        return getServiceAdapter().request(
                chatModel,
                rawRequest,
                requestId
        );
    }

    Future<O> chat(
            ChatModel chatModel,
            I request,
            String requestId
    );

    Future<Void> chatStream(
            ChatModel chatModel,
            JsonObject requestPayload,
            Function<String, Future<Void>> cutterProcessFunc,
            long cutterTimeout,
            String requestId
    );

    Future<Void> chatStream(
            ChatModel chatModel,
            I request,
            Function<C, Future<Void>> cutterProcessFunc,
            long cutterTimeout,
            String requestId
    );

    Future<O> chatStream(
            ChatModel chatModel,
            I request,
            long cutterTimeout,
            String requestId
    );
}
