package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai.o;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIServiceAdapter;
import io.github.sinri.AiOnHttpMix.test.unit.provider.AbstractModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.models.azure.openai.gpt.OModelSeries;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import org.junit.Assert;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public abstract class AbstractOModelUnitTest extends AbstractModelUnitTest<OModelSeries> {
    private final OModelSeries o1;

    public AbstractOModelUnitTest() {
        super();
        o1 = OModelSeries.model(OModelSeries.MODEL_NAME_OF_O1);
    }

    @Override
    protected OModelSeries getModel() {
        return o1;
    }

    @Override
    protected ChatModelServiceAdapter buildServiceAdapter() {
        KeelConfigElement chatgptConfig = Keel.getConfiguration().extract("provider", "azure", "openai");
        Assert.assertNotNull(chatgptConfig);
        return o1.buildServiceAdapter(chatgptConfig);
    }

    @Override
    protected OpenAIServiceAdapter getServiceAdapter() {
        return (OpenAIServiceAdapter) super.getServiceAdapter();
    }
}
