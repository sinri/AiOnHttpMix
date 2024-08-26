package io.github.sinri.AiOnHttpMix.moonshot.kimi.mixin;

import io.github.sinri.AiOnHttpMix.moonshot.kimi.KimiKit;
import io.github.sinri.keel.core.SelfInterface;
import io.github.sinri.keel.core.json.JsonifiableEntity;

public interface KimiToolDefinitionMixin<E> extends JsonifiableEntity<E>, SelfInterface<E> {
    default Type getType(){
        return Type.valueOf(readString("type"));
    }
    default KimiKit.FunctionToolDefinition getFunction(){
        return KimiKit.FunctionToolDefinition.wrap(readJsonObject("function"));
    }
    enum Type {
        function
    }
}
