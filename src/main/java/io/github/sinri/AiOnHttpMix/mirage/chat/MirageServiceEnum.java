package io.github.sinri.AiOnHttpMix.mirage.chat;

import io.github.sinri.AiOnHttpMix.utils.SupportedModel;

/**
 * 除了阿里系的Dashscope之外，都有Mirage Service定义。
 *
 * @since 1.2.6
 */
public enum MirageServiceEnum {
    ChatGPTo1("o1", SupportedModel.ChatGPT),
    ChatGPTo1Mini("o1-mini", SupportedModel.ChatGPT),
    ChatGPTo3Mini("o3-mini", SupportedModel.ChatGPT),
    ChatGPT4o("gpt-4-o", SupportedModel.ChatGPT),
    ChatGPT4oMini("gpt-4o-mini", SupportedModel.ChatGPT),
    ChatGPT4Turbo("gpt-4-turbo", SupportedModel.ChatGPT),
    DoubaoLite128k("doubao-lite-128k", SupportedModel.Doubao),
    DoubaoPro32k("doubao-pro-32k", SupportedModel.Doubao),
    DoubaoPro128k("doubao-pro-128k", SupportedModel.Doubao),
    Doubao1dot5Lite32k("doubao-1.5-lite-32k", SupportedModel.Doubao),
    Doubao1dot5Pro256k("doubao-1.5-pro-256k", SupportedModel.Doubao),
    VolcesMoonshotFirst128k("moonshot-v1-128k", SupportedModel.KimiOnVolces),
    VolcesDeepSeekV3("DeepSeek-V3", SupportedModel.DeepSeekChatOnVolces),
    VolcesDeepSeekV3P0324("DeepSeek-V3-0324", SupportedModel.DeepSeekChatOnVolces),
    VolcesDeepSeekR1("DeepSeek-R1", SupportedModel.DeepSeekReasonerOnVolces),
    QwenPlus(null, SupportedModel.QwenPlus),
    QwenMax(null, SupportedModel.QwenMax),
    QwenLong(null, SupportedModel.QwenLong),
    DashscopeDeepSeekV3(null, SupportedModel.DeepSeekChatOnDashScope),
    DashscopeDeepSeekR1(null, SupportedModel.DeepSeekReasonerOnDashScope),
    ;

    private final String serviceCode;
    private final SupportedModel mappedSupportedModel;


    MirageServiceEnum(String serviceCode, SupportedModel mappedSupportedModel) {
        this.serviceCode = serviceCode;
        this.mappedSupportedModel = mappedSupportedModel;
    }


    public String getServiceCode() {
        return serviceCode;
    }

    public SupportedModel getMappedSupportedModel() {
        return mappedSupportedModel;
    }
}
