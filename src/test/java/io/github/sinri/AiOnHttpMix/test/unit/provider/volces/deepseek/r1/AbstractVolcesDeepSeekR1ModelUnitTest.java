package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.deepseek.r1;

import io.github.sinri.AiOnHttpMix.test.unit.provider.volces.AbstractVolcesModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.volces.deepseek.VolcesDeepSeekChatModelSeries;

public class AbstractVolcesDeepSeekR1ModelUnitTest extends AbstractVolcesModelUnitTest<VolcesDeepSeekChatModelSeries> {

    public AbstractVolcesDeepSeekR1ModelUnitTest() {
        super();
    }

    @Override
    protected VolcesDeepSeekChatModelSeries buildModel() {
        return VolcesDeepSeekChatModelSeries.model(VolcesDeepSeekChatModelSeries.MODEL_NAME_OF_DEEPSEEK_R1_250120);
    }
}
