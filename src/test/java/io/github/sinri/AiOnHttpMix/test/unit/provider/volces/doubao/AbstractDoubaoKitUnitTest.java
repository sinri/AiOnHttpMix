package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.doubao;

import io.github.sinri.AiOnHttpMix.provider.volces.doubao.DoubaoKit;
import org.junit.Before;

public class AbstractDoubaoKitUnitTest extends AbstractDoubaoServiceAdapterUnitTest {
    private DoubaoKit kit;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        kit = buildKit();
    }

    private DoubaoKit buildKit() {
        return new DoubaoKit();
    }

    protected final DoubaoKit getKit() {
        return kit;
    }
}
