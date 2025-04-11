package io.github.sinri.AiOnHttpMix.test.unit.mirage.vl;

import io.github.sinri.AiOnHttpMix.mirage.vl.MirageVLRequestEntity;

public interface AnyMirageVLUnitTestWithoutFCMixin extends AnyMirageVLUnitTestCommonMixin {
    MirageVLRequestEntity generateVLRequestWithoutFC();

    void testSyncWithoutFC();

    void testStreamWithoutFC();
}
