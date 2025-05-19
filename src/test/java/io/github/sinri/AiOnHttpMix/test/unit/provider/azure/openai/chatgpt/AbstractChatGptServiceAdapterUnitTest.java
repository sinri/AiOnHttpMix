package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai.chatgpt;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt.ChatGPTServiceAdapter;
import io.github.sinri.AiOnHttpMix.test.unit.provider.AbstractServiceAdapterUnitTest;
import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;
import io.github.sinri.AiOnHttpMix.utils.specification.GPTModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import org.junit.Assert;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public abstract class AbstractChatGptServiceAdapterUnitTest extends AbstractServiceAdapterUnitTest<GPTModelSpecification, ChatGPTServiceAdapter> {
    @Override
    protected GPTModelSpecification getModelSeries() {
        return ModelSpecification.chatgpt;
    }

    @Override
    protected ChatGPTServiceAdapter buildServiceAdapter() {
        KeelConfigElement chatgptConfig = Keel.getConfiguration()
                                              .extract("provider", "azure", "openai", "EighthTower");
        Assert.assertNotNull(chatgptConfig);
        return (ChatGPTServiceAdapter) ServiceProvider.azureOpenAI.buildServiceAdapter(
                ModelSpecification.chatgpt,
                chatgptConfig
        );
    }
}
