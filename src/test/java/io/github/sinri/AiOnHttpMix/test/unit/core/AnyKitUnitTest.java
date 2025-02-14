package io.github.sinri.AiOnHttpMix.test.unit.core;

import io.github.sinri.AiOnHttpMix.utils.ServiceMeta;
import org.junit.Before;

public abstract class AnyKitUnitTest<S extends ServiceMeta, K> extends AnyUnitTest {
    private S serviceMeta;
    private K kit;

    abstract protected S generateServiceMeta();

    abstract protected K generateKit();

    final protected S getServiceMeta() {
        return serviceMeta;
    }

    final protected K getKit() {
        return kit;
    }

    @Before
    @Override
    public void setUp() {
        super.setUp();

        this.serviceMeta = generateServiceMeta();
        this.kit = generateKit();
    }
}
