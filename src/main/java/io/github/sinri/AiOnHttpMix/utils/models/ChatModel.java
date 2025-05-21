package io.github.sinri.AiOnHttpMix.utils.models;

import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;

/**
 * 本接口定义了一种具体的大语言模型，其规格和风格是确定的。
 * 这个接口的实现类均应在本package下并实现构造函数为package-protected，然后在这个接口里放置静态单例。
 *
 * @since 1.4.0
 */
public interface ChatModel extends ModelSpecification {
    String getModelName();

    interface ChatModelBuilder<M> {
        M build(String modelName);
    }
}
