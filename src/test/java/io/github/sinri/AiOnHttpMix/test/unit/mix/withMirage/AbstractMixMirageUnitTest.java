package io.github.sinri.AiOnHttpMix.test.unit.mix.withMirage;

import io.github.sinri.AiOnHttpMix.mix.chat.MixChatKit;
import io.github.sinri.AiOnHttpMix.mix.service.MirageMixServiceAdapter;
import io.github.sinri.keel.facade.tesuto.unit.KeelUnitTest;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class AbstractMixMirageUnitTest extends KeelUnitTest {
    private final MirageMixServiceAdapter serviceAdapter;
    private final MixChatKit mixChatKit;

    public AbstractMixMirageUnitTest() {
        super();
        serviceAdapter = new MirageMixServiceAdapter(Keel.getConfiguration().extract("provider"));
        mixChatKit = MixChatKit.create(serviceAdapter);
    }

    public MirageMixServiceAdapter getServiceAdapter() {
        return serviceAdapter;
    }

    public MixChatKit getMixChatKit() {
        return mixChatKit;
    }
}
