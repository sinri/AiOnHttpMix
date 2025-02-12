package io.github.sinri.AiOnHttpMix.test.unit.dashscope.deepseek;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.test.unit.LLMUnitTestCoverageForNonFC;

public interface DeepSeekOnDashScopeMixin extends LLMUnitTestCoverageForNonFC<QwenRequest> {

    String getDeepSeekModelNameInDashScope();

    @Override
    default QwenRequest generateRequest() {
        return QwenRequest.create()
                          .setModel(getDeepSeekModelNameInDashScope())
                          .handleInput(input -> input
                                  .addSystemMessage("你是个数学家")
                                  .addUserMessage("如何快速拟合出π的值？")
                          );
    }


}
