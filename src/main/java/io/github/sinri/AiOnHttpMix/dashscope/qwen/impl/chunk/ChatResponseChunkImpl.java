package io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.chunk;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class ChatResponseChunkImpl implements QwenKit.ChatResponseChunk {
    private Integer id;
    private String event;
    private Integer httpStatus;
    private String dataString;

    public ChatResponseChunkImpl(@NotNull String text) {
        String[] split = text.split("[\r\n]+");
        for (var s : split) {
            if (s.startsWith("id:")) {
                id = Integer.valueOf(s.replaceAll("^id:\\s*", ""));
            } else if (s.startsWith("event:")) {
                event = s.replaceAll("^event:\\s*", "");
            } else if (s.startsWith(":HTTP_STATUS/")) {
                httpStatus = Integer.valueOf(s.replaceAll("^:HTTP_STATUS/", ""));
            } else if (s.startsWith("data:")) {
                dataString = s.replaceAll("^data:\\s*", "");
            }
        }
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getEvent() {
        return event;
    }

    @Override
    public Integer getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getDataAsString() {
        return dataString;
    }

    @Override
    public JsonObject toJsonObject() {
        return new JsonObject()
                .put("id", id)
                .put("event", event)
                .put("httpStatus", httpStatus)
                .put("data", dataString);
    }

    @Override
    public String toString() {
        return toJsonObject().toString();
    }
}
