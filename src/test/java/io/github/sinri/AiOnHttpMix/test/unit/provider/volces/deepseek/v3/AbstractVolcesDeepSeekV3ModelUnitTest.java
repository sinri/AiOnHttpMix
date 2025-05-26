package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.deepseek.v3;

import io.github.sinri.AiOnHttpMix.test.unit.provider.volces.AbstractVolcesTextModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.volces.deepseek.VolcesDeepSeekTextModelSeries;

public abstract class AbstractVolcesDeepSeekV3ModelUnitTest extends AbstractVolcesTextModelUnitTest<VolcesDeepSeekTextModelSeries> {

    public AbstractVolcesDeepSeekV3ModelUnitTest() {
        super();
    }


    @Override
    protected VolcesDeepSeekTextModelSeries buildModel() {
        return VolcesDeepSeekTextModelSeries.model(VolcesDeepSeekTextModelSeries.MODEL_NAME_OF_DEEPSEEK_V3_241226);
    }
}
