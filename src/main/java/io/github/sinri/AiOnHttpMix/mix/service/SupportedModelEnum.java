package io.github.sinri.AiOnHttpMix.mix.service;

import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.models.azure.openai.gpt.GPTTextModelSeries;
import io.github.sinri.AiOnHttpMix.utils.models.azure.openai.gpt.GPTVisionModelSeries;
import io.github.sinri.AiOnHttpMix.utils.models.dashscope.qwen.QwenTextModelSeries;
import io.github.sinri.AiOnHttpMix.utils.models.dashscope.qwen.QwenVisionModelSeries;
import io.github.sinri.AiOnHttpMix.utils.models.volces.deepseek.VolcesDeepSeekTextModelSeries;
import io.github.sinri.AiOnHttpMix.utils.models.volces.doubao.DoubaoTextModelSeries;
import io.github.sinri.AiOnHttpMix.utils.models.volces.doubao.DoubaoVisionModelSeries;
import io.github.sinri.AiOnHttpMix.utils.models.volces.moonshot.VolcesMoonshotTextModelSeries;

public enum SupportedModelEnum {
    /**
     * @deprecated use {@link io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum#ChatGPT4}
     */
    @Deprecated
    ChatGPT(GPTTextModelSeries.NAME_OF_MODEL_SERIES, GPTTextModelSeries.MODEL_NAME_OF_GPT_4),
    ChatGPT4(GPTTextModelSeries.NAME_OF_MODEL_SERIES, GPTTextModelSeries.MODEL_NAME_OF_GPT_4),
    QwenPlus(QwenTextModelSeries.NAME_OF_MODEL_SERIES, QwenTextModelSeries.MODEL_NAME_OF_QWEN_PLUS),
    QwenPlusLatest(QwenTextModelSeries.NAME_OF_MODEL_SERIES, QwenTextModelSeries.MODEL_NAME_OF_QWEN_PLUS_LATEST),
    QwenMax(QwenTextModelSeries.NAME_OF_MODEL_SERIES, QwenTextModelSeries.MODEL_NAME_OF_QWEN_MAX),
    QwenMaxLatest(QwenTextModelSeries.NAME_OF_MODEL_SERIES, QwenTextModelSeries.MODEL_NAME_OF_QWEN_MAX_LATEST),
    QwenTurbo(QwenTextModelSeries.NAME_OF_MODEL_SERIES, QwenTextModelSeries.MODEL_NAME_OF_QWEN_TURBO),
    QwenTurboLatest(QwenTextModelSeries.NAME_OF_MODEL_SERIES, QwenTextModelSeries.MODEL_NAME_OF_QWEN_TURBO_LATEST),
    QwenLong(QwenTextModelSeries.NAME_OF_MODEL_SERIES, QwenTextModelSeries.MODEL_NAME_OF_QWEN_LONG),
    //    DeepSeekReasonerOnDashScope(SupportedProvider.DashScope, "deepseek-r1"),
    //    DeepSeekChatOnDashScope(SupportedProvider.DashScope, "deepseek-v3"),
    /**
     * @deprecated use {@link SupportedModelEnum#Doubao1d5ThinkingPro250415}
     */
    @Deprecated
    Doubao(DoubaoTextModelSeries.NAME_OF_MODEL_SERIES, DoubaoTextModelSeries.MODEL_NAME_OF_DOUBAO_1D5_THINKING_PRO_250415),
    Doubao1d5ThinkingPro250415(DoubaoTextModelSeries.NAME_OF_MODEL_SERIES, DoubaoTextModelSeries.MODEL_NAME_OF_DOUBAO_1D5_THINKING_PRO_250415),
    KimiOnVolces(VolcesMoonshotTextModelSeries.NAME_OF_MODEL_SERIES, VolcesMoonshotTextModelSeries.MODEL_NAME_OF_MOONSHOT_V1_128k),
    DeepSeekReasonerOnVolces(VolcesDeepSeekTextModelSeries.NAME_OF_MODEL_SERIES, VolcesDeepSeekTextModelSeries.MODEL_NAME_OF_DEEPSEEK_R1_250120),
    DeepSeekChatOnVolces(VolcesDeepSeekTextModelSeries.NAME_OF_MODEL_SERIES, VolcesDeepSeekTextModelSeries.MODEL_NAME_OF_DEEPSEEK_V3_241226),

    //    DeepSeekChat(SupportedProvider.DeepSeek, DeepseekModel.ChatModel.getCode()),
    //    DeepSeekReasoner(SupportedProvider.DeepSeek, DeepseekModel.ReasonerModel.getCode()),

    ChatGPT4o(GPTVisionModelSeries.NAME_OF_MODEL_SERIES, GPTVisionModelSeries.MODEL_NAME_OF_GPT_4O),
    QwenVLPlus(QwenVisionModelSeries.NAME_OF_MODEL_SERIES, QwenVisionModelSeries.MODEL_NAME_OF_QWEN_VL_PLUS),
    QwenVLPlusLatest(QwenVisionModelSeries.NAME_OF_MODEL_SERIES, QwenVisionModelSeries.MODEL_NAME_OF_QWEN_VL_PLUS_LATEST),
    QwenVLMax(QwenVisionModelSeries.NAME_OF_MODEL_SERIES, QwenVisionModelSeries.MODEL_NAME_OF_QWEN_VL_MAX),
    QwenVLMaxLatest(QwenVisionModelSeries.NAME_OF_MODEL_SERIES, QwenVisionModelSeries.MODEL_NAME_OF_QWEN_VL_MAX_LATEST),
    Doubao1d5VisionPro32k250115(DoubaoVisionModelSeries.NAME_OF_MODEL_SERIES, DoubaoVisionModelSeries.MODEL_NAME_OF_DOUBAO_1d5_VISION_PRO_32K_250115),
    ;

    private final String seriesName;
    private final String chatModelName;

    SupportedModelEnum(String seriesName, String chatModelName) {
        this.seriesName = seriesName;
        this.chatModelName = chatModelName;
    }

    public ChatModel getChatModel() {
        return switch (seriesName) {
            case GPTTextModelSeries.NAME_OF_MODEL_SERIES -> new GPTTextModelSeries() {
                @Override
                public String getModelName() {
                    return chatModelName;
                }
            };
            case QwenTextModelSeries.NAME_OF_MODEL_SERIES -> new QwenTextModelSeries() {
                @Override
                public String getModelName() {
                    return chatModelName;
                }
            };
            case VolcesDeepSeekTextModelSeries.NAME_OF_MODEL_SERIES -> new VolcesDeepSeekTextModelSeries() {
                @Override
                public String getModelName() {
                    return chatModelName;
                }
            };
            case DoubaoTextModelSeries.NAME_OF_MODEL_SERIES -> new DoubaoTextModelSeries() {
                @Override
                public String getModelName() {
                    return chatModelName;
                }
            };
            case VolcesMoonshotTextModelSeries.NAME_OF_MODEL_SERIES -> new VolcesMoonshotTextModelSeries() {
                @Override
                public String getModelName() {
                    return chatModelName;
                }
            };
            case GPTVisionModelSeries.NAME_OF_MODEL_SERIES -> new GPTVisionModelSeries() {
                @Override
                public String getModelName() {
                    return chatModelName;
                }
            };
            case QwenVisionModelSeries.NAME_OF_MODEL_SERIES -> new QwenVisionModelSeries() {
                @Override
                public String getModelName() {
                    return chatModelName;
                }
            };
            case DoubaoVisionModelSeries.NAME_OF_MODEL_SERIES -> new DoubaoVisionModelSeries() {
                @Override
                public String getModelName() {
                    return chatModelName;
                }
            };

            default -> throw new IllegalStateException("Unexpected value: " + seriesName);
        };
    }
}
