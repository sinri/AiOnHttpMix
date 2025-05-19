package io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.stream;

import io.vertx.core.json.JsonObject;

import javax.annotation.Nullable;
import java.util.Objects;

class DoubaoResponseFragmentImpl implements DoubaoResponseFragment {
    private String data;

    public DoubaoResponseFragmentImpl(String s) {
        String[] lines = s.split("[\r\n]+");
        for (var line : lines) {
            String[] pair = line.split(":\\s*", 2);
            if (pair.length == 2) {
                if (Objects.equals("data", pair[0])) {
                    //                    if(pair[1].startsWith("[DONE]")){
                    //                        break;
                    //                    }
                    data = pair[1];
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
    @Nullable
    public JsonObject getData() {
        try {
            String rawData = getRawData();
            return new JsonObject(rawData);
        } catch (Throwable throwable) {
            return null;
        }
    }
}
