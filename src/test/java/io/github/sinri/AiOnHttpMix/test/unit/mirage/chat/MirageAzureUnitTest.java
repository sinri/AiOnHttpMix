package io.github.sinri.AiOnHttpMix.test.unit.mirage.chat;

import io.github.sinri.AiOnHttpMix.test.unit.core.TestPassed;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import org.junit.Test;

public class MirageAzureUnitTest extends AnyMirageUnitTest {

    @Override
    public String getModel() {
        return SupportedModel.ChatGPT.name();
    }

    @Override
    public String getService() {
        return "gpt-4-o";
    }

    @Test
    @TestPassed(time = "2025-02-14")
    @Override
    public void testSyncWithFC() {
        super.testSyncWithFC();
    }

    @Test
    @TestPassed(time = "2025-02-14")
    @Override
    public void testStreamWithFC() {
        super.testStreamWithFC();
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
