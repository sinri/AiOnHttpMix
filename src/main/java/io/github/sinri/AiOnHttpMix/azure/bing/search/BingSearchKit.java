package io.github.sinri.AiOnHttpMix.azure.bing.search;

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

    public Future<JsonObject> callForRaw(JsonObject parameters) {
//        String searchQuery = "赛能和阿司匹林能一起吃吗";
//        String mkt = "zh-CN";

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
                    JsonObject headers = new JsonObject();
                    bufferHttpResponse.headers().forEach(headers::put);
                    String body = bufferHttpResponse.bodyAsString();
                    return Future.succeededFuture(new JsonObject()
                            .put("status_code", bufferHttpResponse.statusCode())
                            .put("headers", headers)
                            .put("raw_body", body)
                    );
                })
                .andThen(ar -> {
                    webClient.close();
                });
    }

    public Future<BingSearchResponse> callBingSearch(JsonObject parameters) {
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
                    return Future.succeededFuture(bingSearchResponse);
                })
                .andThen(ar -> {
                    webClient.close();
                });
    }

    public Future<BingSearchResponse> callBingSearch(BingSearchParameters parameters) {
        return this.callBingSearch(parameters.toJsonObject());
    }
}
