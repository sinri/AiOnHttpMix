package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.stream;

import io.github.sinri.AiOnHttpMix.utils.ServiceAdapter;
import io.vertx.core.json.JsonObject;

class GPTResponseFragmentImpl implements GPTResponseFragment {
    private final String data;

    public GPTResponseFragmentImpl(String s) {
        //        var lines = s.split("[\r\n]+");
        //        for (var line : lines) {
        //            var pair = line.split(":\\s*", 2);
        //            if (pair.length == 2) {
        //                if (Objects.equals(pair[0], "data")) {
        //                    this.data = pair[1];
        //                    break;
        //                }
        //            }
        //        }

        this.data = ServiceAdapter.extractFragmentData(s);
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
