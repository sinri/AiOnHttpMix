package io.github.sinri.AiOnHttpMix.test.unit;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.keel.facade.tesuto.unit.KeelUnitTest;

import java.util.UUID;

/**
 * @since 1.2.2
 */
public abstract class AnyUnitTest extends KeelUnitTest {
    public AnyUnitTest() {
        super();
    }

    @Override
    public void setUp() {
        // do you need verbose logging?
        AigcMix.disableVerboseLogger();
    }

    protected final String generateRequestId() {
        return UUID.randomUUID().toString();
    }
}
