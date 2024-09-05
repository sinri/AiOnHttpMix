package io.github.sinri.AiOnHttpMix.azure.bing.search.v7;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

class BingSearchResponseImpl extends UnmodifiableJsonifiableEntityImpl implements BingSearchResponse {
    private final int statusCode;
    private final Map<String, String> headers;
    private final String rawBody;

    public BingSearchResponseImpl(
            int statusCode,
            Map<String, String> headers,
            @Nullable String rawBody
    ) {
        super(prepareBody(rawBody));
        this.statusCode = statusCode;
        this.headers = headers;
        this.rawBody = rawBody;
    }

    private static JsonObject prepareBody(String rawBody) {
        try {
            return new JsonObject(rawBody);
        } catch (Throwable t) {
            return new JsonObject();
        }
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String getRawBody() {
        return rawBody;
    }
}
