package io.github.sinri.AiOnHttpMix.test.volces;

import io.github.sinri.AiOnHttpMix.volces.core.VolcesServiceMeta;
import io.github.sinri.keel.tesuto.KeelTest;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class VolcesTestCore extends KeelTest {
    private VolcesServiceMeta serviceMeta;

    public VolcesServiceMeta getServiceMeta() {
        return serviceMeta;
    }

    @Override
    protected @NotNull Future<Void> starting() {
        Keel.getConfiguration().loadPropertiesFile("config.properties");

        String apiKey = Keel.config("volces.doubao-pro-128k.apiKey");
        String model = Keel.config("volces.doubao-pro-128k.model");

        serviceMeta=new VolcesServiceMeta(apiKey, model);
        return Future.succeededFuture();
    }
}
