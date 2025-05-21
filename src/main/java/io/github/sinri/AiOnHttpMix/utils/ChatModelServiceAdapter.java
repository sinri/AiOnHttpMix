package io.github.sinri.AiOnHttpMix.utils;

import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;
import io.github.sinri.keel.core.cutter.IntravenouslyCutterOnString;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.json.JsonObject;

import java.util.function.Function;

import static io.github.sinri.keel.facade.KeelInstance.Keel;


public interface ChatModelServiceAdapter {

    static Future<Void> callStreamWithCutter(
            HttpClientOptions httpClientOptions,
            Function<HttpClient, Future<HttpClientResponse>> requestFunction,
            Function<String, Future<Void>> cutterProcessFunc,
            long cutterTimeout
    ) {
        return Keel.useHttpClient(
                httpClientOptions,
                client -> Future.succeededFuture()
                                .compose(v -> requestFunction.apply(client))
                                .compose(httpClientResponse -> {
                                    IntravenouslyCutterOnString cutter = new IntravenouslyCutterOnString(cutterProcessFunc::apply, cutterTimeout);
                                    httpClientResponse
                                            .handler(cutter::acceptFromStream)
                                            .endHandler(ended -> cutter.stopHere())
                                            .exceptionHandler(cutter::stopHere);
                                    return cutter.waitForAllHandled();
                                }));
    }

    boolean isModelCompatible(ChatModel chatModel);

    boolean isModelCompatible(ModelSpecification modelSpecification);

    default void assertModelCompatible(ChatModel chatModel) {
        if (!isModelCompatible(chatModel)) {
            throw new IllegalArgumentException("Model " + chatModel.getModelName() + " is not compatible with this service adapter.");
        }
    }

    Future<JsonObject> request(
            ChatModel chatModel,
            JsonObject requestPayload,
            String requestId
    );

    Future<Void> requestStream(
            ChatModel chatModel,
            JsonObject requestPayload,
            Function<String, Future<Void>> cutterProcessFunc,
            long cutterTimeout,
            String requestId
    );

}
