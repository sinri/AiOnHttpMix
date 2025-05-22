package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.deepseek.v3;

import io.github.sinri.AiOnHttpMix.test.unit.provider.volces.AbstractVolcesModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.volces.deepseek.VolcesDeepSeekChatModelSeries;

public abstract class AbstractVolcesDeepSeekV3ModelUnitTest extends AbstractVolcesModelUnitTest<VolcesDeepSeekChatModelSeries> {
    private final VolcesDeepSeekChatModelSeries deepSeekV3;
    //protected final VolcesDeepSeekModelSeries deepSeekR1;

    public AbstractVolcesDeepSeekV3ModelUnitTest() {
        super();
        deepSeekV3 = VolcesDeepSeekChatModelSeries.model(VolcesDeepSeekChatModelSeries.MODEL_NAME_OF_DEEPSEEK_V3_241226);
        //deepSeekR1 = new VolcesDeepSeekModelSeries.Builder().build(VolcesDeepSeekModelSeries.MODEL_NAME_OF_DEEPSEEK_R1_250120);
    }

    @Override
    protected VolcesDeepSeekChatModelSeries getModel() {
        return deepSeekV3;
    }


}
