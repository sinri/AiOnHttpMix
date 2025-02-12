package io.github.sinri.AiOnHttpMix.dashscope.qwen;

/**
 * 文本生成-通义千问
 *
 * @see <a href="https://help.aliyun.com/zh/dashscope/developer-reference/model-introduction">模型概览</a>
 */
public enum QwenModel {
    /**
     * 通义千问系列效果最好的模型，适合复杂、多步骤的任务。 稳定版. 当前等同qwen-max-2024-09-19.
     */
    QWEN_MAX("qwen-max"),
    /**
     * 通义千问系列效果最好的模型，适合复杂、多步骤的任务。 最新版. 始终等同最新快照版.
     * <p>
     * e.g. 最新的qwen-max-0125模型：通义千问系列效果最好的模型，代码编写与理解能力、逻辑能力、多语言能力显著提升，回复风格面向人类偏好进行大幅调整，模型回复详实程度和格式清晰度明显改善，内容创作、JSON *
     * 格式遵循、角色扮演能力定向提升。
     * </p>
     *
     * @since 1.2.2
     */
    QWEN_MAX_LATEST("qwen-max-latest"),
    /**
     * 能力均衡，推理效果、成本和速度介于通义千问-Max和通义千问-Turbo之间，适合中等复杂任务。 稳定版. 当前等同qwen-plus-2024-11-25.
     */
    QWEN_PLUS("qwen-plus"),
    /**
     * 能力均衡，推理效果、成本和速度介于通义千问-Max和通义千问-Turbo之间，适合中等复杂任务。 始终等同最新快照版.
     *
     * @since 1.2.2
     */
    QWEN_PLUS_LATEST("qwen-plus-latest"),
    /**
     * 通义千问系列速度最快、成本极低的模型，适合简单任务。 稳定版. 当前等同qwen-turbo-2024-11-01.
     */
    QWEN_TURBO("qwen-turbo"),
    /**
     * 通义千问系列速度最快、成本极低的模型，适合简单任务。 最新版. 始终等同最新快照版.
     *
     * @since 1.2.2
     */
    QWEN_TURBO_LATEST("qwen-turbo-latest"),
    /**
     * 通义千问系列上下文窗口最长，能力均衡且成本较低的模型，适合长文本分析、信息抽取、总结摘要和分类打标等任务。
     */
    QWEN_LONG("qwen-long"),
    ;
    private final String modelCode;

    QwenModel(String modelCode) {
        this.modelCode = modelCode;
    }

    public static QwenModel fromModelCode(String modelCode) {
        for (QwenModel qwenModel : QwenModel.values()) {
            if (qwenModel.modelCode.equals(modelCode)) {
                return qwenModel;
            }
        }
        throw new IllegalArgumentException("Unknown modelCode: " + modelCode);
    }

    public String getModelCode() {
        return modelCode;
    }

}
