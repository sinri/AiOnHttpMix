package io.github.sinri.AiOnHttpMix.azure.bing.search;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.azure.bing.search.v7.BingSearchParameters;
import io.github.sinri.AiOnHttpMix.azure.bing.search.v7.BingSearchResponse;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

import java.util.HashMap;
import java.util.Map;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class BingSearchKit {
    private final String subscriptionKey;

    public BingSearchKit(String subscriptionKey) {
        this.subscriptionKey = subscriptionKey;
    }

    public Future<BingSearchResponse> callBingSearch(JsonObject parameters, String requestId) {
        AigcMix.getVerboseLogger().info(
                "Start BingSearchKit.callBingSearch",
                j -> j
                        .put("input", parameters)
                        .put("request_id", requestId)
        );

        WebClient webClient = WebClient.create(Keel.getVertx());
        var x = webClient
                .get(443, "api.bing.microsoft.com", "/v7.0/search")
                .ssl(true);
        parameters.forEach(e -> {
            x.addQueryParam(e.getKey(), String.valueOf(e.getValue()));
        });

        return x.putHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
                .send()
                .compose(bufferHttpResponse -> {
                    Map<String, String> headers = new HashMap<>();
                    bufferHttpResponse.headers().forEach(headers::put);

                    String body = bufferHttpResponse.bodyAsString();
                    BingSearchResponse bingSearchResponse = BingSearchResponse.wrap(
                            bufferHttpResponse.statusCode(),
                            headers,
                            body
                    );

                    AigcMix.getVerboseLogger().info(
                            "bufferHttpResponse in BingSearchKit.callBingSearch",
                            j -> j
                                    .put("output", bingSearchResponse.cloneAsJsonObject())
                                    .put("request_id", requestId)
                    );

                    return Future.succeededFuture(bingSearchResponse);
                })
                .andThen(ar -> {
                    webClient.close();
                });
    }

    public Future<BingSearchResponse> callBingSearch(BingSearchParameters parameters, String requestId) {
        return this.callBingSearch(parameters.toJsonObject(), requestId);
    }
}
