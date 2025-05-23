package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIServiceAdapter;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.GPTKit;
import io.github.sinri.AiOnHttpMix.test.unit.provider.core.AbstractModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.ServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.models.azure.openai.gpt.GPTChatModelSeries;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import org.junit.Assert;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public abstract class AbstractGptUnitTest extends AbstractModelUnitTest<GPTChatModelSeries> {

    private final GPTKit gptKit;

    public AbstractGptUnitTest() {
        super();
        gptKit = new GPTKit((OpenAIServiceAdapter) getServiceAdapter());
    }

    protected final GPTKit getKit() {
        return gptKit;
    }

    @Override
    protected GPTChatModelSeries buildModel() {
        return GPTChatModelSeries.model(GPTChatModelSeries.MODEL_NAME_OF_GPT_4O);
    }

    @Override
    protected ServiceAdapter buildServiceAdapter() {
        KeelConfigElement chatgptConfig = Keel.getConfiguration().extract("provider", "azure", "openai");
        Assert.assertNotNull(chatgptConfig);
        return getModel().buildServiceAdapter(chatgptConfig);
    }
}
