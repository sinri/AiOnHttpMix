package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.GPTServiceAdapter;
import io.github.sinri.AiOnHttpMix.test.unit.provider.AbstractServiceAdapterUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.azure.openai.gpt.GPTModelSeries;
import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;
import io.github.sinri.AiOnHttpMix.utils.specification.GPTModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import org.junit.Assert;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public abstract class AbstractGptServiceAdapterUnitTest extends AbstractServiceAdapterUnitTest<GPTModelSpecification, GPTServiceAdapter> {
    protected final GPTModelSeries gpt4o;

    public AbstractGptServiceAdapterUnitTest() {
        super();
        gpt4o = new GPTModelSeries.Builder().build(GPTModelSeries.MODEL_NAME_OF_GPT_4O);
    }

    @Override
    protected GPTModelSpecification getModelSeries() {
        return ModelSpecification.gpt;
    }

    @Override
    protected GPTServiceAdapter buildServiceAdapter() {
        KeelConfigElement chatgptConfig = Keel.getConfiguration().extract("provider", "azure", "openai");
        Assert.assertNotNull(chatgptConfig);
        return (GPTServiceAdapter) ServiceProvider.azureOpenAI.buildServiceAdapter(
                ModelSpecification.gpt,
                chatgptConfig
        );
    }
}
