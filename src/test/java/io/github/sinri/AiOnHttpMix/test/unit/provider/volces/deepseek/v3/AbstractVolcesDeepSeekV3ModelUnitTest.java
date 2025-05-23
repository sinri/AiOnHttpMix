package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.deepseek.v3;

import io.github.sinri.AiOnHttpMix.test.unit.provider.volces.AbstractVolcesModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.volces.deepseek.VolcesDeepSeekChatModelSeries;

public abstract class AbstractVolcesDeepSeekV3ModelUnitTest extends AbstractVolcesModelUnitTest<VolcesDeepSeekChatModelSeries> {

    public AbstractVolcesDeepSeekV3ModelUnitTest() {
        super();
    }


    @Override
    protected VolcesDeepSeekChatModelSeries buildModel() {
        return VolcesDeepSeekChatModelSeries.model(VolcesDeepSeekChatModelSeries.MODEL_NAME_OF_DEEPSEEK_V3_241226);
    }
}
