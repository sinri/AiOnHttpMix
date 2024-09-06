package io.github.sinri.AiOnHttpMix.azure.openai.dalle;

import io.github.sinri.AiOnHttpMix.azure.openai.core.AzureOpenAIServiceMeta;
import io.github.sinri.AiOnHttpMix.azure.openai.dalle.v3.Dalle3Parameters;
import io.github.sinri.AiOnHttpMix.azure.openai.dalle.v3.Dalle3Response;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

public class Dalle3Kit {

    public Future<JsonObject> draw(
            AzureOpenAIServiceMeta serviceMeta,
            JsonObject parameters,
            String requestId
    ) {
        return serviceMeta.request(
                "/images/generations",
                parameters,
                requestId
        );
    }

    public Future<Dalle3Response> draw(
            AzureOpenAIServiceMeta serviceMeta,
            Dalle3Parameters parameters,
            String requestId
    ) {
        return this.draw(serviceMeta, parameters.toJsonObject(), requestId)
                .compose(jsonObject -> {
                    return Future.succeededFuture(Dalle3Response.wrap(jsonObject));
                });
    }

    public Future<Dalle3Response> draw(
            AzureOpenAIServiceMeta serviceMeta,
            Handler<Dalle3Parameters> parametersHandler,
            String requestId
    ) {
        Dalle3Parameters parameters = Dalle3Parameters.create();
        parametersHandler.handle(parameters);
        return this.draw(serviceMeta, parameters, requestId);
    }


    public enum Dalle3Size {
        LANDSCAPE("1792x1024"),

        SQUARE("1024x1024"),

        PORTRAIT("1024x1792");
        private final String size;

        Dalle3Size(String size) {
            this.size = size;
        }

        public String size() {
            return size;
        }
    }

    public enum Dalle3Quality {
        hd, standard
    }

    public enum Dalle3Style {
        natural, vivid
    }

}
