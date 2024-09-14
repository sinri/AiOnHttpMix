package io.github.sinri.AiOnHttpMix.utils;

/**
 * @since 1.1.2
 */
public enum FunctionToolArgumentType {
    STRING("string"),
    INTEGER("int"),
    NUMBER("number"),
    BOOLEAN("boolean");
    private final String code;

    FunctionToolArgumentType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
