package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.moonshot;

import io.github.sinri.AiOnHttpMix.provider.volces.VolcesKit;
import io.github.sinri.AiOnHttpMix.test.unit.provider.volces.doubao.AbstractDoubaoServiceAdapterUnitTest;
import org.junit.Before;

public class VolcesMoonshotKitUnitTest extends AbstractVolcesMoonshotServiceAdapterUnitTest {
    private VolcesKit kit;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        kit = buildKit();
    }

    private VolcesKit buildKit() {
        return new VolcesKit();
    }

    protected final VolcesKit getKit() {
        return kit;
    }
}
