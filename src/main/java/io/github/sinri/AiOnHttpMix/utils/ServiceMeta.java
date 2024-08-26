package io.github.sinri.AiOnHttpMix.utils;

import io.github.sinri.keel.core.cutter.CutterOnString;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public interface ServiceMeta {
    Future<JsonObject> request(
            String api,
            JsonObject requestBody,
            String requestId
    );

    void requestSSE(
            String api,
            @NotNull JsonObject parameters,
            Promise<Void> promise,
            CutterOnString cutter,
            String requestId
    );
}
