package io.github.sinri.AiOnHttpMix.moonshot.core;

import io.github.sinri.AiOnHttpMix.utils.ServiceMeta;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.github.sinri.AiOnHttpMix.utils.SupportedProvider;
import io.github.sinri.keel.core.cutter.IntravenouslyCutter;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class MoonshotServiceMeta implements ServiceMeta {
    /**
     * @since 1.2.2
     */
    private static final Set<SupportedModel> supportedModels = new HashSet<>();
    private static final String host = "api.moonshot.cn";
    private static final String endpoint = "https://" + host;//+"/v1";

    static {
        // since 1.2.2
        // however, no money there, see you later.
    }

    private final String apiKey;
    private final long streamTimeout = 180_000L;

    public MoonshotServiceMeta(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * @since 1.2.2
     */
    @Override
    public Set<SupportedModel> getSupportedModels() {
        return supportedModels;
    }

    @Override
    public Future<JsonObject> request(String api, JsonObject requestBody, String requestId) {
        WebClient webClient = WebClient.create(Keel.getVertx());
        return Future.succeededFuture(endpoint + api)
                     .compose(url -> {
                         var req = webClient
                                 .postAbs(url)
                                 .bearerTokenAuthentication(apiKey);
                         return Future.succeededFuture(req);
                     })
                     .compose(req -> {
                         return req.sendJsonObject(requestBody);
                     })
                     .compose(bufferHttpResponse -> {
                         int statusCode = bufferHttpResponse.statusCode();
                         if (statusCode == 200) {
                             return Future.succeededFuture(bufferHttpResponse.bodyAsJsonObject());
                         } else {
                             return Future.failedFuture("MoonshotServiceMeta Request Failed, status code is " + statusCode + ", request id is " + requestId);
                         }
                     })
                     .andThen(ar -> {
                         webClient.close();
                     });
    }

    @Override
    public Future<Void> requestSSE(String api, @NotNull JsonObject parameters, IntravenouslyCutter<String> cutter, int maxExecutionSeconds, String requestId) {
        return ServiceMeta.requestSSEImpl(
                new HttpClientOptions()
                        .setKeepAlive(true)
                        .setSsl(true)
                        .setDefaultHost(host)
                        .setDefaultPort(443),
                client -> client.request(HttpMethod.POST, api)
                                .compose(httpClientRequest -> {
                                    httpClientRequest
                                            .putHeader("Content-Type", "application/json")
                                            .putHeader("Authorization", "Bearer " + this.apiKey);
                                    return httpClientRequest.send(parameters.toString());
                                }),
                cutter,
                maxExecutionSeconds * 1000L,
                requestId
        );
    }

    public Future<JsonObject> requestGet(String api, String requestId) {
        WebClient webClient = WebClient.create(Keel.getVertx());
        return webClient
                .getAbs(endpoint + api)
                .bearerTokenAuthentication(apiKey)
                .send()
                .compose(bufferHttpResponse -> {
                    return Future.succeededFuture(bufferHttpResponse.bodyAsJsonObject());
                })
                .andThen(ar -> {
                    webClient.close();
                });
    }

    @Override
    public SupportedProvider getSupportedProvider() {
        return null;
    }

}
