package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.GPTKit;

public abstract class AbstractGptKitUnitTest extends AbstractGptServiceAdapterUnitTest {
    private GPTKit gptKit;

    protected GPTKit buildKit() {
        return new GPTKit();
    }

    @Override
    public void setUp() {
        super.setUp();
        gptKit = buildKit();
    }

    protected final GPTKit getKit() {
        return gptKit;
    }
}
