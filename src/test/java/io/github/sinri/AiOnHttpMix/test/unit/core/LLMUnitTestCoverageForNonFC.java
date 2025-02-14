package io.github.sinri.AiOnHttpMix.test.unit.core;

/**
 * @since 1.2.2
 */
public interface LLMUnitTestCoverageForNonFC<R> extends AnyKitUnitTestRequestMixin<R> {

    void testSyncWithoutToolCall();


    void testStreamWithoutToolCall();


    void testStreamBufferWithoutToolCall();

}
