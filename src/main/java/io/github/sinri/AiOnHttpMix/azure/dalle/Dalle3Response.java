package io.github.sinri.AiOnHttpMix.azure.dalle;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class Dalle3Response extends SimpleJsonifiableEntity {
    public Dalle3Response(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    @NotNull
    public Long created() {
        return Objects.requireNonNull(readLong("created"));
    }

    @Nullable
    public List<Datum> data() {
        List<JsonObject> array = readJsonObjectArray("data");
        if (array != null) {
            return array.stream().map(Datum::new).toList();
        } else {
            return null;
        }
    }

    @Nullable
    public Error error() {
        var e = readJsonObject("error");
        if (e == null) return null;
        return new Error(e);
    }

    public static class Datum extends SimpleJsonifiableEntity {
        public Datum(@NotNull JsonObject j) {
            super(j);
        }

        public String url() {
            return readString("url");
        }

        public String revisedPrompt() {
            return readString("revised_prompt");
        }
    }

    public static class Error extends SimpleJsonifiableEntity {
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
