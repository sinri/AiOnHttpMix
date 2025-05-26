package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.deepseek.r1;

import io.github.sinri.AiOnHttpMix.test.unit.provider.volces.AbstractVolcesTextModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.volces.deepseek.VolcesDeepSeekTextModelSeries;

public class AbstractVolcesDeepSeekR1ModelUnitTest extends AbstractVolcesTextModelUnitTest<VolcesDeepSeekTextModelSeries> {

    public AbstractVolcesDeepSeekR1ModelUnitTest() {
        super();
    }

    @Override
    protected VolcesDeepSeekTextModelSeries buildModel() {
        return VolcesDeepSeekTextModelSeries.model(VolcesDeepSeekTextModelSeries.MODEL_NAME_OF_DEEPSEEK_R1_250120);
    }
}
