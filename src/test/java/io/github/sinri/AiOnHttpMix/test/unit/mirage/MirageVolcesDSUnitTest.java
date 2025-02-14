package io.github.sinri.AiOnHttpMix.test.unit.mirage;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.test.unit.core.TestPassed;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import org.junit.Test;

public class MirageVolcesDSUnitTest extends AnyMirageUnitTest {

    @Override
    public String getModel() {
        return SupportedModel.DeepSeekChatOnVolces.name();
    }

    @Override
    public String getService() {
        return "DeepSeek-V3";
    }

    /**
     * Volces DeepSeek is not full powered: the requested model does not support function calling, please switch to a
     * different model or contact with administrator if you believe this model should support function calling.
     */
    @Test
    @Override
    public void testSyncWithFC() {
        // todo wait for available
        //        AigcMix.enableVerboseLogger();
        //        super.testSyncWithFC();
        //        AigcMix.disableVerboseLogger();
    }

    @Test
    @TestPassed(time = "2025-02-14")
    @Override
    public void testStreamWithFC() {
        AigcMix.enableVerboseLogger();
        super.testStreamWithFC();
        AigcMix.disableVerboseLogger();
    }

    @Test
    @TestPassed(time = "2025-02-14")
    @Override
    public void testSyncWithoutFC() {
        super.testSyncWithoutFC();
    }

    @Test
    @TestPassed(time = "2025-02-14")
    @Override
    public void testStreamWithoutFC() {
        super.testStreamWithoutFC();
    }
}
