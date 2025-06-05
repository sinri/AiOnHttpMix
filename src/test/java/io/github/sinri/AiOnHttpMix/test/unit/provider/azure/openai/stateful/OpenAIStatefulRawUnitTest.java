package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai.stateful;

import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import io.github.sinri.keel.facade.tesuto.unit.KeelUnitTest;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class OpenAIStatefulRawUnitTest extends KeelUnitTest {

    @Test
    public void test1() {
        async(() -> {
            KeelConfigElement chatgptConfig = Keel.getConfiguration().extract("provider", "azure", "openai");
            Assert.assertNotNull(chatgptConfig);

            var apiKey = chatgptConfig.readString(List.of("gpt-4dot1", "apiKey"));
            var resourceName = chatgptConfig.readString(List.of("gpt-4dot1", "resourceName"));
            var deployment = chatgptConfig.readString(List.of("gpt-4dot1", "deployment"));
            var apiVersion = chatgptConfig.readString(List.of("gpt-4dot1", "apiVersion"));


            String url = "https://" + resourceName + ".openai.azure.com/openai/v1/responses?api-version=preview";
            JsonObject body = new JsonObject()
                    .put("model", deployment)
                    .put("input", "介绍一下筑波大学的校徽");
            return Keel.useWebClient(webClient -> {
                           return webClient.postAbs(url)
                                           .putHeader("Content-Type", "application/json")
                                           .bearerTokenAuthentication(apiKey)
                                           .sendJsonObject(body);
                       })
                       .compose(bufferHttpResponse -> {
                           var output = bufferHttpResponse.bodyAsString();
                           getUnitTestLogger().info("output:\n" + output);
                           return Future.succeededFuture();
                       });
        });
    }
}
