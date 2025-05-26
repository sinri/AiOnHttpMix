package io.github.sinri.AiOnHttpMix.test.unit.mix.mixin;

import io.github.sinri.AiOnHttpMix.mix.chat.MixChatKit;
import io.github.sinri.keel.facade.tesuto.unit.KeelUnitTestCore;

public interface MixCoreMixin extends KeelUnitTestCore {
    MixChatKit getMixChatKit();
}
