package io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.stream;

import io.github.sinri.AiOnHttpMix.utils.ServiceAdapter;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nullable;

class DoubaoResponseFragmentImpl implements DoubaoResponseFragment {
    private final String data;

    public DoubaoResponseFragmentImpl(String s) {
        //        String[] lines = s.split("[\r\n]+");
        //        for (var line : lines) {
        //            String[] pair = line.split(":\\s*", 2);
        //            if (pair.length == 2) {
        //                if (Objects.equals("data", pair[0])) {
        //                    data = pair[1];
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
