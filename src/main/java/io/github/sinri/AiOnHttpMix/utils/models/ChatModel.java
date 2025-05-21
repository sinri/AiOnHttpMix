package io.github.sinri.AiOnHttpMix.utils.models;

import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;

/**
 * 本接口定义了一种具体的大语言模型，其规格和风格是确定的。
 * 这个接口的实现类均应在本package下并实现构造函数为package-protected，然后在这个接口里放置静态单例。
 *
 * @since 1.4.0
 */
public interface ChatModel {
    //    QwenPlusModel qwenPlus = new QwenPlusModel();
    //    QwenPlusLatestModel qwenPlusLatest = new QwenPlusLatestModel();
    //    OpenAIGPT4OModel chatgpt4o = new OpenAIGPT4OModel();
    //    OpenAIO1Model chatgptO1 = new OpenAIO1Model();
    //    DoubaoPro32kModel doubaoPro32k = new DoubaoPro32kModel();
    //    Doubao1dot5ThinkingPro250415Model doubao1dot5ThinkingPro250415 = new Doubao1dot5ThinkingPro250415Model();
    //    VolcesDeepSeekV3on241226 volcesDeepSeekV3on241226 = new VolcesDeepSeekV3on241226();

    static ChatModel factory(ModelSpecification specification, String modelName) {
        return new ChatModelImpl(specification, modelName);
    }

    String getModelName();

    ModelSpecification getSpecification();

    interface ChatModelBuilder<M> {
        M build(String modelName);
    }

}
