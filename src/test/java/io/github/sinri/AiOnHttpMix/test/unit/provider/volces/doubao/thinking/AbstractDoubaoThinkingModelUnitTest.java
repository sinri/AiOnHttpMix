package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.doubao.thinking;

import io.github.sinri.AiOnHttpMix.test.unit.provider.volces.AbstractVolcesModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.volces.doubao.DoubaoChatModelSeries;

public class AbstractDoubaoThinkingModelUnitTest extends AbstractVolcesModelUnitTest<DoubaoChatModelSeries> {
    protected final DoubaoChatModelSeries doubaoThinking;

    public AbstractDoubaoThinkingModelUnitTest() {
        super();
        doubaoThinking = DoubaoChatModelSeries.model(DoubaoChatModelSeries.MODEL_NAME_OF_DOUBAO_1D5_THINKING_PRO_250415);
    }

    @Override
    protected DoubaoChatModelSeries getModel() {
        return doubaoThinking;
    }
}
