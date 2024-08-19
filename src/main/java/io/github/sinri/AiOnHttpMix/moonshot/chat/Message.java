package io.github.sinri.AiOnHttpMix.moonshot.chat;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class Message extends SimpleJsonifiableEntity {
    /*
    "message":{
                "role":"assistant",
                "content":"",
                "tool_calls":[
                    {
                        "index":0,
                        "id":"weatherQueryFunc:0",
                        "type":"function",
                        "function":{
                            "name":"weatherQueryFunc",
                            "arguments":"{\n    \"day\": \"2024-02-02\",\n    \"place\": \"东京都江户川区\"\n}"
                        }
                    }
                ]
            },
     */
    public Message(JsonObject jsonObject) {
        super(jsonObject);
    }

    public Message(@Nullable Role role, @NotNull String content) {
        super(new JsonObject()
                .put("role", role == null ? null : role.name())
                .put("content", content)
        );
    }

    @Nullable
    public Role getRole() {
        return Role.valueOf(readString("role"));
    }

    public String getContent() {
        return readString("content");
    }

    @Nullable
    public JsonArray getToolCalls() {
        return readJsonArray("tool_calls");
    }
}
