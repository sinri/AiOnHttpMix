package io.github.sinri.AiOnHttpMix.mirage.vl;

import io.github.sinri.AiOnHttpMix.utils.SupportedVLModel;

/**
 * @since 1.3.0
 */
public enum MirageVLServiceEnum {
    DoubaoVisionLite32k("doubao-vision-lite-32k", SupportedVLModel.DoubaoVL),
    Doubao1dot5VisionPro32k("doubao-1.5-vision-pro-32k", SupportedVLModel.DoubaoVL),
    QwenVLPlus(null, SupportedVLModel.QwenVLPlus),
    QwenVLMax(null, SupportedVLModel.QwenVLMax),
    ;
    private final String serviceCode;
    private final SupportedVLModel mappedSupportedVLModel;

    MirageVLServiceEnum(String serviceCode, SupportedVLModel mappedSupportedVLModel) {
        this.serviceCode = serviceCode;
        this.mappedSupportedVLModel = mappedSupportedVLModel;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public SupportedVLModel getMappedSupportedVLModel() {
        return mappedSupportedVLModel;
    }
}
