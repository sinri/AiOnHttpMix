package io.github.sinri.AiOnHttpMix.dashscope.qwen;

/**
 * 通义千问VL是具有视觉（图像）理解能力的文本生成模型，不仅能进行OCR（图片文字识别），还能进一步总结和推理，例如从商品照片中提取属性，根据习题图进行解题等。
 */
public enum QwenVLModel {
    /**
     * 稳定版：大幅提升细节识别和文字识别能力，支持超百万像素分辨率和任意宽高比的图像。在广泛的视觉任务中提供卓越性能。
     */
    QWEN_VL_PLUS("qwen-vl-plus"),
    /**
     * 最新版：始终等同最新快照版。
     *
     * @since 1.2.2
     */
    QWEN_VL_PLUS_LATEST("qwen-vl-plus-latest"),
    /**
     * 稳定版： 相比qwen-vl-plus再次提升视觉推理和指令遵循能力，在更多复杂任务中提供最佳性能。 当前等同qwen-vl-max-2024-11-19。
     */
    QWEN_VL_MAX("qwen-vl-max"),
    /**
     * 最新版：始终等同最新快照版。
     *
     * @since 1.2.2
     */
    QWEN_VL_MAX_LATEST("qwen-vl-max-latest"),
    ;

    private final String modelCode;

    QwenVLModel(String modelCode) {
        this.modelCode = modelCode;
    }

    public static QwenVLModel fromModelCode(String modelCode) {
        for (QwenVLModel vlModel : QwenVLModel.values()) {
            if (vlModel.modelCode.equals(modelCode)) {
                return vlModel;
            }
        }
        throw new IllegalArgumentException("Unknown modelCode: " + modelCode);
    }

    public String getModelCode() {
        return modelCode;
    }
}
