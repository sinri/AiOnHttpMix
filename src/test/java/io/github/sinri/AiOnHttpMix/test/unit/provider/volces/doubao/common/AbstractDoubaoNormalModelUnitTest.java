package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.doubao.common;

import io.github.sinri.AiOnHttpMix.test.unit.provider.volces.AbstractVolcesTextModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.volces.doubao.DoubaoTextModelSeries;

public class AbstractDoubaoNormalModelUnitTest extends AbstractVolcesTextModelUnitTest<DoubaoTextModelSeries> {

    public AbstractDoubaoNormalModelUnitTest() {
        super();
    }

    @Override
    protected DoubaoTextModelSeries buildModel() {
        return DoubaoTextModelSeries.model(DoubaoTextModelSeries.MODEL_NAME_OF_DOUBAO_PRO_32K);
    }
}
