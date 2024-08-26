package io.github.sinri.AiOnHttpMix.azure.openai.dalle;

import io.github.sinri.AiOnHttpMix.azure.openai.core.AzureOpenAIServiceMeta;
import io.github.sinri.AiOnHttpMix.azure.openai.core.mixin.filter.ContentFilterSeverityResult;
import io.github.sinri.AiOnHttpMix.azure.openai.core.impl.filter.ContentFilterSeverityResultImpl;
import io.github.sinri.AiOnHttpMix.azure.openai.dalle.impl.Dalle3ResponseImpl;
import io.github.sinri.AiOnHttpMix.azure.openai.dalle.impl.DalleContentFilterResultsImpl;
import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

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
            Parameters parameters,
            String requestId
    ) {
        return this.draw(serviceMeta, parameters.toJsonObject(), requestId)
                .compose(jsonObject -> {
                    return Future.succeededFuture(Dalle3Response.wrap(jsonObject));
                });
    }

//    /**
//     * @param prompt  prompt text
//     * @param size    supported values are “1792x1024”, “1024x1024” and “1024x1792”
//     * @param n       The number of images to generate. Only n=1 is supported for DALL-E 3.
//     * @param quality Options are “hd” and “standard”; defaults to standard
//     * @param style   Options are “natural” and “vivid”; defaults to “vivid”
//     */
//    protected Future<Dalle3Response> draw(
//            AzureOpenAIServiceMeta serviceMeta,
//            String prompt,
//            String size,
//            int n,
//            String quality,
//            String style,
//            String requestId
//    ) {
//        return serviceMeta.postRequest(
//                        "/images/generations",
//                        new JsonObject()
//                                .put("prompt", prompt)
//                                .put("size", size)
//                                .put("n", n)
//                                .put("quality", quality)
//                                .put("style", style),
//                        requestId
//                )
//                .compose(resp -> {
//                    var x = new Dalle3Response(resp);
//                    return Future.succeededFuture(x);
//                });
//    }
//
//    public Future<String> drawOneImageForUrl(
//            AzureOpenAIServiceMeta serviceMeta,
//            String prompt,
//            Dalle3Size size,
//            Dalle3Quality quality,
//            Dalle3Style style,
//            String requestId
//    ) {
//        return draw(serviceMeta, prompt, size.size(), 1, quality.name(), style.name(), requestId)
//                .compose(resp -> {
//                    var data = resp.data();
//                    if (data == null || data.isEmpty()) {
//                        Dalle3Response.Error error = resp.error();
//                        if (error != null) {
//                            String msg = error.code() + " " + error.message();
//                            return Future.failedFuture(msg);
//                        } else {
//                            return Future.failedFuture("Data contains nothing");
//                        }
//                    } else {
//                        var url = data.get(0).url();
//                        return Future.succeededFuture(url);
//                    }
//                });
//    }

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

    public static class Parameters extends SimpleJsonifiableEntity {
        public Parameters(String prompt) {
            super();
            setPrompt(prompt);
            setN(1);
            setQuality(Dalle3Quality.standard);
            setStyle(Dalle3Style.natural);
            setSize(Dalle3Size.SQUARE);
        }

        public Parameters setPrompt(String prompt) {
            this.jsonObject.put("prompt", prompt);
            return this;
        }

        public Parameters setSize(Dalle3Size size) {
            this.jsonObject.put("size", size.size());
            return this;
        }

        public Parameters setQuality(Dalle3Quality quality) {
            this.jsonObject.put("quality", quality.name());
            return this;
        }

        public Parameters setStyle(Dalle3Style style) {
            this.jsonObject.put("style", style.name());
            return this;
        }

        public Parameters setN(int n) {
            this.jsonObject.put("n", n);
            return this;
        }
    }

    public interface Dalle3Response extends UnmodifiableJsonifiableEntity {
        static Dalle3Response wrap(JsonObject jsonObject) {
            return new Dalle3ResponseImpl(jsonObject);
        }

        @NotNull
        default Long created() {
            return Objects.requireNonNull(readLong("created"));
        }

        @Nullable
        default List<Datum> data() {
            List<JsonObject> array = readJsonObjectArray("data");
            if (array != null) {
                return array.stream().map(Datum::new).toList();
            } else {
                return null;
            }
        }

        @Nullable
        default Error error() {
            var e = readJsonObject("error");
            if (e == null) return null;
            return new Error(e);
        }

        class Datum extends UnmodifiableJsonifiableEntityImpl {
            public Datum(@NotNull JsonObject j) {
                super(j);
            }

            public String url() {
                return readString("url");
            }

            public String revisedPrompt() {
                return readString("revised_prompt");
            }

            public DalleContentFilterResults getContentFilterResults(){
                return DalleContentFilterResults.wrap(readJsonObject("content_filter_results"));
            }
        }

        class Error extends UnmodifiableJsonifiableEntityImpl {
            public Error(JsonObject j) {
                super(j);
            }

            public String code() {
                return readString("code");
            }

            public String message() {
                return readString("message");
            }
        }
    }

    public interface DalleContentFilterResults extends UnmodifiableJsonifiableEntity {
        static DalleContentFilterResults wrap(JsonObject j) {
            return new DalleContentFilterResultsImpl(j);
        }

        @Nullable
        default ContentFilterSeverityResult getSexual() {
            JsonObject entries = readJsonObject("sexual");
            if (entries == null) return null;
            return new ContentFilterSeverityResultImpl(entries);
        }

        @Nullable
        default ContentFilterSeverityResult getViolence() {
            JsonObject entries = readJsonObject("violence");
            if (entries == null) return null;
            return new ContentFilterSeverityResultImpl(entries);
        }

        @Nullable
        default ContentFilterSeverityResult getHate() {
            JsonObject entries = readJsonObject("hate");
            if (entries == null) return null;
            return new ContentFilterSeverityResultImpl(entries);
        }

        @Nullable
        default ContentFilterSeverityResult getSelfHarm() {
            JsonObject entries = readJsonObject("self_harm");
            if (entries == null) return null;
            return new ContentFilterSeverityResultImpl(entries);
        }
    }
}
