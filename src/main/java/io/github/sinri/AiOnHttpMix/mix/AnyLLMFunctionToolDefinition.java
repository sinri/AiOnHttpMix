package io.github.sinri.AiOnHttpMix.mix;

import io.github.sinri.AiOnHttpMix.utils.FunctionToolArgumentDefinition;
import io.github.sinri.AiOnHttpMix.utils.FunctionToolArgumentType;
import io.github.sinri.AiOnHttpMix.utils.FunctionToolDefinition;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 1.1.0
 */
public class AnyLLMFunctionToolDefinition implements FunctionToolDefinition<AnyLLMFunctionToolDefinition> {
    private JsonObject jsonObject;

    public AnyLLMFunctionToolDefinition() {
        jsonObject = new JsonObject();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static AnyLLMFunctionToolDefinition wrap(JsonObject jsonObject) {
        return new AnyLLMFunctionToolDefinition().reloadDataFromJsonObject(jsonObject);
    }

    @Override
    public @NotNull AnyLLMFunctionToolDefinition getImplementation() {
        return this;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return this.jsonObject;
    }

    /**
     * @since 1.1.5
     */
    public String getFunctionName() {
        return jsonObject.getJsonObject("function").getString("name");
    }

    /**
     * @since 1.1.5
     */
    public String getFunctionDescription() {
        return jsonObject.getJsonObject("function").getString("description");
    }

    /**
     * @return
     * @since 1.1.5
     */
    @Nullable
    public List<FunctionToolArgumentDefinition> getFunctionArgumentDefinitions() {
        var x = jsonObject.getJsonObject("function");
        var y = x.getJsonObject("parameters");
        if (y == null) {
            return null;
        } else {
            List<FunctionToolArgumentDefinition> list = new ArrayList<>();
            var properties = y.getJsonObject("properties");
            properties.forEach(entry -> {
                String argumentName = entry.getKey();
                JsonObject meta = (JsonObject) entry.getValue();
                String description = meta.getString("description");
                String type = meta.getString("type");
                var d = new FunctionToolArgumentDefinition(FunctionToolArgumentType.fromCode(type), argumentName, description);
                list.add(d);
            });
            return list;
        }
    }

    @Override
    public @NotNull AnyLLMFunctionToolDefinition reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    public static class Builder extends FunctionToolDefinitionBuilder<Builder, AnyLLMFunctionToolDefinition> {

        @Override
        public AnyLLMFunctionToolDefinition build() {
            return new AnyLLMFunctionToolDefinition().reloadDataFromJsonObject(toJsonObject());
        }

        @Override
        public @NotNull Builder getImplementation() {
            return this;
        }
    }
}
