package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.doubao;

import io.github.sinri.AiOnHttpMix.provider.volces.VolcesServiceAdapter;
import io.github.sinri.AiOnHttpMix.test.unit.provider.AbstractServiceAdapterUnitTest;
import io.github.sinri.AiOnHttpMix.test.unit.provider.volces.AbstractVolcesServiceAdapterUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.volces.doubao.DoubaoModelSeries;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.VolcesModelSpecification;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import org.junit.Assert;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class AbstractDoubaoServiceAdapterUnitTest extends AbstractVolcesServiceAdapterUnitTest {
    protected final DoubaoModelSeries doubaoPro32k;
    protected final DoubaoModelSeries doubaoThinking;

    public AbstractDoubaoServiceAdapterUnitTest() {
        super();
        doubaoPro32k = new DoubaoModelSeries.Builder().build(DoubaoModelSeries.MODEL_NAME_OF_DOUBAO_PRO_32K);
        doubaoThinking = new DoubaoModelSeries.Builder().build(DoubaoModelSeries.MODEL_NAME_OF_DOUBAO_1D5_THINKING_PRO_250415);
    }
}
