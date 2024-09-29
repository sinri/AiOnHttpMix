package io.github.sinri.AiOnHttpMix.utils;

import io.github.sinri.AiOnHttpMix.mix.AnyLLMResponse;

public interface LLMStreamBuffer {
    AnyLLMResponse toAnyLLMResponse();
}
