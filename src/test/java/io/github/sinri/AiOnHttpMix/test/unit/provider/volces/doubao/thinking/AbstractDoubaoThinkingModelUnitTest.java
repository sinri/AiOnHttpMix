package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.doubao.thinking;

import io.github.sinri.AiOnHttpMix.test.unit.provider.volces.AbstractVolcesModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.volces.doubao.DoubaoModelSeries;

public class AbstractDoubaoThinkingModelUnitTest extends AbstractVolcesModelUnitTest<DoubaoModelSeries> {
    protected final DoubaoModelSeries doubaoThinking;

    public AbstractDoubaoThinkingModelUnitTest() {
        super();
        doubaoThinking = DoubaoModelSeries.model(DoubaoModelSeries.MODEL_NAME_OF_DOUBAO_1D5_THINKING_PRO_250415);
    }

    @Override
    protected DoubaoModelSeries getModel() {
        return doubaoThinking;
    }
}
