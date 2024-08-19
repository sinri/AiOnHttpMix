package io.github.sinri.AiOnHttpMix.azure.chat;

import io.vertx.core.json.JsonObject;

public class ChatMessage {
    private ChatRole chatRole;
    private String content;

    public ChatMessage(ChatRole chatRole, String content) {
        this.chatRole = chatRole;
        this.content = content;
    }

    public ChatMessage setChatRole(ChatRole chatRole) {
        this.chatRole = chatRole;
        return this;
    }

    public ChatMessage setContent(String content) {
        this.content = content;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ChatRole getChatRole() {
        return chatRole;
    }

    public JsonObject toJsonObject() {
        return new JsonObject()
                .put("role", chatRole.name())
                .put("content", content);
    }

    @Override
    public String toString() {
        return toJsonObject().toString();
    }
}
