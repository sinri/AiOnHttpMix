package io.github.sinri.AiOnHttpMix.utils.models.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.specification.DashscopeModelSpecification;

public abstract sealed class QwenModelSeries
        extends DashscopeModelSpecification implements ChatModel
        permits QwenChatModelSeries, QwenVisionModelSeries {
}
