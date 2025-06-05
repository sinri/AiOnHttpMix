package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai.stateful;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIConfigElement;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.stateful.OpenAIStatefulApiKit;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.stateful.StatefulChatRequest;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.stateful.StatefulChatResponseOutputItem;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import io.github.sinri.keel.facade.tesuto.unit.KeelUnitTest;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class OpenAIStatefulKitUnitTest extends KeelUnitTest {
    @Test
    public void test1() {
        async(() -> {
            KeelConfigElement chatgptConfig = Keel.getConfiguration().extract("provider", "azure", "openai");
            Assert.assertNotNull(chatgptConfig);

            Map<String, OpenAIConfigElement> map = chatgptConfig.getChildren()
                                                                .entrySet()
                                                                .stream()
                                                                .collect(Collectors.toMap(
                                                                        Map.Entry::getKey,
                                                                        entry -> new OpenAIConfigElement(entry.getValue())
                                                                ));

            OpenAIStatefulApiKit kit = new OpenAIStatefulApiKit(map);
            return kit.request(
                              "gpt-4dot1",
                              StatefulChatRequest.create()
                                                 .setInput("介绍日本旧海军的Akagi舰")
                                                 .setStore(true)
                      )
                      .compose(resp -> {
                          JsonObject entries = resp.cloneAsJsonObject();
                          getUnitTestLogger().info("resp", entries);

                          getUnitTestLogger().info("id: " + resp.getId());

                          getUnitTestLogger().info("status: " + resp.getStatus());
                          StatefulChatResponseOutputItem outputItem = resp.getOutput().get(0);
                          String role = outputItem.getRole();
                          getUnitTestLogger().info("role: " + role);
                          outputItem.getContent().forEach(c -> {
                              getUnitTestLogger().info("content[]: " + c.getText());
                          });

                          return Future.succeededFuture();
                      });
        });
    }

    @Test
    public void test2() {
        async(() -> {
            KeelConfigElement chatgptConfig = Keel.getConfiguration().extract("provider", "azure", "openai");
            Assert.assertNotNull(chatgptConfig);

            Map<String, OpenAIConfigElement> map = chatgptConfig.getChildren()
                                                                .entrySet()
                                                                .stream()
                                                                .collect(Collectors.toMap(
                                                                        Map.Entry::getKey,
                                                                        entry -> new OpenAIConfigElement(entry.getValue())
                                                                ));

            OpenAIStatefulApiKit kit = new OpenAIStatefulApiKit(map);

            AtomicReference<String> firstRoundIdRef = new AtomicReference<>();

            return Future.succeededFuture()
                         .compose(v -> {
                             return kit.request(
                                     "gpt-4dot1",
                                     StatefulChatRequest.create()
                                                        .setInput("目前有哪些主流的微型计算机操作系统")
                                                        .setStore(true)
                             );
                         })
                         .compose(resp -> {
                             // JsonObject entries = resp.cloneAsJsonObject();
                             //getUnitTestLogger().info("resp", entries);

                             getUnitTestLogger().info("first round resp, id: " + resp.getId());
                             firstRoundIdRef.set(resp.getId());

                             getUnitTestLogger().info("status: " + resp.getStatus());
                             StatefulChatResponseOutputItem outputItem = resp.getOutput().get(0);
                             String role = outputItem.getRole();
                             getUnitTestLogger().info("role: " + role);
                             outputItem.getContent().forEach(c -> {
                                 getUnitTestLogger().info("content[]: " + c.getText());
                             });

                             return Future.succeededFuture();
                         })
                         .compose(v -> {
                             return kit.request(
                                     "gpt-4dot1",
                                     StatefulChatRequest.create()
                                                        .setPreviousResponseId(firstRoundIdRef.get())
                                                        .setInput("其中开源的有哪些？")
                                                        .setStore(true)
                             );
                         })
                         .compose(resp -> {
                             getUnitTestLogger().info("second round resp, id: " + resp.getId());
                             firstRoundIdRef.set(resp.getId());

                             getUnitTestLogger().info("status: " + resp.getStatus());
                             StatefulChatResponseOutputItem outputItem = resp.getOutput().get(0);
                             String role = outputItem.getRole();
                             getUnitTestLogger().info("role: " + role);
                             outputItem.getContent().forEach(c -> {
                                 getUnitTestLogger().info("content[]: " + c.getText());
                             });

                             return Future.succeededFuture();
                         });
        });
    }
}
