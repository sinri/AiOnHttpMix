package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.doubao;

import io.github.sinri.AiOnHttpMix.provider.volces.doubao.DoubaoServiceAdapter;
import io.github.sinri.AiOnHttpMix.test.unit.provider.AbstractServiceAdapterUnitTest;
import io.github.sinri.AiOnHttpMix.utils.specification.DoubaoModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import org.junit.Assert;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class AbstractDoubaoServiceAdapterUnitTest extends AbstractServiceAdapterUnitTest<DoubaoModelSpecification, DoubaoServiceAdapter> {
    @Override
    protected DoubaoModelSpecification getModelSeries() {
        return ModelSpecification.doubao;
    }

    @Override
    protected DoubaoServiceAdapter buildServiceAdapter() {
        KeelConfigElement doubaoConfig = Keel.getConfiguration()
                                             .extract("provider", "volces", "doubao");
        Assert.assertNotNull(doubaoConfig);

        return (DoubaoServiceAdapter) getModelSeries().getServiceProvider()
                                                      .buildServiceAdapter(getModelSeries(), doubaoConfig);
    }
}
