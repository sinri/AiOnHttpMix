package io.github.sinri.AiOnHttpMix.utils.specification;

import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;

/**
 * 本接口定义了一种大语言模型系列，其往往采用统一的输入输出格式定义，可由不同规格或风格的大语言模型进行推理。
 * 这个接口的实现类均应在本package下并实现构造函数为package-protected，然后在这个接口里放置静态单例。
 *
 * @since 1.4.0
 */
public interface ModelSpecification {
    QwenModelSpecification qwen = new QwenModelSpecification();
    GPTModelSpecification chatgpt = new GPTModelSpecification();
    OModelSpecification o = new OModelSpecification();
    DoubaoModelSpecification doubao = new DoubaoModelSpecification();

    ServiceProvider getServiceProvider();

    String getSpecificationName();

    default void assertChatModelCompatible(ChatModel chatModel) {
        if (chatModel.getSpecification() != this) {
            throw new IllegalArgumentException("ChatModel is not compatible with Specification");
        }
    }

    // <S extends ModelSpecification> ChatModelServiceAdapter<S> buildServiceAdapter(KeelConfigElement config);
}
