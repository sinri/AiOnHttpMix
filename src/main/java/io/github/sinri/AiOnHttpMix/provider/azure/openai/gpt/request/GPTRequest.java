package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.request;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.GPTMessage;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.GPTMessageInRequest;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.GPTMessageInResponse;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolDefinition;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonToolDefinition;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public interface GPTRequest extends JsonifiableEntity<GPTRequest> {

    static GPTRequest create() {
        return new GPTRequestImpl();
    }

    static GPTRequest wrap(JsonObject jsonObject) {
        return new GPTRequestImpl(jsonObject);
    }

    /**
     * If set, partial message deltas will be sent, like in ChatGPT.
     * Tokens will be sent as data-only server-sent events as they become available,
     * with the stream terminated by a
     * {@code data: [DONE]} message.
     */
    default GPTRequest stream(boolean stream) {
        return write("stream", stream);
    }

    default Boolean stream() {
        return readBoolean("stream");
    }

    /**
     * What sampling temperature to use, between 0 and 2.
     * Higher values like 0.8 will make the output more random, while lower values
     * like 0.2 will make it more focused
     * and deterministic.
     * We generally recommend altering this or top_p but not both.
     *
     * @param temperature (0,2), default 1
     */
    default GPTRequest temperature(float temperature) {
        return write("temperature", temperature);
    }

    default Float temperature() {
        return readFloat("temperature");
    }

    /**
     * Whether to return log probabilities of the output tokens or not.
     * If true, returns the log probabilities of each output token returned in the
     * content of message.
     *
     * @param logprobs default false
     */
    default GPTRequest logprobs(boolean logprobs) {
        return write("logprobs", logprobs);
    }

    default Boolean logprobs() {
        return readBoolean("logprobs");
    }

    /**
     * Specifies the number of most likely tokens to return at each token position,
     * each with an associated log
     * probability.
     * logprobs must be set to true if this parameter is used.
     *
     * @param top_logprobs integer between 0 and 20
     */
    default GPTRequest topLogprobs(int top_logprobs) {
        return write("top_logprobs", top_logprobs);
    }

    default Integer topLogprobs() {
        return readInteger("top_logprobs");
    }

    /**
     * How many chat completion choices to generate for each input message.
     * Note that you'll be charged based on the number of generated tokens across all of the choices.
     * Keep n as 1 to minimize costs.
     *
     * @param n default 1
     */
    default GPTRequest n(int n) {
        return write("n", n);
    }

    default Integer n() {
        return readInteger("n");
    }

    /**
     * Whether to enable parallel function calling during tool use.
     *
     * @param parallel_tool_calls default true
     */
    default GPTRequest parallelToolCalls(boolean parallel_tool_calls) {
        return write("parallel_tool_calls", parallel_tool_calls);
    }

    default Boolean parallelToolCalls() {
        return readBoolean("parallel_tool_calls");
    }

    /**
     * An object specifying the format that the model must output.
     * Compatible with GPT-4o, GPT-4o mini, GPT-4 Turbo and all GPT-3.5 Turbo models
     * newer than gpt-3.5-turbo-1106.
     * Setting to {@code {"type": "json_schema", "json_schema": {...}}} enables
     * Structured Outputs.
     * Setting to {@code {"type": "json_object"}} enables JSON mode.
     *
     * @param responseFormat ResponseFormatText or ResponseFormatJsonObject or
     *                       ResponseFormatJsonSchema
     */
    default GPTRequest responseFormat(GPTResponseFormat responseFormat) {
        return write("response_format", responseFormat.toJsonObject());
    }

    default GPTResponseFormat responseFormat() {
        JsonObject x = readJsonObject("response_format");
        if (x == null) {
            x = new JsonObject();
        }
        return GPTResponseFormat.wrap(x);
    }

    /**
     * If specified, our system will make a best effort to sample deterministically.
     * Repeated requests with the same seed and parameters should return the same
     * result.
     * Determinism isn't guaranteed.
     *
     * @param seed integer (Beta feature)
     */
    default GPTRequest seed(Integer seed) {
        return write("seed", seed);
    }

    default Integer seed() {
        return readInteger("seed");
    }

    /**
     * A list of tools the model may call. Currently, only functions are supported
     * as a tool.
     * Use this to provide a list of functions the model may generate JSON inputs
     * for.
     * A max of 128 functions are supported.
     *
     * @param toolDefinition tool definition
     */
    default GPTRequest addTool(ToolDefinition toolDefinition) {
        ensureJsonArray("tools")
                .add(toolDefinition.toJsonObject());
        return this;
    }

    default GPTRequest addTool(Handler<ToolDefinition> toolDefinitionHandler) {
        var x = new CommonToolDefinition(new JsonObject());
        toolDefinitionHandler.handle(x);
        return addTool(x);
    }

    default List<CommonToolDefinition> tools() {
        List<JsonObject> tools = readJsonObjectArray("tools");
        if (tools == null)
            return List.of();
        return tools.stream().map(CommonToolDefinition::new).toList();
    }

    /**
     * Controls which (if any) tool is called by the model.
     * required: model must call one or more tools
     * {@code {"type": "function", "function": {"name": "my_function"}}}: forces
     * model to call that tool
     *
     * @param functionName forces model to call that tool with this name
     */
    default GPTRequest toolChoiceForFunctionType(String functionName) {
        return write("tool_choice", new JsonObject()
                .put("type", "function")
                .put("function", new JsonObject().put("name", functionName)));
    }

    /**
     * @param toolChoice Controls which (if any) tool is called by the model.
     *                   none: model won't call any tool and generates a message
     *                   auto: model can pick between generating a message or
     *                   calling tools
     */
    default GPTRequest toolChoice(String toolChoice) {
        return write("tool_choice", toolChoice);
    }

    default String toolChoiceType() {
        var type = readString("tool_choice");
        if (type != null) {
            return type;
        }
        JsonObject object = readJsonObject("tool_choice");
        if (object == null) {
            return null;
        }
        return object.getString("type");
    }

    default String toolChoiceFunction() {
        JsonObject object = readJsonObject("tool_choice");
        if (object == null) {
            return null;
        }
        var type = object.getString("type");
        if (!Objects.equals("function", type)) {
            return null;
        }
        JsonObject f = object.getJsonObject("function");
        if (f == null)
            return null;
        return f.getString("name");
    }

    /**
     * @param stop Up to four sequences where the API will stop generating further
     *             tokens.
     */
    default GPTRequest stop(String stop) {
        return this.write("stop", stop);
    }

    default String stop() {
        return readString("stop");
    }

    /**
     * The total length of input tokens and generated tokens is limited by the
     * model's context length.
     *
     * @param max_tokens The maximum number of tokens that can be generated in the
     *                   chat completion.
     */
    default GPTRequest maxTokens(int max_tokens) {
        return this.write("max_tokens", max_tokens);
    }

    default Integer maxTokens() {
        return readInteger("max_tokens");
    }

    /**
     * @param max_completion_tokens An upper bound for the number of tokens that can
     *                              be generated for a completion,
     *                              including visible output tokens and reasoning
     *                              tokens.
     */
    default GPTRequest maxCompletionTokens(int max_completion_tokens) {
        return this.write("max_completion_tokens", max_completion_tokens);
    }

    default Integer maxCompletionTokens() {
        return readInteger("max_completion_tokens");
    }

    /**
     * Positive values penalize new tokens based on whether they appear in the text
     * so far, increasing the model's
     * likelihood to talk about new topics.
     *
     * @param presence_penalty Number between -2.0 and 2.0. default 0
     */
    default GPTRequest presencePenalty(float presence_penalty) {
        return write("presence_penalty", presence_penalty);
    }

    default Float presencePenalty() {
        return readFloat("presence_penalty");
    }

    /**
     * Positive values penalize new tokens based on their existing frequency in the
     * text so far, decreasing the model's
     * likelihood to repeat the same line verbatim.
     *
     * @param frequency_penalty Number between -2.0 and 2.0.
     */
    default GPTRequest frequencyPenalty(float frequency_penalty) {
        return write("frequency_penalty", frequency_penalty);
    }

    default Float frequencyPenalty() {
        return readFloat("frequency_penalty");
    }

    /**
     * Modify the likelihood of specified tokens appearing in the completion.
     * Accepts a JSON object that maps tokens (specified by their token ID in the
     * tokenizer) to an associated bias value from -100 to 100.
     * Mathematically, the bias is added to the logits generated by the model prior
     * to sampling. The
     * exact effect will vary per model, but values between -1 and 1 should decrease
     * or increase likelihood of
     * selection; values like -100 or 100 should result in a ban or exclusive
     * selection of the relevant token.
     */
    default GPTRequest logitBias(JsonObject logit_bias) {
        return write("logit_bias", logit_bias);
    }

    default JsonObject logitBias() {
        return readJsonObject("logit_bias");
    }

    /**
     * @param user A unique identifier representing your end-user, which can help to
     *             monitor and detect abuse.
     */
    default GPTRequest user(String user) {
        return write("user", user);
    }

    default String user() {
        return readString("user");
    }

    default GPTRequest addMessage(GPTMessage message) {
        this.ensureJsonArray("messages")
            .add(message.toJsonObject());
        return this;
    }

    default GPTRequest addSystemMessage(String content) {
        return this.addMessage(GPTMessageInRequest.createAsSystem(content, null));
    }

    default GPTRequest addUserMessage(String content) {
        return this.addMessage(GPTMessageInRequest.createAsUser(content, null));
    }

    default GPTRequest addAssistantMessage(String content) {
        return this.addMessage(GPTMessageInRequest.createAsAssistant(content, null, null));
    }

    default GPTRequest addToolCallMessage(@Nullable String content, List<CommonToolCall> toolCalls) {
        return this.addMessage(GPTMessageInRequest.createAsToolCall(content, toolCalls, null, null));
    }

    default GPTRequest addToolCallMessage(GPTMessageInResponse messageInResponse) {
        return this.addMessage(messageInResponse);
    }

    default GPTRequest addToolOutputMessage(@Nullable String content, String tool_call_id) {
        return this.addMessage(GPTMessageInRequest.createAsToolOutput(content, tool_call_id));
    }

    /**
     * A list of messages comprising the conversation so far.
     */
    default List<GPTMessageInRequest> messages() {
        List<JsonObject> messages = readJsonObjectArray("messages");
        if (messages == null)
            return List.of();
        return messages.stream().map(GPTMessageInRequest::wrap).toList();
    }

    // Field data_sources is not implemented: The configuration entries for Azure
    // OpenAI chat extensions that use them. This additional specification is only
    // compatible with Azure OpenAI.
}
