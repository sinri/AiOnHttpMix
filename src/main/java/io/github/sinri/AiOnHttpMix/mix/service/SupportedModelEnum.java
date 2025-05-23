package io.github.sinri.AiOnHttpMix.mix.service;

import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.models.azure.openai.gpt.GPTChatModelSeries;
import io.github.sinri.AiOnHttpMix.utils.models.dashscope.qwen.QwenChatModelSeries;
import io.github.sinri.AiOnHttpMix.utils.models.volces.deepseek.VolcesDeepSeekChatModelSeries;
import io.github.sinri.AiOnHttpMix.utils.models.volces.doubao.DoubaoChatModelSeries;
import io.github.sinri.AiOnHttpMix.utils.models.volces.moonshot.VolcesMoonshotChatModelSeries;

public enum SupportedModelEnum {
    ChatGPT(GPTChatModelSeries.NAME_OF_MODEL_SERIES, GPTChatModelSeries.MODEL_NAME_OF_GPT_4O),
    QwenPlus(QwenChatModelSeries.NAME_OF_MODEL_SERIES, QwenChatModelSeries.MODEL_NAME_OF_QWEN_PLUS),
    QwenPlusLatest(QwenChatModelSeries.NAME_OF_MODEL_SERIES, QwenChatModelSeries.MODEL_NAME_OF_QWEN_PLUS_LATEST),
    QwenMax(QwenChatModelSeries.NAME_OF_MODEL_SERIES, QwenChatModelSeries.MODEL_NAME_OF_QWEN_MAX),
    QwenMaxLatest(QwenChatModelSeries.NAME_OF_MODEL_SERIES, QwenChatModelSeries.MODEL_NAME_OF_QWEN_MAX_LATEST),
    QwenTurbo(QwenChatModelSeries.NAME_OF_MODEL_SERIES, QwenChatModelSeries.MODEL_NAME_OF_QWEN_TURBO),
    QwenTurboLatest(QwenChatModelSeries.NAME_OF_MODEL_SERIES, QwenChatModelSeries.MODEL_NAME_OF_QWEN_TURBO_LATEST),
    QwenLong(QwenChatModelSeries.NAME_OF_MODEL_SERIES, QwenChatModelSeries.MODEL_NAME_OF_QWEN_LONG),
    //    DeepSeekReasonerOnDashScope(SupportedProvider.DashScope, "deepseek-r1"),
    //    DeepSeekChatOnDashScope(SupportedProvider.DashScope, "deepseek-v3"),
    Doubao(DoubaoChatModelSeries.NAME_OF_MODEL_SERIES, DoubaoChatModelSeries.MODEL_NAME_OF_DOUBAO_1D5_THINKING_PRO_250415),
    KimiOnVolces(VolcesMoonshotChatModelSeries.NAME_OF_MODEL_SERIES, VolcesMoonshotChatModelSeries.MODEL_NAME_OF_MOONSHOT_V1_128k),
    DeepSeekReasonerOnVolces(VolcesDeepSeekChatModelSeries.NAME_OF_MODEL_SERIES, VolcesDeepSeekChatModelSeries.MODEL_NAME_OF_DEEPSEEK_R1_250120),
    DeepSeekChatOnVolces(VolcesDeepSeekChatModelSeries.NAME_OF_MODEL_SERIES, VolcesDeepSeekChatModelSeries.MODEL_NAME_OF_DEEPSEEK_V3_241226),

    //    DeepSeekChat(SupportedProvider.DeepSeek, DeepseekModel.ChatModel.getCode()),
    //    DeepSeekReasoner(SupportedProvider.DeepSeek, DeepseekModel.ReasonerModel.getCode()),
    ;

    private final String seriesName;
    private final String chatModelName;

    SupportedModelEnum(String seriesName, String chatModelName) {
        this.seriesName = seriesName;
        this.chatModelName = chatModelName;
    }

    public ChatModel getChatModel() {
        return switch (seriesName) {
            case GPTChatModelSeries.NAME_OF_MODEL_SERIES -> new GPTChatModelSeries() {
                @Override
                public String getModelName() {
                    return chatModelName;
                }
            };
            case QwenChatModelSeries.NAME_OF_MODEL_SERIES -> new QwenChatModelSeries() {
                @Override
                public String getModelName() {
                    return chatModelName;
                }
            };
            case VolcesDeepSeekChatModelSeries.NAME_OF_MODEL_SERIES -> new VolcesDeepSeekChatModelSeries() {
                @Override
                public String getModelName() {
                    return chatModelName;
                }
            };
            case DoubaoChatModelSeries.NAME_OF_MODEL_SERIES -> new DoubaoChatModelSeries() {
                @Override
                public String getModelName() {
                    return chatModelName;
                }
            };
            case VolcesMoonshotChatModelSeries.NAME_OF_MODEL_SERIES -> new VolcesMoonshotChatModelSeries() {
                @Override
                public String getModelName() {
                    return chatModelName;
                }
            };
            default -> throw new IllegalStateException("Unexpected value: " + seriesName);
        };
    }
}
