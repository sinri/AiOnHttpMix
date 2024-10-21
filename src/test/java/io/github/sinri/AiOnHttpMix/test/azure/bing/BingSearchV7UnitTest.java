package io.github.sinri.AiOnHttpMix.test.azure.bing;

import io.github.sinri.AiOnHttpMix.azure.bing.search.BingSearchKit;
import io.github.sinri.AiOnHttpMix.azure.bing.search.v7.BingSearchParameters;
import io.github.sinri.AiOnHttpMix.test.BaseUnitTest;
import io.github.sinri.keel.facade.async.KeelAsyncKit;
import io.vertx.core.Future;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class BingSearchV7UnitTest extends BaseUnitTest {
    private BingSearchKit bingSearchInFirstTower;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        String instanceCode = "BingSearchInFirstTower";
        String subscriptionKey = Keel.config("azure.bing.search." + instanceCode + ".SubscriptionKey");
        bingSearchInFirstTower = new BingSearchKit(subscriptionKey);
    }

    @Test
    public void test() {
        try {
            KeelAsyncKit.pseudoAwait(promise -> {
                bingSearchInFirstTower.callBingSearch(
                                BingSearchParameters.create()
                                        .setQ("日本东北地区的大城市")
                                        .toJsonObject(),
                                UUID.randomUUID().toString()
                        )
                        .compose(response -> {
                            getLogger().info("resp", response.cloneAsJsonObject());
                            Assert.assertFalse(response.getWebPages().getValue().isEmpty());
                            return Future.succeededFuture();
                        })
                        .onComplete(promise);
            });
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}
