package io.github.sinri.AiOnHttpMix.test.unit.provider.core;

import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.UUID;

public interface AbstractModelRawUnitTest<M extends ChatModel> extends ModelServiceMixin<M> {


    default Future<Void> toTestSync(JsonObject requestPayload) {
        return getServiceAdapter()
                .request(getModel(), requestPayload, UUID.randomUUID().toString())
                .compose(resp -> {
                    getUnitTestLogger().info("resp", resp);
                    return Future.succeededFuture();
                });
    }

    default Future<Void> toTestStream(JsonObject requestPayload) {
        return getServiceAdapter()
                .requestStream(getModel(), requestPayload, s -> {
                    getUnitTestLogger().info("fragment: " + s);
                    return Future.succeededFuture();
                }, 180_000L, UUID.randomUUID().toString())
                .compose(fin -> {
                    getUnitTestLogger().info("fin");
                    return Future.succeededFuture();
                });
    }
}
