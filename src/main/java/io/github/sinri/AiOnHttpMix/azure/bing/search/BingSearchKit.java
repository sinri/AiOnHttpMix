package io.github.sinri.AiOnHttpMix.azure.bing.search;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.azure.bing.search.v7.BingSearchParameters;
import io.github.sinri.AiOnHttpMix.azure.bing.search.v7.BingSearchResponse;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

/**
 * BingSearchKit provides methods to interact with the Bing Web Search API (v7) using a given subscription key.
 * <p>
 * This class allows you to perform web searches by constructing requests with various parameters and handling responses.
 * It supports both generic parameter input via {@link JsonObject} and type-safe input via {@link io.github.sinri.AiOnHttpMix.azure.bing.search.v7.BingSearchParameters}.
 * <p>
 * Example usage:
 * <pre>
 *     BingSearchKit kit = new BingSearchKit("your-subscription-key");
 *     BingSearchParameters params = BingSearchParameters.create("search query");
 *     kit.callBingSearch(params, "request-id").onSuccess(response -> { ... });
 * </pre>
 *
 * @author sinri
 */
public class BingSearchKit {
    private final String subscriptionKey;

    /**
     * Constructs a BingSearchKit with the specified Bing API subscription key.
     *
     * @param subscriptionKey The Bing Web Search API subscription key.
     */
    public BingSearchKit(String subscriptionKey) {
        this.subscriptionKey = subscriptionKey;
    }

    /**
     * Calls the Bing Web Search API with the specified parameters and request ID.
     *
     * @param parameters The search parameters as a {@link JsonObject}. Each key-value pair is sent as a query parameter.
     * @param requestId  An identifier for the request, used for logging and tracing.
     * @return A {@link Future} that will be completed with the {@link io.github.sinri.AiOnHttpMix.azure.bing.search.v7.BingSearchResponse} from Bing.
     */
    public Future<BingSearchResponse> callBingSearch(JsonObject parameters, String requestId) {
        AigcMix.getVerboseLogger().info(x -> x
                .message("Start BingSearchKit.callBingSearch")
                .context(j -> j
                        .put("input", parameters)
                        .put("request_id", requestId)));

        return Keel.useWebClient(webClient -> {
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
                                body);

                        AigcMix.getVerboseLogger().info(e -> e
                                .message("bufferHttpResponse in BingSearchKit.callBingSearch")
                                .context(j -> j
                                        .put("output", bingSearchResponse.cloneAsJsonObject())
                                        .put("request_id", requestId)));

                        return Future.succeededFuture(bingSearchResponse);
                    });
        });
    }

    /**
     * Calls the Bing Web Search API with type-safe parameters and request ID.
     *
     * @param parameters The search parameters as a {@link io.github.sinri.AiOnHttpMix.azure.bing.search.v7.BingSearchParameters} object.
     * @param requestId  An identifier for the request, used for logging and tracing.
     * @return A {@link Future} that will be completed with the {@link io.github.sinri.AiOnHttpMix.azure.bing.search.v7.BingSearchResponse} from Bing.
     */
    public Future<BingSearchResponse> callBingSearch(BingSearchParameters parameters, String requestId) {
        return this.callBingSearch(parameters.toJsonObject(), requestId);
    }
}
