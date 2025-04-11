package io.github.sinri.AiOnHttpMix.test.unit.mirage.chat;

import io.github.sinri.AiOnHttpMix.mirage.chat.MirageRequestEntity;

public interface AnyMirageUnitTestWithFCMixin extends AnyMirageUnitTestCommonMixin {
    MirageRequestEntity generateRequestWithFC();

    void testSyncWithFC();

    void testStreamWithFC();
}
