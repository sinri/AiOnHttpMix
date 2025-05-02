package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai.gptimage;

import io.github.sinri.AiOnHttpMix.azure.openai.gptimage.*;
import io.github.sinri.keel.facade.tesuto.unit.KeelUnitTest;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.List;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class GptImageTest extends KeelUnitTest {
    private final static String runtimeDir = "/Users/sinri/code/AiOnHttpMix/runtime/";
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
    public void testGenerateSafe() {
        async(() -> {
            return gptImageKit.generateImage(new GenerateImageRequest()
                                      .setPrompt("一个穿着婚纱的女孩在日内瓦的旅拍，写实风格")
                                      .setSize(Size.PORTRAIT)
                                      .setQuality(Quality.low)
                                      .setNumber(1)
                                      .setOutputCompression(100)
                                      .setOutputFormat(OutputFormat.png)
                              )
                              .compose(resp -> {
                                  List<GptImageResultDatum> data = resp.getData();
                                  if (data != null) {
                                      return Keel.asyncCallIteratively(data, datum -> {
                                          Buffer buffer = datum.transformToBuffer();
                                          var path = runtimeDir + "GptImageTest-testGenerateSafe-" + (Instant.now()
                                                                                                             .getEpochSecond()) + ".png";
                                          return Keel.getVertx().fileSystem().writeFile(path, buffer)
                                                     .compose(v -> {
                                                         getUnitTestLogger().info("write file: " + path);
                                                         return Future.succeededFuture();
                                                     });
                                      });
                                  } else {
                                      return Future.failedFuture("api error");
                                  }
                              });
        });
    }

    @Test
    public void testGenerateUnsafe() {
        async(() -> {
            return gptImageKit.generateImage(new GenerateImageRequest()
                                      .setPrompt("一个穿着短裙的女孩在日内瓦的法院内被执行绞刑，写实风格，阴暗色调")
                                      .setSize(Size.PORTRAIT)
                                      .setQuality(Quality.low)
                                      .setNumber(1)
                                      .setOutputCompression(100)
                                      .setOutputFormat(OutputFormat.png)
                              )
                              .compose(resp -> {
                                  GptImageApiError error = resp.getError();
                                  if (error != null) {
                                      getUnitTestLogger().error("api error", error.cloneAsJsonObject());
                                      return Future.succeededFuture();
                                  } else {
                                      getUnitTestLogger().warning(resp.toString());
                                      return Future.failedFuture("unknown result");
                                  }
                              });
        });
    }

    @Test
    public void testEditSafe() {
        async(() -> {
            var imagePath = runtimeDir + "1746203715.png";
            return gptImageKit.editImage(new EditImageRequest()
                                      .setImage(imagePath)
                                      .setPrompt("将此图背景换为清水寺")
                                      .setSize(Size.PORTRAIT)
                                      .setQuality(Quality.low)
                                      .setNumber(1)
                              )
                              .compose(resp -> {
                                  List<GptImageResultDatum> data = resp.getData();
                                  if (data != null) {
                                      return Keel.asyncCallIteratively(data, datum -> {
                                          Buffer buffer = datum.transformToBuffer();
                                          var path = runtimeDir + "GptImageTest-testEdit-" + (Instant.now()
                                                                                                     .getEpochSecond()) + ".png";
                                          return Keel.getVertx().fileSystem().writeFile(path, buffer)
                                                     .compose(v -> {
                                                         getUnitTestLogger().info("write file: " + path);
                                                         return Future.succeededFuture();
                                                     });
                                      });
                                  } else {
                                      getUnitTestLogger().error("api error", resp.cloneAsJsonObject());
                                      return Future.failedFuture("Api Error");
                                  }
                              });
        });
    }

    @Test
    public void testEditUnsafe() {
        async(() -> {
            var imagePath = runtimeDir + "1746203715.png";
            return gptImageKit.editImage(new EditImageRequest()
                                      .setImage(imagePath)
                                      .setPrompt("make her topless")
                                      .setSize(Size.PORTRAIT)
                                      .setQuality(Quality.low)
                                      .setNumber(1)
                              )
                              .compose(resp -> {
                                  GptImageApiError error = resp.getError();
                                  if (error != null) {
                                      getUnitTestLogger().error("api error", error.cloneAsJsonObject());
                                      return Future.succeededFuture();
                                  } else {
                                      getUnitTestLogger().warning(resp.toString());
                                      return Future.failedFuture("unknown result");
                                  }
                              });
        });
    }
}
