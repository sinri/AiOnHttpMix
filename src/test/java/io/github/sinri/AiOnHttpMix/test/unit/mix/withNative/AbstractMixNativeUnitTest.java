package io.github.sinri.AiOnHttpMix.test.unit.mix.withNative;

import io.github.sinri.AiOnHttpMix.mix.chat.MixChatKit;
import io.github.sinri.AiOnHttpMix.mix.service.NativeMixServiceAdapter;
import io.github.sinri.keel.facade.tesuto.unit.KeelUnitTest;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public abstract class AbstractMixNativeUnitTest extends KeelUnitTest {
    private final NativeMixServiceAdapter serviceAdapter;
    private final MixChatKit mixChatKit;

    public AbstractMixNativeUnitTest() {
        super();
        serviceAdapter = new NativeMixServiceAdapter(Keel.getConfiguration().extract("provider"));
        mixChatKit = MixChatKit.create(serviceAdapter);
    }

    public NativeMixServiceAdapter getServiceAdapter() {
        return serviceAdapter;
    }

    public MixChatKit getMixChatKit() {
        return mixChatKit;
    }
}
