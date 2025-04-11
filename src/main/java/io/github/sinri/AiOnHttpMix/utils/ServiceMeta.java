package io.github.sinri.AiOnHttpMix.utils;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.keel.core.cutter.IntravenouslyCutter;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Function;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public interface ServiceMeta {
    /**
     * @since 1.3.0
     */
    static Future<Void> requestSSEImpl(
            HttpClientOptions httpClientOptions,
            Function<HttpClient, Future<HttpClientResponse>> requestFunction,
            IntravenouslyCutter<String> cutter,
            long maxExecution,
            String requestId
    ) {
        if (maxExecution <= 0)
            throw new IllegalArgumentException("maxExecution must be a positive integer");
        return Keel.useHttpClient(
                httpClientOptions,
                client -> {
                    Promise<Void> promise = Promise.promise();
                    Future.succeededFuture()
                          .compose(v -> requestFunction.apply(client))
                          .andThen(ar -> {
                              if (ar.succeeded()) {
                                  var httpClientResponse = ar.result();
                                  long timer = Keel.getVertx()
                                                   .setTimer(maxExecution, timeout -> {
                                                       client.close();
                                                       promise.tryFail("TIMEOUT FOR REQUEST " + requestId);
                                                   });
                                  httpClientResponse
                                          .handler(buffer -> {
                                              cutter.acceptFromStream(buffer);
                                          })
                                          .endHandler(ended -> {
                                              Keel.getVertx().cancelTimer(timer);
                                              promise.complete();
                                          })
                                          .exceptionHandler(throwable -> {
                                              Keel.getVertx().cancelTimer(timer);
                                              AigcMix.getVerboseLogger().exception(throwable);
                                              promise.tryFail(new RuntimeException("httpClientResponse exception", throwable));
                                          });
                              } else {
                                  AigcMix.getVerboseLogger().exception(ar.cause());
                                  promise.fail(ar.cause());
                              }
                          });

                    return promise.future()
                                  .compose(promised -> {
                                      cutter.stopHere();
                                      return cutter.waitForAllHandled();
                                  }, throwable -> {
                                      AigcMix.getVerboseLogger().exception(throwable);
                                      cutter.stopHere();
                                      return cutter.waitForAllHandled();
                                  });
                }
        );
    }

    /**
     * @since 1.2.2
     */
    Set<SupportedModel> getSupportedModels();

    /**
     * @since 1.2.2
     */
    default boolean isModelSupported(SupportedModel model) {
        return getSupportedModels().contains(model);
    }

    Future<JsonObject> request(
            String api,
            JsonObject requestBody,
            String requestId
    );

    Future<Void> requestSSE(
            String api,
            @NotNull JsonObject parameters,
            IntravenouslyCutter<String> cutter,
            int maxExecutionSeconds,
            String requestId
    );

    SupportedProvider getSupportedProvider();

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
}
