package io.github.sinri.AiOnHttpMix.test.unit;

/**
 * @since 1.2.2
 */
public interface LLMUnitTestCoverageForFC<R> extends AnyKitUnitTestRequestMixin<R> {


    void testSyncWithToolCall();


    void testStreamWithToolCall();


    void testStreamBufferWithToolCall();
}
