package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.vision.GPTVisionMessageContent;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface GPTMessage extends JsonifiableEntity<GPTMessage> {
    default String getRole() {
        return readString("role");
    }

    default GPTMessage setRole(String role) {
        return write("role", role);
    }

    default String getContent() {
        return readString("content");
    }

    default List<GPTVisionMessageContent> getContents() {
        List<JsonObject> array = readJsonObjectArray("content");
        if (array == null) return List.of();
        return array.stream().map(GPTVisionMessageContent::wrap).toList();
    }

    default GPTMessage setTextContent(String content) {
        return write("content", content);
    }

    default GPTMessage setVisionContent(List<GPTVisionMessageContent> contentList) {
        JsonArray array = new JsonArray();
        contentList.forEach(c -> array.add(c.toJsonObject()));
        return write("content", array);
    }
}
