package io.github.sinri.AiOnHttpMix.dashscope.qwen.text.tool;

import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface QwenToolDefinition extends JsonifiableEntity<QwenToolDefinition> {

    /**
     * @param name        function的名称，必须是字母、数字，或包含下划线和短划线，最大长度为64。
     * @param description function的描述，供模型选择何时以及如何调用function。
     * @param parameters  表示function的参数描述，需要是一个合法的json schema。缺省表示function没有入参。
     */
    static QwenToolDefinition asFunction(
            @NotNull String name,
            @NotNull String description,
            @Nullable JsonObject parameters
    ) {
        return new QwenToolDefinitionImpl()
                .setType(ToolType.function)
                .setFunction(name, description, parameters);
    }

    static QwenToolDefinition asFunction(
            @NotNull String name,
            @NotNull String description,
            @Nullable List<FunctionArgument> parameters
    ) {
        return new QwenToolDefinitionImpl()
                .setType(ToolType.function)
                .setFunction(name, description, parameters);
    }

    /**
     * 表示tools的类型
     */
    enum ToolType {
        function
    }

    record FunctionArgument(
            String argumentName,
            String argumentType,
            String argumentDesc,
            boolean required
    ) {
    }
}
