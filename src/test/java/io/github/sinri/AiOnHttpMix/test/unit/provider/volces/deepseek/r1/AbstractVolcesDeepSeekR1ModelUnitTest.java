package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.deepseek.r1;

import io.github.sinri.AiOnHttpMix.test.unit.provider.volces.AbstractVolcesModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.volces.deepseek.VolcesDeepSeekModelSeries;

public class AbstractVolcesDeepSeekR1ModelUnitTest extends AbstractVolcesModelUnitTest<VolcesDeepSeekModelSeries> {
    private final VolcesDeepSeekModelSeries deepSeekR1;

    public AbstractVolcesDeepSeekR1ModelUnitTest() {
        super();
        deepSeekR1 = VolcesDeepSeekModelSeries.model(VolcesDeepSeekModelSeries.MODEL_NAME_OF_DEEPSEEK_R1_250120);
    }

    @Override
    protected VolcesDeepSeekModelSeries getModel() {
        return deepSeekR1;
    }
}
