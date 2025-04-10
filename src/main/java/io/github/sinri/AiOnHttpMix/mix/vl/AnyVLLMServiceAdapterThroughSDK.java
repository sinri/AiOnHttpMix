package io.github.sinri.AiOnHttpMix.mix.vl;

import io.github.sinri.AiOnHttpMix.dashscope.core.DashscopeServiceMeta;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.utils.ServiceMeta;
import io.github.sinri.AiOnHttpMix.utils.SupportedVLModel;
import io.github.sinri.AiOnHttpMix.volces.core.VolcesServiceMeta;
import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.vertx.core.Future;
import io.vertx.core.Handler;

/**
 * @since 1.3.0
 */
public class AnyVLLMServiceAdapterThroughSDK implements AnyVLLMServiceAdapter {

    private final ServiceMeta serviceMeta;
    private final SupportedVLModel vlModel;

    public AnyVLLMServiceAdapterThroughSDK(ServiceMeta serviceMeta, SupportedVLModel vlModel) {
        this.serviceMeta = serviceMeta;
        this.vlModel = vlModel;
    }

    @Override
    public Future<AnyVLLMResponse> request(AnyVLLMRequest request) {
        return switch (vlModel) {
            case QwenVLMax, QwenVLPlus -> new QwenKit()
                    .chatVL(
                            (DashscopeServiceMeta) serviceMeta,
                            request.toQwenRequest()
                                   .setModel(this.vlModel.getMappedModelCode()),
                            request.getRequestId()
                    )
                    .compose(qwenResp -> {
                        AnyVLLMResponse anyVLLMResponse = AnyVLLMResponse.from(qwenResp);
                        return Future.succeededFuture(anyVLLMResponse);
                    });
            case DoubaoVL -> new VolcesKit()
                    .chat((VolcesServiceMeta) this.serviceMeta, request.toVolcesRequest(), request.getRequestId())
                    .compose(volcesResp -> {
                        AnyVLLMResponse anyVLLMResponse = AnyVLLMResponse.from(volcesResp);
                        return Future.succeededFuture(anyVLLMResponse);
                    });
        };
    }

    @Override
    public Future<Void> request(AnyVLLMRequest request, Handler<String> fragmentHandler) {
        return switch (vlModel) {
            case QwenVLMax, QwenVLPlus -> new QwenKit()
                    .chatVLStreamWithStringHandler(
                            (DashscopeServiceMeta) serviceMeta,
                            request.toQwenRequest()
                                   .setModel(this.vlModel.getMappedModelCode())
                                   .toJsonObject(),
                            fragmentHandler,
                            request.getMaxExecutionSeconds(),
                            request.getRequestId()
                    );
            case DoubaoVL -> new VolcesKit()
                    .chatStreamWithStringHandler(
                            (VolcesServiceMeta) this.serviceMeta,
                            request.toVolcesRequest()
                                   .toJsonObject(),
                            fragmentHandler,
                            request.getMaxExecutionSeconds(),
                            request.getRequestId()
                    );
        };
    }

    @Override
    public Future<AnyVLLMResponse> requestWithStreamBuffer(AnyVLLMRequest request) {
        return switch (vlModel) {
            case QwenVLMax, QwenVLPlus -> new QwenKit()
                    .chatVLStreamWithBuffer(
                            (DashscopeServiceMeta) serviceMeta,
                            request.toQwenRequest()
                                   .setModel(this.vlModel.getMappedModelCode()),
                            request.getMaxExecutionSeconds(),
                            request.getRequestId()
                    )
                    .compose(resp -> {
                        AnyVLLMResponse anyVLLMResponse = AnyVLLMResponse.from(resp);
                        return Future.succeededFuture(anyVLLMResponse);
                    });
            case DoubaoVL -> new VolcesKit()
                    .chatStreamWithBuffer(
                            (VolcesServiceMeta) this.serviceMeta,
                            request.toVolcesRequest(),
                            request.getMaxExecutionSeconds(),
                            request.getRequestId()
                    )
                    .compose(resp -> {
                        AnyVLLMResponse anyVLLMResponse = AnyVLLMResponse.from(resp);
                        return Future.succeededFuture(anyVLLMResponse);
                    });
        };
    }
}
