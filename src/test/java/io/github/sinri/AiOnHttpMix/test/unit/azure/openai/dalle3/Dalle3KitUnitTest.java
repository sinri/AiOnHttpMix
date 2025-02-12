package io.github.sinri.AiOnHttpMix.test.unit.azure.openai.dalle3;

import io.github.sinri.AiOnHttpMix.azure.openai.dalle.v3.Dalle3Response;
import io.vertx.core.Future;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class Dalle3KitUnitTest extends AbstractDalle3KitUnitTest {

    @Override
    protected String getServiceName() {
        return "dalle3";
    }

    @Test
    public void test1() {
        this.async(() -> getDalle3Kit().draw(
                                               getServiceMeta(),
                                               p -> p.setPrompt("非机动车库充电桩昨晚炸了"),
                                               generateRequestId()
                                       )
                                       .compose(resp -> {
                                           Assert.assertNotNull(resp);
                                           List<Dalle3Response.Datum> data = resp.data();
                                           Assert.assertNotNull(data);
                                           data.forEach(datum -> {
                                               String revisedPrompt = datum.revisedPrompt();
                                               getUnitTestLogger().info("Revised Prompt: " + revisedPrompt);
                                               String url = datum.url();
                                               Assert.assertNotNull(url);
                                               getUnitTestLogger().info("URL of Image Generated: " + url);
                                           });
                                           return Future.succeededFuture();
                                       }));
    }
}
