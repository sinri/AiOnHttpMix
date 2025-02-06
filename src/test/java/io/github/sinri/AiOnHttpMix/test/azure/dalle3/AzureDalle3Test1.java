package io.github.sinri.AiOnHttpMix.test.azure.dalle3;

import io.github.sinri.AiOnHttpMix.azure.openai.dalle.Dalle3Kit;
import io.github.sinri.AiOnHttpMix.azure.openai.dalle.v3.Dalle3Response;
import io.vertx.core.Future;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class AzureDalle3Test1 extends AzureDalle3TestCore {
    @Test
    public void test1() {
        Dalle3Kit dalle3Kit = new Dalle3Kit();
        String requestId = UUID.randomUUID().toString();
        Keel.pseudoAwait(promise -> {
            dalle3Kit.draw(
                            getServiceMeta(),
                            p -> p.setPrompt("夏日雨后池塘边，蜻蜓立于石栏杆上"),
                            requestId
                    )
                    .compose(resp -> {
                        Assert.assertNotNull(resp);
                        List<Dalle3Response.Datum> data = resp.data();
                        Assert.assertNotNull(data);
                        data.forEach(datum -> {
                            getLogger().info("DATUM", datum.cloneAsJsonObject());
                            Assert.assertNotNull(datum.url());
                        });
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }
}
