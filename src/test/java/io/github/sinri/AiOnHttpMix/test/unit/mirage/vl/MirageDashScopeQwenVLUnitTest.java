package io.github.sinri.AiOnHttpMix.test.unit.mirage.vl;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.mirage.vl.MirageVLSDK;
import io.github.sinri.AiOnHttpMix.utils.SupportedVLModel;
import io.vertx.core.Future;
import org.junit.Test;

public class MirageDashScopeQwenVLUnitTest extends AnyMirageVLUnitTest {

    @Test
    @Override
    public void testSyncWithoutFC() {
        async(() -> {
            AigcMix.enableVerboseLogger();
            MirageVLSDK mirageVLSDK = generateMirageVLSDK();
            return mirageVLSDK.requestSync(getModel(), getService(), false, generateVLRequestWithoutFC())
                              .compose(anyVLLMResponse -> {
                                  getUnitTestLogger().info("anyVLLMResponse by " + anyVLLMResponse.getRole()
                                                                                                  .name() + ": ");
                                  anyVLLMResponse.getContent().forEach(x -> {
                                      getUnitTestLogger().info("TYPE: " + x.type() + " : " + x.value());
                                  });
                                  return Future.succeededFuture();
                              });
        });

    }

    @Test
    @Override
    public void testStreamWithoutFC() {
        async(() -> {
            AigcMix.enableVerboseLogger();
            MirageVLSDK mirageVLSDK = generateMirageVLSDK();
            return mirageVLSDK.requestStream(
                                      getModel(),
                                      getService(),
                                      false,
                                      generateVLRequestWithoutFC(),
                                      180_000L,
                                      s -> {
                                          getUnitTestLogger().info("Chunk: " + s);
                                      })
                              .compose(anyVLLMResponse -> {
                                  getUnitTestLogger().info("fin");
                                  return Future.succeededFuture();
                              });
        });
    }

    @Override
    public String getModel() {
        return SupportedVLModel.QwenVLPlus.name();
    }

    @Override
    public String getService() {
        return null;
    }
}
