package io.github.sinri.AiOnHttpMix.moonshot.kimi;

import io.github.sinri.AiOnHttpMix.moonshot.core.MoonshotServiceMeta;
import io.github.sinri.AiOnHttpMix.moonshot.kimi.impl.*;
import io.github.sinri.AiOnHttpMix.moonshot.kimi.mixin.*;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * Moonshot is not tested due to account lack of money.
 */
public class KimiKit {
    public Future<JsonObject> getModels(MoonshotServiceMeta serviceMeta, String requestId) {
        return serviceMeta.requestGet("/v1/models", requestId);
    }

    public Future<JsonObject> chat(MoonshotServiceMeta serviceMeta, JsonObject jsonObject, String requestId) {
        return serviceMeta.request("/v1/chat/completions", jsonObject, requestId);
    }

    public Future<ChatCompletionsResponse> chat(MoonshotServiceMeta serviceMeta, ChatCompletionsRequest chatCompletionsRequest, String requestId) {
        return serviceMeta.request("/v1/chat/completions", chatCompletionsRequest.toJsonObject(), requestId)
                .compose(jsonObject -> {
                    return Future.succeededFuture(ChatCompletionsResponse.wrap(jsonObject));
                });
    }

    // TODO SSE

    public enum Model {
        MoonshotFirstVersion8k("moonshot-v1-8k"),
        MoonshotFirstVersion32k("moonshot-v1-32k"),
        MoonshotFirstVersion128k("moonshot-v1-128k"),
        ;
        private final String modelCode;

        Model(String modelCode) {
            this.modelCode = modelCode;
        }

        public String getModelCode() {
            return modelCode;
        }

        public static Model fromModelCode(String modelCode) {
            return switch (modelCode) {
                case "moonshot-v1-8k" -> MoonshotFirstVersion8k;
                case "moonshot-v1-32k" -> MoonshotFirstVersion32k;
                case "moonshot-v1-128k" -> MoonshotFirstVersion128k;
                default -> throw new IllegalArgumentException("Unknown modelCode: " + modelCode);
            };
        }
    }

    public enum Role {
        system, user, assistant
    }


    public interface ChatCompletionsRequest extends KimiChatCompletionsRequestMixin<ChatCompletionsRequest> {
        static ChatCompletionsRequest create() {
            return new ChatCompletionsRequestImpl();
        }

        static ChatCompletionsRequest wrap(JsonObject jsonObject) {
            return new ChatCompletionsRequestImpl(jsonObject);
        }
    }

    public interface Message extends KimiMessageMixin<Message> {
        static Message create() {
            return new KimiMessageImpl();
        }

        static Message wrap(JsonObject jsonObject) {
            return new KimiMessageImpl(jsonObject);
        }
    }

    public interface ToolDefinition extends KimiToolDefinitionMixin<ToolDefinition> {


        static ToolDefinition create() {
            return new KimiToolDefinitionImpl();
        }

        static ToolDefinition wrap(JsonObject jsonObject) {
            return new KimiToolDefinitionImpl(jsonObject);
        }

        static FunctionToolDefinition createFunction() {
            return FunctionToolDefinition.create();
        }
    }

    public interface FunctionToolDefinition extends KimiFunctionToolDefinitionMixin<FunctionToolDefinition> {
        static FunctionToolDefinition create() {
            return new KimiFunctionToolDefinitionImpl();
        }

        static FunctionToolDefinition wrap(JsonObject jsonObject) {
            return new KimiFunctionToolDefinitionImpl(jsonObject);
        }

        static Builder builder() {
            return new KimiFunctionToolDefinitionImpl.Builder();
        }

        interface Builder {

            Builder functionName(String functionName);

            Builder functionDescription(String functionDescription);

            Builder propertyAsString(String name, String desc);

            Builder propertyAsInt(String name, String desc);

            Builder propertyAsNumber(String name, String desc);

            Builder propertyAsBoolean(String name, String desc);

            KimiKit.FunctionToolDefinition build();
        }
    }

    public interface ChatCompletionsResponse extends KimiChatCompletionsResponseMixin {

        static ChatCompletionsResponse wrap(JsonObject jsonObject) {
            return new ChatCompletionsResponseImpl(jsonObject);
        }
    }
}
