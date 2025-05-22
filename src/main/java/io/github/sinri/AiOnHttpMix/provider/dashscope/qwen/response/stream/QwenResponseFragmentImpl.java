package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.stream;

import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class QwenResponseFragmentImpl implements QwenResponseFragment {
    private String id;
    private String event;
    private String rawData;

    public QwenResponseFragmentImpl(@Nonnull String s) {
        for (String p : s.split("[\r\n]+")) {
            String[] pair = p.split(":", 2);
            if (pair.length == 2) {
                String k = pair[0].trim();
                String v = pair[1].trim();
                switch (k) {
                    case "id":
                        this.id = v;
                        break;
                    case "event":
                        this.event = v;
                        break;
                    case "data":
                        this.rawData = v;
                        break;
                }
            }
        }

    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getEvent() {
        return event;
    }

    @Override
    public String getRawData() {
        return rawData;
    }

    @Override
    public JsonObject getData() {
        return new JsonObject(rawData);
    }
}
