package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.doubao;

import io.github.sinri.AiOnHttpMix.provider.volces.VolcesKit;
import io.github.sinri.AiOnHttpMix.provider.volces.VolcesServiceAdapter;
import io.github.sinri.AiOnHttpMix.test.unit.provider.volces.doubao.thinking.AbstractDoubaoThinkingModelUnitTest;

public class AbstractDoubaoKitUnitTest extends AbstractDoubaoThinkingModelUnitTest {
    private final VolcesKit kit;

    public AbstractDoubaoKitUnitTest() {
        super();
        kit = buildKit();
    }

    private VolcesKit buildKit() {
        return new VolcesKit((VolcesServiceAdapter) getServiceAdapter());
    }

    protected final VolcesKit getKit() {
        return kit;
    }
}
