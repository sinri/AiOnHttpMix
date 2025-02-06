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

    public boolean isKeepAliveChunk() {
        return Objects.equals(component, ": keep-alive");
    }

    /**
     * Note: check with isDoneChunk and isKeepAliveChunk first to avoid exception.
     */
    public DeepseekResponseChunk getChunk() {
        try {
            JsonObject entries = new JsonObject(component.substring(6));
            return new DeepseekResponseChunk(entries);
        } catch (Throwable e) {
            throw new RuntimeException("io.github.sinri.AiOnHttpMix.deepseek.chat.chunk.DeepseekResponseChunkString.getChunk failed to parse component: " + component, e);
        }
    }
}
