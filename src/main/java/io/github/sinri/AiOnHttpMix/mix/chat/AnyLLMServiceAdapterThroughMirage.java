package io.github.sinri.AiOnHttpMix.mix.chat;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk.OpenAIChatGptStreamBuffer;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.chunk.QwenStreamBuffer;
import io.github.sinri.AiOnHttpMix.deepseek.DeepseekKit;
import io.github.sinri.AiOnHttpMix.deepseek.chat.chunk.DeepseekStreamBuffer;
import io.github.sinri.AiOnHttpMix.mirage.chat.MirageSDK;
import io.github.sinri.AiOnHttpMix.utils.LLMStreamBuffer;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.github.sinri.AiOnHttpMix.volces.v3.chunk.VolcesChatStreamBuffer;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public class AnyLLMServiceAdapterThroughMirage implements AnyLLMServiceAdapter {
    /**
     * Used by both SDK or Mirage.
     */
    private final SupportedModel model;
    /**
     * Used by Mirage only.
     */
    private final MirageSDK mirageSDK;
    /**
     * Used by Mirage only.
     */
    private final String mirageModel;
    /**
     * Used by Mirage only.
     */
    private final String mirageService;

    AnyLLMServiceAdapterThroughMirage(MirageSDK mirageSDK, SupportedModel supportedModel) {
        this.mirageSDK = mirageSDK;
        this.model = supportedModel;
        this.mirageModel = model.name();
        this.mirageService = generateMirageService(supportedModel);
    }

    private String generateMirageService(SupportedModel supportedModel) {
        return switch (model) {
            case ChatGPT -> "gpt-4-o";
            case QwenPlus, QwenMax, QwenTurbo, QwenLong,
                 QwenPlusLatest, QwenMaxLatest, QwenTurboLatest,
                 DeepSeekReasonerOnDashScope, DeepSeekChatOnDashScope -> null;
            case Doubao -> "doubao-pro-128k";
            case KimiOnVolces -> "moonshot-v1-128k";
            case DeepSeekReasonerOnVolces -> "DeepSeek-R1";
            case DeepSeekChatOnVolces -> "DeepSeek-V3";
            case DeepSeekReasoner, DeepSeekChat -> "main";
        };
    }

    @Override
    public Future<AnyLLMResponse> request(AnyLLMRequest request) {
        return this.mirageSDK.requestSync(
                this.mirageModel,
                this.mirageService,
                true,
                request.toMirageRequestEntity()
        );
    }

    @Override
    public Future<Void> request(AnyLLMRequest request, Handler<String> fragmentHandler) {
        return this.mirageSDK.requestStream(
                this.mirageModel,
                this.mirageService,
                true,
                request.toMirageRequestEntity(),
                request.getMaxExecutionSeconds() * 1000L,
                fragmentHandler
        );
    }

    @Override
    public Future<AnyLLMResponse> requestWithStreamBuffer(AnyLLMRequest request) {

        Handler<String> fragmentHandler;
        LLMStreamBuffer buffer;

        fragmentHandler = switch (model) {
            case ChatGPT -> {
                buffer = new OpenAIChatGptStreamBuffer();
                yield ChatGPTKit.getStreamBufferFragmentHandler((OpenAIChatGptStreamBuffer) buffer,
                        request.getRequestId());
            }
            case QwenPlus, QwenMax, QwenTurbo, QwenLong,
                 QwenPlusLatest, QwenMaxLatest, QwenTurboLatest,
                 DeepSeekReasonerOnDashScope, DeepSeekChatOnDashScope -> {
                buffer = new QwenStreamBuffer();
                yield QwenKit.getStreamBufferFragmentHandler((QwenStreamBuffer) buffer,
                        request.getRequestId());
            }
            case Doubao, KimiOnVolces -> {
                buffer = new VolcesChatStreamBuffer();
                yield VolcesKit.getStreamBufferFragmentHandler((VolcesChatStreamBuffer) buffer,
                        request.getRequestId());
            }
            case DeepSeekReasonerOnVolces, DeepSeekChatOnVolces -> {
                buffer = new DeepseekStreamBuffer();
                yield DeepseekKit.getStreamBufferFragmentHandler((DeepseekStreamBuffer) buffer,
                        request.getRequestId());
                //                default:
                //                    throw new UnsupportedOperationException("Not supported!");
            }
            case DeepSeekChat, DeepSeekReasoner -> {
                buffer = new DeepseekStreamBuffer();
                yield DeepseekKit.getStreamBufferFragmentHandler((DeepseekStreamBuffer) buffer,
                        request.getRequestId());
            }
        };

        return this.mirageSDK.requestStream(
                           this.mirageModel,
                           this.mirageService,
                           true,
                           request.toMirageRequestEntity(),
                           request.getMaxExecutionSeconds() * 1000L,
                           s -> {
                               AigcMix.getVerboseLogger()
                                      .debug("io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMKit.requestWithStreamBuffer::component | " + s);
                                /*
                                {"output":{"choices":[{"message":{"content":"筑","role":"assistant"},
                                "finish_reason":"null"}]},"usage":{"total_tokens":58,"input_tokens":54,
                                "output_tokens":4},"request_id":"ab856b62-68aa-928d-ade3-ae7f9312d5ee"}
                                 */
                               fragmentHandler.handle(s);
                           }
                   )
                             .compose(fin -> {
                                 return Future.succeededFuture(buffer.toAnyLLMResponse());
                             });
    }

}
