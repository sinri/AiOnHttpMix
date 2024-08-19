package io.github.sinri.AiOnHttpMix.azure.bing.search;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class BingSearchKit {
    private final String subscriptionKey;

    public BingSearchKit(String instanceCode) {
        subscriptionKey = Keel.config("azure.bing.search." + instanceCode + ".SubscriptionKey");
    }

    public Future<JsonObject> callForRaw(JsonObject parameters) {
//        String searchQuery = "赛能和阿司匹林能一起吃吗";
//        String mkt = "zh-CN";

        var x = WebClient.create(Keel.getVertx())
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
                });
    }
}
