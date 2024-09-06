package io.github.sinri.AiOnHttpMix.azure.openai.dalle.v3;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

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

        public DalleContentFilterResults getContentFilterResults() {
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
