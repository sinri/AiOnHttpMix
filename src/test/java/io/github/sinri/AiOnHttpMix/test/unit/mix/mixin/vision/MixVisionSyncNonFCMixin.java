package io.github.sinri.AiOnHttpMix.test.unit.mix.mixin.vision;

import io.github.sinri.AiOnHttpMix.mix.chat.message.MixChatMessage;
import io.github.sinri.AiOnHttpMix.mix.chat.message.MixChatVisionContentElement;
import io.github.sinri.AiOnHttpMix.mix.chat.text.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum;
import io.vertx.core.Future;

import java.util.List;

public interface MixVisionSyncNonFCMixin extends MixVisionCoreMixin {

    default Future<Void> toTestSync(SupportedModelEnum supportedModelEnum) {
        return getMixChatKit()
                .chat(MixChatRequest.create()
                                    .setSupportedModelEnum(supportedModelEnum)
                                    .addMessage(MixChatMessage.create()
                                                              .setRole("user")
                                                              .setVisionContent(List.of(
                                                                      MixChatVisionContentElement.create()
                                                                                                 .setImage(getImageUrl()),
                                                                      MixChatVisionContentElement.create()
                                                                                                 .setText("这个照片是在哪里拍的")
                                                              ))
                                    )
                )
                .compose(resp -> {
                    getUnitTestLogger().info("resp", resp.cloneAsJsonObject());
                    MixChatMessage message = resp.getMessage();
                    getUnitTestLogger().info("role: " + message.getRole());
                    getUnitTestLogger().info("reasoning content: " + message.getReasoningContent());
                    getUnitTestLogger().info("content: " + message.getTextContent());
                    var visionContent = message.getVisionContent();
                    for (var c : visionContent) {
                        getUnitTestLogger().info("content[]: " + c.getText());
                    }

                    return Future.succeededFuture();
                });
    }

}
