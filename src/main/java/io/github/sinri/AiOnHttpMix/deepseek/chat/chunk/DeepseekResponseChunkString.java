package io.github.sinri.AiOnHttpMix.deepseek.chat.chunk;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DeepseekResponseChunkString {
    private final String component;

    public DeepseekResponseChunkString(@NotNull String component) {
        this.component = component;
    }

    public boolean isDoneChunk() {
        return Objects.equals(component, "data: [DONE]");
    }

    public DeepseekResponseChunk getChunk() {
        return new DeepseekResponseChunk(new JsonObject(component.substring(6)));
    }
}
