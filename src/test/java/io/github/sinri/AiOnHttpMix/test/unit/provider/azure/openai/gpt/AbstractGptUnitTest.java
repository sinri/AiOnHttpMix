package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIServiceAdapter;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.GPTKit;
import io.github.sinri.AiOnHttpMix.test.unit.provider.AbstractModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.models.azure.openai.gpt.GPTModelSeries;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import org.junit.Assert;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public abstract class AbstractGptUnitTest extends AbstractModelUnitTest<GPTModelSeries> {
    private final GPTModelSeries gpt4o;

    private final GPTKit gptKit;

    public AbstractGptUnitTest() {
        super();
        gpt4o = GPTModelSeries.model(GPTModelSeries.MODEL_NAME_OF_GPT_4O);
        gptKit = new GPTKit();
    }

    protected final GPTKit getKit() {
        return gptKit;
    }

    @Override
    protected GPTModelSeries getModel() {
        return gpt4o;
    }

    @Override
    protected ChatModelServiceAdapter buildServiceAdapter() {
        KeelConfigElement chatgptConfig = Keel.getConfiguration().extract("provider", "azure", "openai");
        Assert.assertNotNull(chatgptConfig);
        return getModel().buildServiceAdapter(chatgptConfig);
    }

    @Override
    protected OpenAIServiceAdapter getServiceAdapter() {
        return (OpenAIServiceAdapter) super.getServiceAdapter();
    }
}
