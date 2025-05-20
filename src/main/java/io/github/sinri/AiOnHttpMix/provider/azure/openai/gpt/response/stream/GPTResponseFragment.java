package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.stream;

import io.vertx.core.json.JsonObject;

public interface GPTResponseFragment {
    static GPTResponseFragment wrap(String s) {
        return new GPTResponseFragmentImpl(s);
    }

    String getRawData();

    JsonObject getData();
}
