package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.stream;

import io.vertx.core.json.JsonObject;

import java.util.Objects;

class GPTResponseFragmentImpl implements GPTResponseFragment {
    private String data;

    public GPTResponseFragmentImpl(String s) {
        var lines = s.split("[\r\n]+");
        for (var line : lines) {
            var pair = line.split(":\s*", 2);
            if (pair.length == 2) {
                if (Objects.equals(pair[0], "data")) {
                    this.data = pair[1];
                    break;
                }
            }
        }
    }

    @Override
    public String getRawData() {
        return data;
    }

    @Override
    public JsonObject getData() {
        try {
            return new JsonObject(data);
        } catch (Throwable e) {
            return null;
        }
    }
}
