package io.github.sinri.AiOnHttpMix.utils;

import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;
import io.github.sinri.AiOnHttpMix.utils.series.ChatModelSeries;
import io.github.sinri.keel.core.cutter.IntravenouslyCutterOnString;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.json.JsonObject;

import java.util.Set;
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

    ServiceProvider getServiceProvider();

    Set<ChatModelSeries> getChatModelSeries();

    default boolean isChatModelSeriesSupported(ChatModelSeries chatModelSeries) {
        return getChatModelSeries().contains(chatModelSeries);
    }

    default boolean isChatModelSupported(ChatModel chatModel) {
        return this.isChatModelSeriesSupported(chatModel.getSeries());
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
