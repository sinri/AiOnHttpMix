package io.github.sinri.AiOnHttpMix.test.unit.core;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.keel.facade.tesuto.unit.KeelUnitTest;
import io.vertx.core.VertxOptions;
import io.vertx.core.dns.AddressResolverOptions;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * @since 1.2.2
 */
public abstract class AnyUnitTest extends KeelUnitTest {
    public AnyUnitTest() {
        super();
    }

    @Override
    protected @Nullable VertxOptions buildVertxOptions() {
        return new VertxOptions()
                .setAddressResolverOptions(new AddressResolverOptions()
                        .addServer("223.5.5.5")
                );
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
