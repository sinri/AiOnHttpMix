package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai.o;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIServiceAdapter;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.GPTKit;
import io.github.sinri.AiOnHttpMix.test.unit.provider.core.AbstractModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.ServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.models.azure.openai.gpt.OTextModelSeries;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import org.junit.Assert;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public abstract class AbstractOModelUnitTest extends AbstractModelUnitTest<OTextModelSeries> {
    private final GPTKit gptKit;

    public AbstractOModelUnitTest() {
        super();
        gptKit = new GPTKit((OpenAIServiceAdapter) getServiceAdapter());
    }

    public GPTKit getKit() {
        return gptKit;
    }

    @Override
    protected OTextModelSeries buildModel() {
        return OTextModelSeries.model(OTextModelSeries.MODEL_NAME_OF_O1);
    }

    @Override
    protected ServiceAdapter buildServiceAdapter() {
        KeelConfigElement chatgptConfig = Keel.getConfiguration().extract("provider", "azure", "openai");
        Assert.assertNotNull(chatgptConfig);
        return getModel().buildServiceAdapter(chatgptConfig);
    }
}
