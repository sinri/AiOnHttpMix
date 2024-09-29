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

    /**
     * @param code
     * @return
     * @since 1.1.5
     */
    public static FunctionToolArgumentType fromCode(String code) {
        return switch (code) {
            case "string" -> STRING;
            case "int" -> INTEGER;
            case "number" -> NUMBER;
            case "boolean" -> BOOLEAN;
            default -> throw new IllegalStateException("Unexpected value: " + code);
        };
    }
}
