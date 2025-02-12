package io.github.sinri.AiOnHttpMix.test.unit;

/**
 * @since 1.2.2
 */
public interface LLMUnitTestCoverageForNonFC<R> extends AnyKitUnitTestRequestMixin<R> {

    void testSyncWithoutToolCall();


    void testStreamWithoutToolCall();


    void testStreamBufferWithoutToolCall();

}
