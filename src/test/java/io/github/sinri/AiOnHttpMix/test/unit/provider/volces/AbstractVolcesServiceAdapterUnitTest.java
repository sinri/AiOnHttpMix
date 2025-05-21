package io.github.sinri.AiOnHttpMix.test.unit.provider.volces;

import io.github.sinri.AiOnHttpMix.provider.volces.VolcesServiceAdapter;
import io.github.sinri.AiOnHttpMix.test.unit.provider.AbstractServiceAdapterUnitTest;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.VolcesModelSpecification;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import org.junit.Assert;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public abstract class AbstractVolcesServiceAdapterUnitTest extends AbstractServiceAdapterUnitTest<VolcesModelSpecification, VolcesServiceAdapter> {
    @Override
    protected final VolcesModelSpecification getModelSeries() {
        return ModelSpecification.volces;
    }

    @Override
    protected final VolcesServiceAdapter buildServiceAdapter() {
        KeelConfigElement doubaoConfig = Keel.getConfiguration()
                                             .extract("provider", "volces");
        Assert.assertNotNull(doubaoConfig);

        return (VolcesServiceAdapter) getModelSeries().getServiceProvider()
                                                      .buildServiceAdapter(getModelSeries(), doubaoConfig);
    }
}
