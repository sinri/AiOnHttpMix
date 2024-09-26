package io.github.sinri.AiOnHttpMix.test.mirage;

import io.github.sinri.AiOnHttpMix.mirage.MirageSDK;
import io.github.sinri.keel.logger.KeelLogLevel;
import io.github.sinri.keel.tesuto.KeelTest;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class MirageTestBase extends KeelTest {
    private MirageSDK mirageSDK;

    public MirageSDK getMirageSDK() {
        return mirageSDK;
    }

    @Override
    protected @NotNull Future<Void> starting() {

        return super.starting()
                .compose(v -> {
                    Keel.getConfiguration().loadPropertiesFile("config.properties");
                    getLogger().setVisibleLevel(KeelLogLevel.DEBUG);

                    var domain = Keel.config("mirage.domain");
                    var clientCode = Keel.config("mirage.client_code");
                    var clientSecret = Keel.config("mirage.client_secret");

                    mirageSDK = new MirageSDK(domain, clientCode, clientSecret);

                    return Future.succeededFuture();
                });
    }
}
