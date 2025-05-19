package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai.chatgpt;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt.ChatGPTKit;

public abstract class AbstractChatGptKitUnitTest extends AbstractChatGptServiceAdapterUnitTest {
    private ChatGPTKit chatGptKit;

    protected ChatGPTKit buildKit() {
        return new ChatGPTKit();
    }

    @Override
    public void setUp() {
        super.setUp();
        chatGptKit = buildKit();
    }

    protected final ChatGPTKit getKit() {
        return chatGptKit;
    }
}
