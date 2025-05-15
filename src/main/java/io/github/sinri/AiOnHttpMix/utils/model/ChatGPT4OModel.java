package io.github.sinri.AiOnHttpMix.utils.model;

import io.github.sinri.AiOnHttpMix.utils.series.ChatModelSeries;

public final class ChatGPT4OModel implements ChatModel {
    public final static String NAME = "gpt-4o";

    ChatGPT4OModel() {
    }

    @Override
    public ChatModelSeries getSeries() {
        return ChatModelSeries.chatgpt;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
