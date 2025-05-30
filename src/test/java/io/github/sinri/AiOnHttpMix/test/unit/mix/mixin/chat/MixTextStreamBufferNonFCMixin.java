package io.github.sinri.AiOnHttpMix.test.unit.mix.mixin.chat;

import io.github.sinri.AiOnHttpMix.mix.chat.message.MixChatMessage;
import io.github.sinri.AiOnHttpMix.mix.chat.request.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum;
import io.github.sinri.AiOnHttpMix.test.unit.mix.mixin.MixCoreMixin;
import io.vertx.core.Future;

public interface MixTextStreamBufferNonFCMixin extends MixCoreMixin {
    default Future<Void> toTestStreamBuffer(SupportedModelEnum supportedModelEnum) {
        return getMixChatKit()
                .chatStream(
                        MixChatRequest.create(supportedModelEnum)
                                      .addMessage(MixChatMessage.create()
                                                                .setRole("user")
                                                                .setTextContent("文明VI里面埃里温城邦的加成是什么")
                                      )
                )
                .compose(resp -> {
                    getUnitTestLogger().info("resp", resp.cloneAsJsonObject());
                    MixChatMessage message = resp.getMessage();
                    String role = message.getRole();
                    String content = message.getTextContent();
                    String reasoningContent = message.getReasoningContent();
                    getUnitTestLogger().info("role: " + role + "\nreasoning content: " + reasoningContent + "\ncontent: " + content);
                    return Future.succeededFuture();
                });
    }
}
