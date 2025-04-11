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

/**
 * Interface defining the contract for a service provider,
 * which includes methods to interact with a Language Model (LLM) Service.
 * It provides capabilities to send requests, manage supported models, and handle Server-Sent Events (SSE).
 */
public interface ServiceMeta {
    /**
     * Initiates a Server-Sent Events (SSE) request using the provided HTTP client options and request function.
     * The method processes the response with the given cutter and ensures the operation does not exceed the specified
     * maximum execution time.
     *
     * @param httpClientOptions the options for the HTTP client
     * @param requestFunction   a function that takes an HttpClient and returns a Future of HttpClientResponse
     * @param cutter            the cutter to process the incoming stream data
     * @param maxExecutionTime  the maximum execution time in milliseconds, must be a positive integer
     * @param requestId         the unique identifier for the request
     * @return a Future that completes when the SSE request is finished or fails if an error occurs
     * @since 1.3.0
     */
    static Future<Void> requestSSEImpl(
            HttpClientOptions httpClientOptions,
            Function<HttpClient, Future<HttpClientResponse>> requestFunction,
            IntravenouslyCutter<String> cutter,
            long maxExecutionTime,
            String requestId
    ) {
        if (maxExecutionTime <= 0) {
            throw new IllegalArgumentException("maxExecution must be a positive integer");
        }
        return Keel.useHttpClient(
                httpClientOptions,
                client -> {
                    Promise<Void> promise = Promise.promise();
                    Future.succeededFuture()
                          .compose(v -> requestFunction.apply(client))
                          .andThen(ar -> {
                              if (ar.succeeded()) {
                                  var httpClientResponse = ar.result();
                                  long timer = Keel.getVertx().setTimer(maxExecutionTime, timeout -> {
                                      client.close();
                                      promise.tryFail("TIMEOUT FOR REQUEST " + requestId);
                                  });
                                  httpClientResponse
                                          .handler(cutter::acceptFromStream)
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

    /**
     * Sends a request, which is specified by a request ID and a JsonObject as its payload,
     * to the LLM Service through specified API.
     *
     * @param api         the endpoint of the API to which the request is sent
     * @param requestBody the JSON object representing the body of the request
     * @param requestId   the unique identifier for the request
     * @return a Future that completes with a JsonObject representing the response from the API
     */
    Future<JsonObject> request(
            String api,
            JsonObject requestBody,
            String requestId
    );

    /**
     * Initiates a Server-Sent Events (SSE) request, which is specified by a request ID and a JsonObject as its
     * payload, to the LLM Service through specified API.
     * The method processes the incoming stream data using the provided cutter and ensures the operation does not exceed
     * the specified maximum execution time.
     *
     * @param api                 the endpoint of the API to which the request is sent
     * @param parameters          the JSON object representing the parameters for the request
     * @param cutter              the cutter to process the incoming stream data
     * @param maxExecutionSeconds the maximum execution time in seconds, must be a positive integer
     * @param requestId           the unique identifier for the request
     * @return a Future that completes when the SSE request is finished or fails if an error occurs
     */
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
