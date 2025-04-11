package io.github.sinri.AiOnHttpMix.mirage.vl;

import io.github.sinri.AiOnHttpMix.mix.AnyLLMRole;
import io.github.sinri.AiOnHttpMix.mix.vl.AnyVLLMMessageComponent;
import io.github.sinri.AiOnHttpMix.mix.vl.AnyVLLMMessageComponentType;
import io.github.sinri.AiOnHttpMix.mix.vl.AnyVLLMRoleMessagePair;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class MirageVLRequestEntity implements JsonifiableEntity<MirageVLRequestEntity> {
    private JsonObject jsonObject;

    public MirageVLRequestEntity() {
        this.jsonObject = new JsonObject();
    }

    public MirageVLRequestEntity(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public static MirageVLRequestEntity fromNayCodeEncodedString(@NotNull String nyacode) {
        return new MirageVLRequestEntity(new JsonObject(Keel.stringHelper().decodeFromNyaCode(nyacode)));
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull MirageVLRequestEntity reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    @Override
    public String toString() {
        return toJsonObject().toString();
    }

    public Integer getMaxExecutionSeconds() {
        return jsonObject.getInteger("max_execution_seconds");
    }

    public MirageVLRequestEntity setMaxExecutionSeconds(int max_execution_seconds) {
        jsonObject.put("max_execution_seconds", max_execution_seconds);
        return this;
    }

    public List<AnyVLLMRoleMessagePair> getPrompt() {
        JsonArray jsonArray = jsonObject.getJsonArray("prompt");
        if (jsonArray == null) {
            return null;
        }
        List<AnyVLLMRoleMessagePair> list = new ArrayList<>();
        jsonArray.forEach(item -> {
            AnyLLMRole role = AnyLLMRole.valueOf(((JsonObject) item).getString("role"));
            JsonArray components = ((JsonObject) item).getJsonArray("components");
            List<AnyVLLMMessageComponent> content = new ArrayList<>();
            components.forEach(contentItem -> {
                ((JsonObject) contentItem).forEach(entry -> {
                    var x = new AnyVLLMMessageComponent(
                            AnyVLLMMessageComponentType.valueOf(entry.getKey()),
                            (String) entry.getValue()
                    );
                    content.add(x);
                });
            });
            list.add(new AnyVLLMRoleMessagePair(role, content));
        });
        return list;
    }

    public MirageVLRequestEntity addToPrompt(AnyVLLMRoleMessagePair pair) {
        JsonArray jsonArray = jsonObject.getJsonArray("prompt");
        if (jsonArray == null) {
            jsonArray = new JsonArray();
            jsonObject.put("prompt", jsonArray);
        }

        jsonArray.add(pair.toJsonObject());
        return this;
    }


}
