package io.github.sinri.AiOnHttpMix.test.unit.anyllm;

import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMRequest;

public interface AnyLLMUnitTestWithoutFCMixin extends AnyLLMUnitTestCommonMixin {
    AnyLLMRequest generateRequestWithoutToolCall();

    void testSyncWithoutToolCall();

    void testStreamBufferWithoutToolCall();
}
