package io.github.sinri.AiOnHttpMix.mirage.chat;

import io.github.sinri.AiOnHttpMix.mix.AnyLLMRole;
import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMFunctionToolDefinition;
import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMSimpleRoleMessagePair;
import io.github.sinri.AiOnHttpMix.utils.FunctionToolArgumentDefinition;
import io.github.sinri.AiOnHttpMix.utils.FunctionToolArgumentType;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class MirageRequestEntity implements JsonifiableEntity<MirageRequestEntity> {
    private JsonObject jsonObject;

    public MirageRequestEntity() {
        this.jsonObject = new JsonObject();
    }

    public MirageRequestEntity(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public static MirageRequestEntity fromNayCodeEncodedString(@NotNull String nyacode) {
        return new MirageRequestEntity(new JsonObject(Keel.stringHelper().decodeFromNyaCode(nyacode)));
    }
    /**
     * @since 1.1.5
     */
    public List<AnyLLMSimpleRoleMessagePair> getPrompt() {
        var array = readJsonObjectArray("prompt");
        if (array == null) {
            return null;
        }
        List<AnyLLMSimpleRoleMessagePair> list = new ArrayList<>();
        array.forEach(item -> {
            AnyLLMRole role = AnyLLMRole.valueOf(item.getString("role"));
            String message = item.getString("message");
            var pair = new AnyLLMSimpleRoleMessagePair(role, message);
            list.add(pair);
        });
        return list;
    }

    /**
     * @since 1.1.5
     * @since 1.1.11 save JsonObject expression of AnyLLMSimpleRoleMessagePair into `prompt`.
     */
    public MirageRequestEntity addToPrompt(AnyLLMSimpleRoleMessagePair pair) {
        var x = this.jsonObject.getJsonArray("prompt");
        if (x == null) {
            x = new JsonArray();
            this.jsonObject.put("prompt", x);
        }
        x.add(pair.toJsonObject());
        return this;
    }

    public MirageRequestEntity addFunctionDefinition(
            String functionName,
            String functionDescription,
            List<FunctionToolArgumentDefinition> arguments
    ) {
        JsonArray array = this.jsonObject.getJsonArray("function_array");
        if (array == null) {
            array = new JsonArray();
            this.jsonObject.put("function_array", array);
        }

        JsonArray argumentsArray = new JsonArray();
        if (arguments != null) {
            arguments.forEach(d -> {
                var j = new JsonObject()
                        .put("type", d.argumentType().getCode())
                        .put("name", d.name())
                        .put("description", d.desc());
                argumentsArray.add(j);
            });
        }

        array.add(new JsonObject()
                .put("function_name", functionName)
                .put("function_description", functionDescription)
                .put("arguments", argumentsArray)
        );
        return this;
    }

    public List<AnyLLMFunctionToolDefinition> getFunctionToolDefinitionList() {
        var array = readJsonObjectArray("function_array");
        if (array == null) {
            return null;
        }
        List<AnyLLMFunctionToolDefinition> list = new ArrayList<>();
        array.forEach(item -> {
            String functionName = item.getString("function_name");
            Objects.requireNonNull(functionName);
            String functionDescription = item.getString("function_description");
            Objects.requireNonNull(functionDescription);

            AnyLLMFunctionToolDefinition.Builder builder = AnyLLMFunctionToolDefinition.builder()
                    .functionName(functionName)
                    .functionDescription(functionDescription);

            JsonArray arguments = item.getJsonArray("arguments");
            if (arguments != null) {
                arguments.forEach(property -> {
                    if (property instanceof JsonObject) {
                        FunctionToolArgumentType type = FunctionToolArgumentType.valueOf(((JsonObject) property).getString("type"));
                        String string = ((JsonObject) property).getString("name");
                        String description = ((JsonObject) property).getString("description");

                        builder.property(type, string, description);
                    }
                });
            }

            var x = builder.build();
            list.add(x);
        });
        return list;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull MirageRequestEntity reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    @Override
    public String toString() {
        return toJsonObject().toString();
    }
}
