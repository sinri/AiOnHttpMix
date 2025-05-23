package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.doubao.thinking;

import io.github.sinri.AiOnHttpMix.test.unit.provider.volces.AbstractVolcesModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.volces.doubao.DoubaoChatModelSeries;

public class AbstractDoubaoThinkingModelUnitTest extends AbstractVolcesModelUnitTest<DoubaoChatModelSeries> {

    public AbstractDoubaoThinkingModelUnitTest() {
        super();
    }

    @Override
    protected DoubaoChatModelSeries buildModel() {
        return DoubaoChatModelSeries.model(DoubaoChatModelSeries.MODEL_NAME_OF_DOUBAO_1D5_THINKING_PRO_250415);
    }
}
