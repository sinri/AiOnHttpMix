package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.bing;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.azure.bing.search.BingSearchKit;
import io.github.sinri.AiOnHttpMix.azure.bing.search.v7.BingSearchParameters;
import io.github.sinri.AiOnHttpMix.azure.bing.search.v7.BingSearchResponse;
import io.github.sinri.AiOnHttpMix.test.unit.core.AnyUnitTest;
import io.github.sinri.AiOnHttpMix.test.unit.core.TestPassed;
import io.vertx.core.Future;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

/**
 * @since 1.2.2
 */
public class BingSearchV7UnitTest extends AnyUnitTest {
    private BingSearchKit bingSearchInFirstTower;

    @Before
    public void setUp() {
        super.setUp();

        String instanceCode = "BingSearchInFirstTower";
        String subscriptionKey = Keel.config("azure.bing.search." + instanceCode + ".SubscriptionKey");
        bingSearchInFirstTower = new BingSearchKit(subscriptionKey);
    }

    /**
     * 测试Bing搜索引擎API的调用。
     */
    @Test
    @TestPassed(time = "2025-02-13")
    public void testCallBingSearch() {
        this.async(() -> bingSearchInFirstTower
                .callBingSearch(
                        BingSearchParameters.create()
                                            .setQ("日本东北地区的超过1千万人口的大城市")
                                            .toJsonObject(),
                        UUID.randomUUID().toString()
                )
                .compose(response -> {
                    AigcMix.getVerboseLogger().debug("resp", response.cloneAsJsonObject());

                    BingSearchResponse.WebAnswer webPages = response.getWebPages();
                    String webSearchUrl = webPages.getWebSearchUrl();
                    getUnitTestLogger().info(x -> x.message("webSearchUrl: " + webSearchUrl));
                    Long totalEstimatedMatches = webPages.getTotalEstimatedMatches();
                    getUnitTestLogger().info("totalEstimatedMatches: " + totalEstimatedMatches);
                    List<BingSearchResponse.WebPage> items = webPages.getValue();
                    for (BingSearchResponse.WebPage item : items) {
                        getUnitTestLogger().info("Search Result", item.cloneAsJsonObject());
                    }
                    Assert.assertFalse(response.getWebPages().getValue().isEmpty());
                    return Future.succeededFuture();
                }));
    }
}
