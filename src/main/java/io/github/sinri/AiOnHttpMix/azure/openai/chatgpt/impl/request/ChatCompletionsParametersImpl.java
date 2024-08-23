package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.request;


import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class ChatCompletionsParametersImpl implements ChatGPTKit.ChatCompletionsParameters {
    private JsonObject jsonObject;

    public ChatCompletionsParametersImpl() {
        this.jsonObject = new JsonObject();
    }

    public ChatCompletionsParametersImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public ChatGPTKit.ChatCompletionsParameters setTemperature(double temperature) {
        this.jsonObject.put("temperature", temperature);
        return this;
    }

    @Override
    public ChatGPTKit.ChatCompletionsParameters setTopP(double topP) {
        this.jsonObject.put("top_p", topP);
        return this;
    }

    @Override
    public ChatGPTKit.ChatCompletionsParameters useStream() {
        this.jsonObject.put("stream", true);
        return this;
    }

    @Override
    public ChatGPTKit.ChatCompletionsParameters setMaxTokens(int maxTokens) {
        this.jsonObject.put("max_tokens", maxTokens);
        return this;
    }

    @Override
    public ChatGPTKit.ChatCompletionsParameters setPresencePenalty(double presencePenalty) {
        this.jsonObject.put("presence_penalty", presencePenalty);
        return this;
    }

    @Override
    public ChatGPTKit.ChatCompletionsParameters setFrequencyPenalty(double frequencyPenalty) {
        this.jsonObject.put("frequency_penalty", frequencyPenalty);
        return this;
    }

    @Override
    public ChatGPTKit.ChatCompletionsParameters addMessage(ChatGPTKit.Message message) {
        JsonArray messages = this.jsonObject.getJsonArray("messages");
        if (messages == null) {
            messages = new JsonArray();
            this.jsonObject.put("messages", messages);
        }
        messages.add(message.toJsonObject());
        return this;
    }

    @Override
    public ChatGPTKit.ChatCompletionsParameters setN(int n) {
        this.jsonObject.put("n", n);
        return this;
    }

    @Override
    public ChatGPTKit.ChatCompletionsParameters setSeed(int seed) {
        this.jsonObject.put("seed", seed);
        return this;
    }

    @Override
    public ChatGPTKit.ChatCompletionsParameters setResponseFormat(ChatCompletionResponseFormat responseFormat) {
        this.jsonObject.put("response_format", responseFormat.name());
        return this;
    }

    @Override
    public ChatGPTKit.ChatCompletionsParameters addTool(ChatGPTKit.ToolDefinition toolDefinition) {
        JsonArray tools = this.jsonObject.getJsonArray("tools");
        if (tools == null) {
            tools = new JsonArray();
            this.jsonObject.put("tools", tools);
        }
        tools.add(toolDefinition.toJsonObject());
        return this;
    }


    @Override
    public ChatGPTKit.ChatCompletionsParameters setToolChoice(ChatCompletionToolChoiceOption toolChoiceOption) {
        ChatCompletionToolChoiceOption.Type type = toolChoiceOption.getType();
        switch (type) {
            case none:
            case auto:
                this.jsonObject.put("tool_choice", type.name());
                break;
            case function:
                this.jsonObject.put("tool_choice", new JsonObject()
                        .put("type", type.name())
                        .put("function", new JsonObject()
                                .put("name", toolChoiceOption.getFunctionName()))
                );
        }

        return this;
    }

    @Override
    public ChatGPTKit.@NotNull ChatCompletionsParameters getImplementation() {
        return this;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public ChatGPTKit.@NotNull ChatCompletionsParameters reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
