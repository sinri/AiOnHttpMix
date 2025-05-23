package io.github.sinri.AiOnHttpMix.utils.models.mix;

import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.specification.MixModelSpecification;

@Deprecated
public class MixChatModel extends MixModelSpecification implements ChatModel {
    @Override
    public String getModelName() {
        return "MixChatModel";
    }
}
