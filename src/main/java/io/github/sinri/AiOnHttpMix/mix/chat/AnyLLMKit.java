package io.github.sinri.AiOnHttpMix.mix.chat;

import io.github.sinri.AiOnHttpMix.mix.FunctionCallAdapter;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @since 1.1.0
 *         As of 1.1.12, add Pure Chat DeepSeek on Volces.
 *         As of 1.3.0 use service adapter.
 */
public class AnyLLMKit implements AnyLLMServiceAdapter {
    /**
     * @since 1.1.2
     */
    private final Map<String, FunctionCallAdapter> fcMap = new HashMap<>();
    /**
     * @since 1.3.0
     */
    private final AnyLLMServiceAdapter serviceAdapter;

    public AnyLLMKit(@NotNull AnyLLMServiceAdapter serviceAdapter) {
        this.serviceAdapter = serviceAdapter;
    }

    /**
     * @since 1.3.0
     */
    public AnyLLMServiceAdapter getServiceAdapter() {
        return serviceAdapter;
    }

    /**
     * @since 1.1.2
     */
    public AnyLLMKit registerFunction(@NotNull FunctionCallAdapter functionCallAdapter) {
        fcMap.put(functionCallAdapter.getFunctionName(), functionCallAdapter);
        return this;
    }

    /**
     * @since 1.1.2
     */
    public AnyLLMKit unregisterFunction(@NotNull String functionName) {
        fcMap.remove(functionName);
        return this;
    }

    /**
     * @since 1.1.2
     */
    public AnyLLMKit unregisterAllFunctions() {
        fcMap.clear();
        return this;
    }

    /**
     * @since 1.1.2
     */
    public @Nullable FunctionCallAdapter getRegisteredFunction(@NotNull String functionName) {
        return fcMap.get(functionName);
    }

    /**
     * @param anyLLMResponseToolFunctionCall as of 1.2.6, it should not be null.
     * @param fixedArgument                  as of 1.2.6, a new nullable fixed argument is added to this method to fit
     *                                       various context.
     * @since 1.1.2
     */
    public Future<String> callRegisterFunction(
            @NotNull AnyLLMResponseToolFunctionCall anyLLMResponseToolFunctionCall,
            @Nullable JsonObject fixedArgument
    ) {
        FunctionCallAdapter registeredFunction =
                this.getRegisteredFunction(anyLLMResponseToolFunctionCall.getFunctionName());
        if (registeredFunction == null) {
            return Future.failedFuture(new UnsupportedOperationException("Function not registered"));
        }
        String functionArguments = anyLLMResponseToolFunctionCall.getFunctionArguments();
        JsonObject argsAsJsonObject;
        try {
            argsAsJsonObject = new JsonObject(functionArguments);
        } catch (Throwable e) {
            argsAsJsonObject = null;
        }
        return registeredFunction.callFunction(argsAsJsonObject, fixedArgument);
    }

    public Future<AnyLLMResponse> request(Handler<AnyLLMRequest> requestHandler) {
        AnyLLMRequest anyLLMRequest = AnyLLMRequest.create();
        requestHandler.handle(anyLLMRequest);
        return request(anyLLMRequest);
    }

    public Future<AnyLLMResponse> request(AnyLLMRequest request) {
        return this.getServiceAdapter().request(request);
    }

    /**
     * @param request         请求
     * @param fragmentHandler 针对一个已经格式化好的SSE Chunk的JSON对象字符串表达的处理器
     * @since 1.1.3
     */
    public Future<Void> request(AnyLLMRequest request, Handler<String> fragmentHandler) {
        return getServiceAdapter().request(request, fragmentHandler);
    }

    @Override
    public Future<AnyLLMResponse> requestWithStreamBuffer(AnyLLMRequest request) {
        return getServiceAdapter().requestWithStreamBuffer(request);
    }
}