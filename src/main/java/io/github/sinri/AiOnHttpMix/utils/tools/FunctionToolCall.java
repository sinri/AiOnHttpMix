package io.github.sinri.AiOnHttpMix.utils.tools;

/**
 * 要求以函数形式调用的工具调用的发起请求中，定义的函数调用要求。
 */
public interface FunctionToolCall {
    /**
     * 调用函数的名称
     */
    String getName();

    /**
     * 需要输入到函数中的参数，为JSON字符串。
     * 由于大模型响应有一定随机性，输出的JSON字符串并不总满足于您的函数，建议您在将参数输入函数前进行参数的有效性校验。
     */
    String getArguments();
}
