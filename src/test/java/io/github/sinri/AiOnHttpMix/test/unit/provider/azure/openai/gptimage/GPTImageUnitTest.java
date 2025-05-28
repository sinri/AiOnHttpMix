package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai.gptimage;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIConfigElement;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gptimage.GPTImageKit;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gptimage.GenerateImageRequest;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gptimage.OutputFormat;
import io.github.sinri.keel.facade.tesuto.unit.KeelUnitTest;
import io.vertx.core.buffer.Buffer;
import org.junit.Test;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class GPTImageUnitTest extends KeelUnitTest {
    @Test
    public void test1() {
        async(() -> {
            var c = Keel.getConfiguration().extract("azure", "openai", "gpt-image-1");
            OpenAIConfigElement openAIConfigElement = new OpenAIConfigElement(c);
            GPTImageKit gptImageKit = new GPTImageKit(openAIConfigElement);
            getUnitTestLogger().info("start");
            return gptImageKit.generateImage(new GenerateImageRequest()
                                      .setPrompt("一张波斯菊特写")
                                      .setOutputFormat(OutputFormat.png)
                                      .setNumber(1)
                              )
                              .compose(resp -> {
                                  getUnitTestLogger().info("resp");
                                  Buffer buffer = resp.getData().get(0).transformToBuffer();
                                  var path = "/Users/sinri/code/AiOnHttpMix/runtime/" + System.currentTimeMillis() + ".png";
                                  getUnitTestLogger().info("path: " + path);
                                  return Keel.getVertx().fileSystem().writeFile(path, buffer);
                              });
        });
    }
}
