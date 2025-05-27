package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.bing;

import io.github.sinri.AiOnHttpMix.provider.azure.bing.search.BingSearchKit;
import io.github.sinri.AiOnHttpMix.provider.azure.bing.search.v7.BingSearchParameters;
import io.github.sinri.AiOnHttpMix.provider.azure.bing.search.v7.BingSearchResponse;
import io.github.sinri.keel.facade.tesuto.unit.KeelUnitTest;
import io.vertx.core.Future;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class BingSearchKitUnitTest extends KeelUnitTest {
    @Test
    public void test1() {
        async(() -> {
            String subscriptionKey = Keel.config("azure.bing.search.BingSearchInFirstTower.SubscriptionKey");
            BingSearchKit bingSearchKit = new BingSearchKit(subscriptionKey);
            return bingSearchKit.callBingSearch(
                                        BingSearchParameters.create("Azure Bing 关停"),
                                        UUID.randomUUID().toString()
                                )
                                .compose(resp -> {
                                    getUnitTestLogger().info("resp", resp.cloneAsJsonObject());

                                    Long totalEstimatedMatches = resp.getWebPages().getTotalEstimatedMatches();
                                    getUnitTestLogger().info("total: " + totalEstimatedMatches);
                                    List<BingSearchResponse.WebPage> webPages = resp.getWebPages().getValue();
                                    webPages.forEach(webPage -> {
                                        getUnitTestLogger().info("web page: " + webPage.getName() + " -> " + webPage.getUrl());
                                    });
                                    return Future.succeededFuture();
                                });
        });
    }
}