package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.deepseek;

import io.github.sinri.AiOnHttpMix.provider.volces.VolcesServiceAdapter;
import io.github.sinri.AiOnHttpMix.test.unit.provider.AbstractServiceAdapterUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.volces.deepseek.VolcesDeepSeekModelSeries;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.VolcesModelSpecification;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import org.junit.Assert;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class AbstractVolcesDeepSeekServiceAdapterUnitTest extends AbstractServiceAdapterUnitTest<VolcesModelSpecification, VolcesServiceAdapter> {
    protected final VolcesDeepSeekModelSeries deepSeekV3;

    public AbstractVolcesDeepSeekServiceAdapterUnitTest() {
        super();
        deepSeekV3 = new VolcesDeepSeekModelSeries.Builder().build(VolcesDeepSeekModelSeries.MODEL_NAME_OF_DEEPSEEK_V3_241226);
    }

    @Override
    protected VolcesModelSpecification getModelSeries() {
        return ModelSpecification.volces;
    }

    @Override
    protected VolcesServiceAdapter buildServiceAdapter() {
        KeelConfigElement doubaoConfig = Keel.getConfiguration()
                                             .extract("provider", "volces", "doubao");
        Assert.assertNotNull(doubaoConfig);

        return (VolcesServiceAdapter) getModelSeries().getServiceProvider()
                                                      .buildServiceAdapter(getModelSeries(), doubaoConfig);
    }
}
