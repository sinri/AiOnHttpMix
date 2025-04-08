package io.github.sinri.AiOnHttpMix.utils;

import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMResponse;

public interface LLMStreamBuffer {
    AnyLLMResponse toAnyLLMResponse();
}
