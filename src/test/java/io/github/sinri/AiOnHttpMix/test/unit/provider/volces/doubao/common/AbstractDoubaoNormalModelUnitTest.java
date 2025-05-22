package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.doubao.common;

import io.github.sinri.AiOnHttpMix.test.unit.provider.volces.AbstractVolcesModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.volces.doubao.DoubaoChatModelSeries;

public class AbstractDoubaoNormalModelUnitTest extends AbstractVolcesModelUnitTest<DoubaoChatModelSeries> {
    private final DoubaoChatModelSeries doubaoPro32k;

    public AbstractDoubaoNormalModelUnitTest() {
        super();
        doubaoPro32k = DoubaoChatModelSeries.model(DoubaoChatModelSeries.MODEL_NAME_OF_DOUBAO_PRO_32K);
    }

    @Override
    protected DoubaoChatModelSeries getModel() {
        return doubaoPro32k;
    }

}
