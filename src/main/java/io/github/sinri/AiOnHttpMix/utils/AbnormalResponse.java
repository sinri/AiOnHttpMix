package io.github.sinri.AiOnHttpMix.utils;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import org.jetbrains.annotations.Nullable;


public class AbnormalResponse extends RuntimeException {
    private final int statusCode;
    private final String responseBody;

    public AbnormalResponse(HttpResponse<Buffer> httpResponse) {
        this(httpResponse.statusCode(), httpResponse.bodyAsString());
    }

    private AbnormalResponse(int statusCode, String responseBody) {
        super("[Abnormal Response] STATUS: " + statusCode + "; BODY: " + responseBody);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    @Nullable
    public JsonObject getResponseBodyAsJson() {
        try {
            return new JsonObject(responseBody);
        } catch (Throwable e) {
            return null;
        }
    }
}
