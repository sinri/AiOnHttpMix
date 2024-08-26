package io.github.sinri.AiOnHttpMix.volces.v3.mixin.request;

import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.github.sinri.keel.core.SelfInterface;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonArray;
import org.jetbrains.annotations.Nullable;

public interface VolcesChatMessageParamMixin<E> extends JsonifiableEntity<E>, SelfInterface<E> {
    default VolcesKit.ChatRole getRole(){
        return VolcesKit.ChatRole.valueOf(readString("role"));
    }
    default E setRole(VolcesKit.ChatRole role){
        this.toJsonObject().put("role",role.name());
        return getImplementation();
    }

    @Nullable
    default String getContent(){
        return readString("content");
    }

    default E setContent(@Nullable String content){
        this.toJsonObject().put("content",content);
        return getImplementation();
    }

    @Nullable
    default String getToolCallId(){
        return readString("tool_call_id");
    }

    default E setToolCallId(@Nullable String toolCallId){
        this.toJsonObject().put("tool_call_id",toolCallId);
        return getImplementation();
    }

    default E setToolCall(VolcesKit.MessageToolCall toolCallParam){
        JsonArray array = this.toJsonObject().getJsonArray("tool_calls");
        if(array==null){
            array = new JsonArray();
            this.toJsonObject().put("tool_calls",array);
        }
        array.add(toolCallParam.toJsonObject());
        return getImplementation();
    }

}
