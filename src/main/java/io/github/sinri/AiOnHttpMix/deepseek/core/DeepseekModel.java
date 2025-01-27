package io.github.sinri.AiOnHttpMix.deepseek.core;

public enum DeepseekModel {
    ChatModel("deepseek-chat"),
    ReasonerModel("deepseek-reasoner"),
    ;
    private final String code;

    DeepseekModel(String code) {
        this.code = code;
    }

    public static DeepseekModel fromCode(String code) {
        for (DeepseekModel m : values()) {
            if (m.code.equals(code)) {
                return m;
            }
        }
        throw new IllegalArgumentException("Unknown deepseek model code: " + code);
    }

    public String getCode() {
        return code;
    }
}
