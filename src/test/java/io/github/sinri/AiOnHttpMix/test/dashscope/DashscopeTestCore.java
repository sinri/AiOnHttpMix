package io.github.sinri.AiOnHttpMix.test.dashscope;

import io.github.sinri.AiOnHttpMix.dashscope.core.DashscopeServiceMeta;
import io.github.sinri.keel.tesuto.KeelTest;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class DashscopeTestCore extends KeelTest {
    private DashscopeServiceMeta serviceMeta;

    @Override
    protected @NotNull Future<Void> starting() {
        Keel.getConfiguration().loadPropertiesFile("config.properties");

        String dashscopeApiKey = Keel.config("dashscope.api_key");

        this.serviceMeta = new DashscopeServiceMeta(dashscopeApiKey);

        return Future.succeededFuture();
    }

    public DashscopeServiceMeta getServiceMeta() {
        return serviceMeta;
    }
}
