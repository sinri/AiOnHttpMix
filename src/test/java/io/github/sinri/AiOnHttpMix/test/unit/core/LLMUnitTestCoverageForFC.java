package io.github.sinri.AiOnHttpMix.test.unit.core;

/**
 * @since 1.2.2
 */
public interface LLMUnitTestCoverageForFC<R> extends AnyKitUnitTestRequestMixin<R> {


    void testSyncWithToolCall();


    void testStreamWithToolCall();


    void testStreamBufferWithToolCall();
}
