package io.github.sinri.AiOnHttpMix.utils;

/**
 * @since 1.1.2
 */
public enum FunctionToolArgumentType {
    STRING,
    INTEGER,
    NUMBER,
    BOOLEAN;
    public static final String CODE_OF_STRING = "string";
    public static final String CODE_OF_INTEGER = "integer";
    public static final String CODE_OF_NUMBER = "number";
    public static final String CODE_OF_BOOLEAN = "boolean";

    /**
     * @since 1.1.5
     */
    public static FunctionToolArgumentType fromCode(String code) {
        return switch (code) {
            case CODE_OF_STRING -> STRING;
            case CODE_OF_INTEGER -> INTEGER;
            case CODE_OF_NUMBER -> NUMBER;
            case CODE_OF_BOOLEAN -> BOOLEAN;
            default -> throw new IllegalStateException("Unexpected value: " + code);
        };
    }

    public String getCode() {
        return switch (this) {
            case STRING -> CODE_OF_STRING;
            case INTEGER -> CODE_OF_INTEGER;
            case NUMBER -> CODE_OF_NUMBER;
            case BOOLEAN -> CODE_OF_BOOLEAN;
        };
    }
}
