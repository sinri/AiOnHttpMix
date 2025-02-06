package io.github.sinri.AiOnHttpMix.test;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.keel.logger.KeelLogLevel;
import io.github.sinri.keel.logger.event.KeelEventLogger;
import io.github.sinri.keel.logger.issue.center.KeelIssueRecordCenter;
import io.vertx.core.VertxOptions;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class BaseUnitTest {
    @Rule
    public TestName testName = new TestName();
    private KeelEventLogger logger;

    @Before
    public void setUp() throws Exception {
        Keel.initializeVertxStandalone(getVertxOptions());
        Keel.getConfiguration().loadPropertiesFile("config.properties");

        logger = KeelIssueRecordCenter.outputCenter().generateEventLogger(testName.getMethodName());

        getLogger().setVisibleLevel(KeelLogLevel.DEBUG);
        getLogger().debug("io.github.sinri.AiOnHttpMix.test.BaseUnitTest.setUp");

        AigcMix.enableVerboseLogger(getLogger());
    }

    protected VertxOptions getVertxOptions() {
        return new VertxOptions();
    }

    @After
    public void tearDown() throws Exception {
        getLogger().debug("io.github.sinri.AiOnHttpMix.test.BaseUnitTest.tearDown start");
        CompletableFuture<Objects> completableFuture = new CompletableFuture<>();
        Keel.close()
                .onSuccess(v -> completableFuture.complete(null))
                .onFailure(completableFuture::completeExceptionally);
        completableFuture.get();
        getLogger().debug("io.github.sinri.AiOnHttpMix.test.BaseUnitTest.tearDown end");
    }

    protected KeelEventLogger getLogger() {
        return logger;
    }
}
