package io.github.sinri.AiOnHttpMix.azure.dalle;

import io.github.sinri.AiOnHttpMix.azure.AzureOpenAIKit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class AzureOpenAIDalle3Kit extends AzureOpenAIKit {
    public AzureOpenAIDalle3Kit(String serviceName) {
        super(serviceName);
    }

    /**
     * @param prompt  prompt text
     * @param size    supported values are “1792x1024”, “1024x1024” and “1024x1792”
     * @param n       The number of images to generate. Only n=1 is supported for DALL-E 3.
     * @param quality Options are “hd” and “standard”; defaults to standard
     * @param style   Options are “natural” and “vivid”; defaults to “vivid”
     */
    protected Future<Dalle3Response> draw(
            String prompt,
            String size,
            int n,
            String quality,
            String style
    ) {
        return postRequest("/images/generations", new JsonObject()
                .put("prompt", prompt)
                .put("size", size)
                .put("n", n)
                .put("quality", quality)
                .put("style", style)
        )
                .compose(resp -> {
                    var x = new Dalle3Response(resp);
                    return Future.succeededFuture(x);
                });
    }

    public Future<String> drawOneImageForUrl(
            String prompt,
            Dalle3Size size,
            Dalle3Quality quality,
            Dalle3Style style
    ) {
        return draw(prompt, size.size(), 1, quality.name(), style.name())
                .compose(resp -> {
                    var data = resp.data();
                    if (data == null || data.isEmpty()) {
                        Dalle3Response.Error error = resp.error();
                        if (error != null) {
                            String msg = error.code() + " " + error.message();
                            return Future.failedFuture(msg);
                        } else {
                            return Future.failedFuture("Data contains nothing");
                        }
                    } else {
                        var url = data.get(0).url();
                        return Future.succeededFuture(url);
                    }
                });
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
