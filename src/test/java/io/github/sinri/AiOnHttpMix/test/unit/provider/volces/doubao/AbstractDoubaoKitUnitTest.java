package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.doubao;

import io.github.sinri.AiOnHttpMix.provider.volces.VolcesKit;
import org.junit.Before;

public class AbstractDoubaoKitUnitTest extends AbstractDoubaoServiceAdapterUnitTest {
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
