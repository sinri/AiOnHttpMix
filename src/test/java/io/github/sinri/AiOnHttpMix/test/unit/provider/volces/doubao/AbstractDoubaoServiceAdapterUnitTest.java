package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.doubao;

import io.github.sinri.AiOnHttpMix.provider.volces.VolcesServiceAdapter;
import io.github.sinri.AiOnHttpMix.test.unit.provider.AbstractServiceAdapterUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.volces.doubao.DoubaoModelSeries;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.VolcesModelSpecification;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import org.junit.Assert;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class AbstractDoubaoServiceAdapterUnitTest extends AbstractServiceAdapterUnitTest<VolcesModelSpecification, VolcesServiceAdapter> {
    protected final DoubaoModelSeries doubaoPro32k;
    protected final DoubaoModelSeries doubaoThinking;

    public AbstractDoubaoServiceAdapterUnitTest() {
        super();
        doubaoPro32k = new DoubaoModelSeries.Builder().build(DoubaoModelSeries.MODEL_NAME_OF_DOUBAO_PRO_32K);
        doubaoThinking = new DoubaoModelSeries.Builder().build(DoubaoModelSeries.MODEL_NAME_OF_DOUBAO_1D5_THINKING_PRO_250415);
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
