package io.github.sinri.AiOnHttpMix.test.azure.dalle3;

import io.github.sinri.AiOnHttpMix.azure.openai.dalle.Dalle3Kit;
import io.github.sinri.AiOnHttpMix.azure.openai.dalle.v3.Dalle3Parameters;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;

import java.util.UUID;

public class AzureDalle3Test1 extends AzureDalle3TestCore {
    @TestUnit
    public Future<Void> test1() {
        Dalle3Kit dalle3Kit = new Dalle3Kit();
        String requestId = UUID.randomUUID().toString();
        return dalle3Kit.draw(
                        getServiceMeta(),
                        new Dalle3Parameters("夏日雨后池塘边，蜻蜓立于石栏杆上"),
                        requestId
                )
                .compose(resp -> {
                    resp.data().forEach(datum -> {
                        getLogger().info("DATUM", datum.cloneAsJsonObject());
                    });
                    return Future.succeededFuture();
                });
    }
}
