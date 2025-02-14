package io.github.sinri.AiOnHttpMix.test.unit.mirage;

import io.github.sinri.AiOnHttpMix.mirage.MirageRequestEntity;

public interface AnyMirageUnitTestWithoutFCMixin extends AnyMirageUnitTestCommonMixin {
    MirageRequestEntity generateRequestWithoutFC();

    void testSyncWithoutFC();

    void testStreamWithoutFC();
}
