package io.github.sinri.AiOnHttpMix.test.azure.bing;

import io.github.sinri.AiOnHttpMix.azure.bing.search.BingSearchKit;
import io.github.sinri.AiOnHttpMix.azure.bing.search.v7.BingSearchParameters;
import io.github.sinri.keel.tesuto.KeelTest;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class BingSearchV7Test extends KeelTest {
    private BingSearchKit bingSearchInFirstTower;

    @Override
    protected @NotNull Future<Void> starting() {
        return super.starting()
                .compose(v -> {
                    Keel.getConfiguration().loadPropertiesFile("config.properties");
                    String instanceCode = "BingSearchInFirstTower";
                    String subscriptionKey = Keel.config("azure.bing.search." + instanceCode + ".SubscriptionKey");
                    bingSearchInFirstTower = new BingSearchKit(subscriptionKey);
                    return Future.succeededFuture();
                });
    }

    @TestUnit
    public Future<Void> test1() {
        return bingSearchInFirstTower.callBingSearch(BingSearchParameters.create()
                        .setQ("日本东北地区的大城市")
                        .toJsonObject()
                )
                .compose(response -> {
//                    getLogger().info("resp " + jsonObject.getInteger("status_code"));
//                    getLogger().info("headers", jsonObject.getJsonObject("headers"));
//                    getLogger().info("body", new JsonObject(jsonObject.getString("raw_body")));
                    getLogger().info("resp", response.cloneAsJsonObject());
                    return Future.succeededFuture();
                });
    }
}
