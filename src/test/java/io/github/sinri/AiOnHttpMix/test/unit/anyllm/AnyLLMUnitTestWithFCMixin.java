package io.github.sinri.AiOnHttpMix.test.unit.anyllm;

import io.github.sinri.AiOnHttpMix.mix.AnyLLMRequest;

public interface AnyLLMUnitTestWithFCMixin extends AnyLLMUnitTestCommonMixin {
    AnyLLMRequest generateRequestWithToolCall();

    void testSyncWithToolCall();

    void testStreamBufferWithToolCall();
}
