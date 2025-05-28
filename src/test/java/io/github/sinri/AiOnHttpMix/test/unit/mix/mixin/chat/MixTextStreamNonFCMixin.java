package io.github.sinri.AiOnHttpMix.test.unit.mix.mixin.chat;

import io.github.sinri.AiOnHttpMix.mix.chat.message.MixChatMessage;
import io.github.sinri.AiOnHttpMix.mix.chat.request.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum;
import io.github.sinri.AiOnHttpMix.test.unit.mix.mixin.MixCoreMixin;
import io.vertx.core.Future;

public interface MixTextStreamNonFCMixin extends MixCoreMixin {
    default Future<Void> toTestStream(SupportedModelEnum supportedModelEnum) {
        return getMixChatKit()
                .chatStream(
                        MixChatRequest.create()
                                      .setSupportedModelEnum(supportedModelEnum)
                                      .addMessage(MixChatMessage.create()
                                                                .setRole("user")
                                                                .setTextContent("文明VI里面埃里温城邦的加成是什么")
                                      ),
                        fragmentData -> {
                            getUnitTestLogger().info("fragment data: \n" + fragmentData);
                            return Future.succeededFuture();
                        }
                )
                .compose(v -> {
                    getUnitTestLogger().info("fin");
                    return Future.succeededFuture();
                });
    }
}
