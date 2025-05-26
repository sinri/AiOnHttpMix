package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.doubao.thinking;

import io.github.sinri.AiOnHttpMix.test.unit.provider.volces.AbstractVolcesTextModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.volces.doubao.DoubaoTextModelSeries;

public class AbstractDoubaoThinkingModelUnitTest extends AbstractVolcesTextModelUnitTest<DoubaoTextModelSeries> {

    public AbstractDoubaoThinkingModelUnitTest() {
        super();
    }

    @Override
    protected DoubaoTextModelSeries buildModel() {
        return DoubaoTextModelSeries.model(DoubaoTextModelSeries.MODEL_NAME_OF_DOUBAO_1D5_THINKING_PRO_250415);
    }
}
