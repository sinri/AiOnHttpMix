package io.github.sinri.AiOnHttpMix.test.unit.mirage;

import io.github.sinri.AiOnHttpMix.mirage.MirageRequestEntity;

public interface AnyMirageUnitTestWithFCMixin extends AnyMirageUnitTestCommonMixin {
    MirageRequestEntity generateRequestWithFC();

    void testSyncWithFC();

    void testStreamWithFC();
}
