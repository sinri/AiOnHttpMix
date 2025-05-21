package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.deepseek;

import io.github.sinri.AiOnHttpMix.provider.volces.VolcesKit;
import org.junit.Before;

public class AbstractVolcesDeepSeekKitUnitTest extends AbstractVolcesDeepSeekServiceAdapterUnitTest {
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
