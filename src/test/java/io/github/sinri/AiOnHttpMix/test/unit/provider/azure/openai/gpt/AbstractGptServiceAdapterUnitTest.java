package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIServiceAdapter;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.GPTServiceAdapter;
import io.github.sinri.AiOnHttpMix.test.unit.provider.AbstractServiceAdapterUnitTest;
import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;
import io.github.sinri.AiOnHttpMix.utils.specification.GPTModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import org.junit.Assert;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public abstract class AbstractGptServiceAdapterUnitTest extends AbstractServiceAdapterUnitTest<GPTModelSpecification, GPTServiceAdapter> {
    @Override
    protected GPTModelSpecification getModelSeries() {
        return ModelSpecification.chatgpt;
    }

    @Override
    protected GPTServiceAdapter buildServiceAdapter() {
        KeelConfigElement chatgptConfig = Keel.getConfiguration().extract("provider", "azure", "openai");
        Assert.assertNotNull(chatgptConfig);
        return (GPTServiceAdapter) ServiceProvider.azureOpenAI.buildServiceAdapter(
                ModelSpecification.chatgpt,
                chatgptConfig
        );
    }
}
