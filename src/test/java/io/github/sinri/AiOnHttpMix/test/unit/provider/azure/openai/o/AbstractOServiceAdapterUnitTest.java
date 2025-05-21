package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai.o;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.o.OServiceAdapter;
import io.github.sinri.AiOnHttpMix.test.unit.provider.AbstractServiceAdapterUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.azure.openai.gpt.OModelSeries;
import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.OModelSpecification;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import org.junit.Assert;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public abstract class AbstractOServiceAdapterUnitTest extends AbstractServiceAdapterUnitTest<OModelSpecification, OServiceAdapter> {
    protected final OModelSeries o1;

    public AbstractOServiceAdapterUnitTest() {
        super();
        o1 = new OModelSeries.Builder().build(OModelSeries.MODEL_NAME_OF_O1);
    }

    @Override
    protected OModelSpecification getModelSeries() {
        return ModelSpecification.o;
    }

    @Override
    protected OServiceAdapter buildServiceAdapter() {
        KeelConfigElement chatgptConfig = Keel.getConfiguration().extract("provider", "azure", "openai");
        Assert.assertNotNull(chatgptConfig);
        return (OServiceAdapter) ServiceProvider.azureOpenAI.buildServiceAdapter(
                ModelSpecification.o,
                chatgptConfig
        );
    }
}
