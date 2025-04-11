package io.github.sinri.AiOnHttpMix.test.unit.mirage.chat;

import io.github.sinri.AiOnHttpMix.mirage.chat.MirageRequestEntity;

public interface AnyMirageUnitTestWithoutFCMixin extends AnyMirageUnitTestCommonMixin {
    MirageRequestEntity generateRequestWithoutFC();

    void testSyncWithoutFC();

    void testStreamWithoutFC();
}
