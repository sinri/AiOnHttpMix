package io.github.sinri.AiOnHttpMix;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.message.AssistantMessage;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.request.OpenAIChatGptRequest;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponse;
import io.github.sinri.AiOnHttpMix.azure.openai.core.AzureOpenAIServiceMeta;
import io.github.sinri.AiOnHttpMix.azure.openai.dalle.Dalle3Kit;
import io.github.sinri.AiOnHttpMix.azure.openai.dalle.v3.Dalle3Parameters;
import io.github.sinri.AiOnHttpMix.azure.openai.dalle.v3.Dalle3Response;
import io.github.sinri.AiOnHttpMix.dashscope.core.DashscopeServiceMeta;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.response.QwenResponseInMessageFormat;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.vl.QwenVLRequest;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.vl.QwenVLResponse;
import io.github.sinri.AiOnHttpMix.volces.core.VolcesServiceMeta;
import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.github.sinri.AiOnHttpMix.volces.v3.request.VolcesChatRequest;
import io.github.sinri.AiOnHttpMix.volces.v3.response.VolcesChatResponse;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public class AigcMix {
    private static ChatGPTKit chatGPTKit;
    private static Dalle3Kit dalle3Kit;
    private static QwenKit qwenKit;
    private static VolcesKit volcesKit;

    public static ChatGPTKit getChatGPTKit() {
        if (chatGPTKit == null) {
            chatGPTKit = new ChatGPTKit();
        }
        return chatGPTKit;
    }

    public static Dalle3Kit getDalle3Kit() {
        if (dalle3Kit == null) {
            dalle3Kit = new Dalle3Kit();
        }
        return dalle3Kit;
    }

    public static QwenKit getQwenKit() {
        if (qwenKit == null) {
            qwenKit = new QwenKit();
        }
        return qwenKit;
    }

    public static VolcesKit getVolcesKit() {
        if (volcesKit == null) {
            volcesKit = new VolcesKit();
        }
        return volcesKit;
    }

    public static Future<OpenAIChatGptResponse> chatGPT(
            AzureOpenAIServiceMeta serviceMeta,
            Handler<OpenAIChatGptRequest> handler,
            String requestId
    ) {
        return getChatGPTKit().chat(serviceMeta, handler, requestId);
    }

    public static Future<AssistantMessage> chatGPTStream(
            AzureOpenAIServiceMeta serviceMeta,
            Handler<OpenAIChatGptRequest> handler,
            String requestId
    ) {
        return getChatGPTKit().chatStream(serviceMeta, handler, requestId);
    }

    public static Future<Dalle3Response> dalle3(
            AzureOpenAIServiceMeta serviceMeta,
            Handler<Dalle3Parameters> parametersHandler,
            String requestId
    ) {
        return getDalle3Kit().draw(serviceMeta, parametersHandler, requestId);
    }

    public static Future<QwenResponseInMessageFormat> qwen(
            DashscopeServiceMeta serviceMeta,
            Handler<QwenRequest> chatRequestHandler,
            String requestId
    ) {
        return getQwenKit().chatForMessageResponse(serviceMeta, chatRequestHandler, requestId);
    }

    public static Future<QwenResponseInMessageFormat> qwenStream(
            DashscopeServiceMeta serviceMeta,
            Handler<QwenRequest> chatRequestHandler,
            String requestId
    ) {
        return getQwenKit().chatStreamWithBuffer(serviceMeta, chatRequestHandler, requestId);
    }

    public static Future<QwenVLResponse> qwenVL(
            DashscopeServiceMeta serviceMeta,
            Handler<QwenVLRequest> chatRequestHandler,
            String requestId
    ) {
        return getQwenKit().chatVL(serviceMeta, chatRequestHandler, requestId);
    }

    public static Future<QwenVLResponse> qwenVLStream(
            DashscopeServiceMeta serviceMeta,
            Handler<QwenVLRequest> chatRequestHandler,
            String requestId
    ) {
        return getQwenKit().chatVLStreamWithBuffer(serviceMeta, chatRequestHandler, requestId);
    }

    public static Future<VolcesChatResponse> volces(
            VolcesServiceMeta serviceMeta,
            Handler<VolcesChatRequest> requestBodyHandler,
            String requestId
    ) {
        return getVolcesKit().chat(serviceMeta, requestBodyHandler, requestId);
    }

    public static Future<VolcesChatResponse> volcesStream(
            VolcesServiceMeta serviceMeta,
            Handler<VolcesChatRequest> requestBodyHandler,
            String requestId
    ) {
        return getVolcesKit().chatStreamWithBuffer(serviceMeta, requestBodyHandler, requestId);
    }
}