package io.github.sinri.AiOnHttpMix.test.unit.mix.mixin.vision;

import io.github.sinri.AiOnHttpMix.mix.chat.message.MixChatMessage;
import io.github.sinri.AiOnHttpMix.mix.chat.message.MixChatVisionContentElement;
import io.github.sinri.AiOnHttpMix.mix.chat.request.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum;
import io.vertx.core.Future;

import java.util.List;

public interface MixVisionStreamNonFCMixin extends MixVisionCoreMixin {
    default Future<Void> toTestStream(SupportedModelEnum supportedModelEnum) {
        return getMixChatKit()
                .chatStream(
                        MixChatRequest.create()
                                      .setSupportedModelEnum(supportedModelEnum)
                                      .addMessage(MixChatMessage.create()
                                                                .setRole("user")
                                                                .setVisionContent(List.of(
                                                                        MixChatVisionContentElement.create()
                                                                                                   .setImage(getImageUrl()),
                                                                        MixChatVisionContentElement.create()
                                                                                                   .setText("这个照片是在哪里拍的")
                                                                ))
                                      ),
                        fragment -> {
                            getUnitTestLogger().info("fragment: \n" + fragment);
                            return Future.succeededFuture();
                        }
                )
                .compose(resp -> {
                    getUnitTestLogger().info("fin");
                    return Future.succeededFuture();
                });
    }
}
