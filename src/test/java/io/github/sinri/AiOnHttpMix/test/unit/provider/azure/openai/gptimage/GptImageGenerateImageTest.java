package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai.gptimage;

import io.github.sinri.AiOnHttpMix.azure.openai.gptimage.*;
import io.github.sinri.keel.facade.tesuto.unit.KeelUnitTest;
import io.vertx.core.buffer.Buffer;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class GptImageGenerateImageTest extends KeelUnitTest {
    private GPTImageKit gptImageKit;

    @Before
    @Override
    public void setUp() {
        var serviceName = getServiceName();

        String apiKey = Keel.config("azure.openai." + serviceName + ".apiKey");
        String resourceName = Keel.config("azure.openai." + serviceName + ".resourceName");
        String deployment = Keel.config("azure.openai." + serviceName + ".deployment");
        String apiVersion = Keel.config("azure.openai." + serviceName + ".apiVersion");

        gptImageKit = new GPTImageKit(resourceName, deployment, apiVersion, apiKey);
    }

    private String getServiceName() {
        return "gpt-image-1";
    }

    @Test
    public void testGenerate() {
        async(() -> {
            return gptImageKit.generateImage(new GenerateImageRequest()
                                      .setPrompt("一个穿着学校制服的中国女孩行走在日内瓦的法院内，吉卜力风格，阴暗色调")
                                      .setSize(Size.PORTRAIT)
                                      .setQuality(Quality.low)
                                      .setNumber(1)
                              )
                              .compose(imagesInBase64 -> {
                                  String s = imagesInBase64.get(0);
                                  byte[] bytes = Keel.stringHelper().decodeWithBase64ToBytes(s);
                                  return Keel.getVertx().fileSystem().writeFile(
                                          "/Users/sinri/code/AiOnHttpMix/runtime/" + (Instant.now()
                                                                                             .getEpochSecond()) + ".png",
                                          Buffer.buffer(bytes)
                                  );
                              });
        });
    }

    @Test
    public void testEdit() {
        async(() -> {
            var imagePath = "/Users/sinri/code/AiOnHttpMix/runtime/1745980546.png";
            return gptImageKit.editImage(new EditImageRequest()
                                      .setImage(imagePath)
                                      .setPrompt("将图中女孩替换成当地的年轻修女，整体色调改为明朗")
                                      .setSize(Size.PORTRAIT)
                                      .setQuality(Quality.low)
                                      .setNumber(1)
                              )
                              .compose(imagesInBase64 -> {
                                  String s = imagesInBase64.get(0);
                                  byte[] bytes = Keel.stringHelper().decodeWithBase64ToBytes(s);
                                  return Keel.getVertx().fileSystem().writeFile(
                                          "/Users/sinri/code/AiOnHttpMix/runtime/" + (Instant.now()
                                                                                             .getEpochSecond()) + ".png",
                                          Buffer.buffer(bytes)
                                  );
                              });
        });
    }
}
