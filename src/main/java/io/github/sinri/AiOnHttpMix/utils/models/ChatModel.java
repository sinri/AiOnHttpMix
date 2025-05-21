package io.github.sinri.AiOnHttpMix.utils.models;

import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;

/**
 * 本接口定义了在特定的大语言模型接口规格下的一系列或一种的可推理的大语言模型。
 * 最终实现本接口的非抽象类应该是一种具体的可推理的大语言模型。
 *
 * @since 2.0.0
 */
public interface ChatModel extends ModelSpecification {
    String getModelName();
}
