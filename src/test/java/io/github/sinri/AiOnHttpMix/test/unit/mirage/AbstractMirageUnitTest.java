package io.github.sinri.AiOnHttpMix.test.unit.mirage;

import io.github.sinri.AiOnHttpMix.mirage.MirageConfigElement;
import io.github.sinri.AiOnHttpMix.mirage.MirageKit;
import io.github.sinri.keel.facade.tesuto.unit.KeelUnitTest;

import java.util.Objects;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public abstract class AbstractMirageUnitTest extends KeelUnitTest {
    private final MirageKit mirageKit;

    public AbstractMirageUnitTest() {
        super();

        var c = Keel.getConfiguration().extract("mirage");
        Objects.requireNonNull(c);
        MirageConfigElement mirageConfigElement = new MirageConfigElement(c);

        mirageKit = new MirageKit(mirageConfigElement);
    }

    protected final MirageKit getMirageKit() {
        return mirageKit;
    }
}
