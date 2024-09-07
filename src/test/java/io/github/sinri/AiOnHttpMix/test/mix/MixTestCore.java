package io.github.sinri.AiOnHttpMix.test.mix;

import io.github.sinri.keel.logger.KeelLogLevel;
import io.github.sinri.keel.tesuto.KeelTest;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class MixTestCore extends KeelTest {
    @Override
    protected @NotNull Future<Void> starting() {
        return super.starting()
                .compose(v -> {
                    Keel.getConfiguration().loadPropertiesFile("config.properties");
                    getLogger().setVisibleLevel(KeelLogLevel.DEBUG);
                    return Future.succeededFuture();
                });
    }
}
