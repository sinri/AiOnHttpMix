package io.github.sinri.AiOnHttpMix.utils.models;

import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;

/**
 * 本接口定义了在特定的大语言模型接口规格下的一系列或一种的可用于对话的基础大语言模型。
 * 作为所有聊天模型（包括纯文本模型和具备视觉理解能力的模型）的基础接口，
 * 其最终实现类应代表一种具体的可用于对话的大语言模型。
 * <p>
 * 继承本接口的子接口包括：
 * <ul>
 *   <li>{@link TextModel} —— 仅具备文本对话能力的大语言模型</li>
 *   <li>{@link VisionModel} —— 具备视觉理解能力的多模态大语言模型</li>
 * </ul>
 *
 * @since 2.0.0
 */
public interface ChatModel extends ModelSpecification {
    /**
     * @return 聊天模型的名称
     */
    String getModelName();
}
