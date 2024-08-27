package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.request;


import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.message.OpenAIChatGptMessage;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class OpenAIChatGptRequestImpl implements OpenAIChatGptRequest {
    private JsonObject jsonObject;

    public OpenAIChatGptRequestImpl() {
        this.jsonObject = new JsonObject();
    }

    public OpenAIChatGptRequestImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public OpenAIChatGptRequest setTemperature(double temperature) {
        this.jsonObject.put("temperature", temperature);
        return this;
    }

    @Override
    public OpenAIChatGptRequest setTopP(double topP) {
        this.jsonObject.put("top_p", topP);
        return this;
    }

    @Override
    public OpenAIChatGptRequest useStream() {
        this.jsonObject.put("stream", true);
        return this;
    }

    @Override
    public OpenAIChatGptRequest setMaxTokens(int maxTokens) {
        this.jsonObject.put("max_tokens", maxTokens);
        return this;
    }

    @Override
    public OpenAIChatGptRequest setPresencePenalty(double presencePenalty) {
        this.jsonObject.put("presence_penalty", presencePenalty);
        return this;
    }

    @Override
    public OpenAIChatGptRequest setFrequencyPenalty(double frequencyPenalty) {
        this.jsonObject.put("frequency_penalty", frequencyPenalty);
        return this;
    }

    @Override
    public OpenAIChatGptRequest addMessage(OpenAIChatGptMessage message) {
        JsonArray messages = this.jsonObject.getJsonArray("messages");
        if (messages == null) {
            messages = new JsonArray();
            this.jsonObject.put("messages", messages);
        }
        messages.add(message.toJsonObject());
        return this;
    }

    @Override
    public OpenAIChatGptRequest setN(int n) {
        this.jsonObject.put("n", n);
        return this;
    }

    @Override
    public OpenAIChatGptRequest setSeed(int seed) {
        this.jsonObject.put("seed", seed);
        return this;
    }

    @Override
    public OpenAIChatGptRequest setResponseFormat(ChatCompletionResponseFormat responseFormat) {
        this.jsonObject.put("response_format", responseFormat.name());
        return this;
    }

    @Override
    public OpenAIChatGptRequest addTool(OpenAIChatGptToolDefinition toolDefinition) {
        JsonArray tools = this.jsonObject.getJsonArray("tools");
        if (tools == null) {
            tools = new JsonArray();
            this.jsonObject.put("tools", tools);
        }
        tools.add(toolDefinition.toJsonObject());
        return this;
    }


    @Override
    public OpenAIChatGptRequest setToolChoice(OpenAIChatGptRequestToolChoiceOption toolChoiceOption) {
        OpenAIChatGptRequestToolChoiceOption.Type type = toolChoiceOption.getType();
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
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull OpenAIChatGptRequest reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
