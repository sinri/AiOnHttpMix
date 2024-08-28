package io.github.sinri.AiOnHttpMix.volces.v3.tool;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;

public interface SharedFunctionCall extends UnmodifiableJsonifiableEntity {
    /**
     * @return 模型需要调用的函数名称
     */
    default String getName() {
        return readString("name");
    }

    /**
     * 请注意，模型并不总是生成有效的 JSON，并且可能会虚构出一些您的函数参数规范中未定义的参数。在调用函数之前，请在您的代码中验证这些参数是否有效。
     *
     * @return 模型生成的用于调用函数的参数，JSON 格式。
     */
    default String getArguments() {
        return readString("arguments");
    }
}
