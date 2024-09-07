package io.github.sinri.AiOnHttpMix.utils;

import io.github.sinri.keel.core.cutter.Cutter;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
            Cutter<String> cutter,
            String requestId
    );

    SupportedModelSeries getSupportedModelSeries();

    class AbnormalResponse extends Exception {
        private final int statusCode;
        private final String responseBody;

        public AbnormalResponse(int statusCode, String responseBody) {
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

    default long getStreamTimeout() {
        return 180_000L;
    }
}
