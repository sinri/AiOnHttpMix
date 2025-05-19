package io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.stream;

import io.vertx.core.json.JsonObject;

import javax.annotation.Nullable;

public interface DoubaoResponseFragment {
    static DoubaoResponseFragment wrap(String s) {
        return new DoubaoResponseFragmentImpl(s);
    }

    String getRawData();

    @Nullable
    JsonObject getData();
}
