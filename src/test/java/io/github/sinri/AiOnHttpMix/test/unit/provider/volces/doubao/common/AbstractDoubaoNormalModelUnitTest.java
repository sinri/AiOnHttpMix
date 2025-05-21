package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.doubao.common;

import io.github.sinri.AiOnHttpMix.test.unit.provider.volces.AbstractVolcesModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.volces.doubao.DoubaoModelSeries;

public class AbstractDoubaoNormalModelUnitTest extends AbstractVolcesModelUnitTest<DoubaoModelSeries> {
    private final DoubaoModelSeries doubaoPro32k;

    public AbstractDoubaoNormalModelUnitTest() {
        super();
        doubaoPro32k = DoubaoModelSeries.model(DoubaoModelSeries.MODEL_NAME_OF_DOUBAO_PRO_32K);
    }

    @Override
    protected DoubaoModelSeries getModel() {
        return doubaoPro32k;
    }

}
