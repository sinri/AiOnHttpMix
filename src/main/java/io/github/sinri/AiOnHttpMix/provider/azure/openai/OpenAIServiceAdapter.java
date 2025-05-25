package io.github.sinri.AiOnHttpMix.provider.azure.openai;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.utils.AbnormalResponse;
import io.github.sinri.AiOnHttpMix.utils.ServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

/**
 * Azure OpenAI ChatGPT 服务适配器。
 * <p>
 * 封装了与 Azure OpenAI ChatGPT 服务的 API 交互，包括普通请求和流式请求。
 * 主要用于统一模型服务的调用方式。
 * </p>
 *
 * @author sinri
 * @since 2.0.0
 */
public abstract class OpenAIServiceAdapter implements ServiceAdapter {

    private final Map<String, OpenAIConfigElement> deploymentConfigMap;

    public OpenAIServiceAdapter(Map<String, OpenAIConfigElement> deploymentConfigMap) {
        this.deploymentConfigMap = deploymentConfigMap;
    }

    @Nonnull
    private OpenAIConfigElement getConfig(ChatModel chatModel) {
        OpenAIConfigElement openAIConfigElement = deploymentConfigMap.get(chatModel.getModelName());
        Objects.requireNonNull(openAIConfigElement);
        return openAIConfigElement;
    }

    /**
     * 生成 Azure OpenAI 服务主机名。
     *
     * @return 主机名
     */
    private String generateHost(OpenAIConfigElement configElement) {
        return configElement.getResourceName() + ".openai.azure.com";
    }

    /**
     * 生成 API 路径。
     *
     * @param api API 路径（以 / 开头）
     * @return 完整 URI
     */
    private String generateUri(OpenAIConfigElement configElement, String api) {
        return "/openai/deployments/" + configElement.getDeployment() + api + "?api-version=" + configElement.getApiVersion();
    }

    /**
     * 生成完整请求 URL。
     *
     * @param api API 路径（以 / 开头）
     * @return 完整 URL
     */
    private String generateUrl(OpenAIConfigElement configElement, String api) {
        return "https://" + generateHost(configElement) + generateUri(configElement, api);
    }

    /**
     * 发起 ChatGPT 聊天补全请求。
     *
     * @param chatModel      聊天模型
     * @param requestPayload 请求体
     * @param requestId      请求 ID
     */
    @Override
    public Future<JsonObject> request(ChatModel chatModel, JsonObject requestPayload, String requestId) {
        assertModelCompatible(chatModel);

        OpenAIConfigElement configElement = this.getConfig(chatModel);

        var url = generateUrl(configElement, "/chat/completions");
        AigcMix.getVerboseLogger().info(x -> x
                .message("Start AzureOpenAIServiceMeta.request")
                .context(
                        j -> j
                                .put("api", url)
                                .put("input", requestPayload)
                                .put("requestId", requestId)
                )
        );

        return Keel.useWebClient(webClient -> webClient
                .postAbs(url)
                .putHeader("Content-Type", "application/json")
                .putHeader("api-key", configElement.getApiKey())
                .sendJsonObject(requestPayload)
                .compose(bufferHttpResponse -> {
                    JsonObject entries = bufferHttpResponse.bodyAsJsonObject();
                    if (bufferHttpResponse.statusCode() != 200 || entries == null) {
                        throw new AbnormalResponse(bufferHttpResponse);
                    }
                    AigcMix.getVerboseLogger().info(x -> x
                            .message("bufferHttpResponse in AzureOpenAIServiceMeta.request")
                            .context(j -> j
                                    .put("output", entries)
                                    .put("requestId", requestId)
                            )
                    );
                    return Future.succeededFuture(entries);
                }));
    }

    /**
     * 发起流式聊天补全请求。
     *
     * @param chatModel         聊天模型
     * @param requestPayload    请求参数
     * @param cutterProcessFunc SSE 数据处理函数
     * @param cutterTimeout     超时时间（毫秒）
     * @param requestId         请求 ID
     */
    @Override
    public Future<Void> requestStream(ChatModel chatModel, JsonObject requestPayload, Function<String, Future<Void>> cutterProcessFunc, long cutterTimeout, String requestId) {
        assertModelCompatible(chatModel);
        OpenAIConfigElement configElement = this.getConfig(chatModel);
        var uri = generateUri(configElement, "/chat/completions");
        return ServiceAdapter.callStreamWithCutter(
                new HttpClientOptions()
                        .setKeepAlive(true)
                        .setSsl(true)
                        .setDefaultHost(generateHost(configElement))
                        .setDefaultPort(443),
                client -> client.request(HttpMethod.POST, uri)
                                .compose(httpClientRequest -> {
                                    httpClientRequest
                                            .putHeader("Content-Type", "application/json")
                                            .putHeader("api-key", configElement.getApiKey());
                                    return httpClientRequest
                                            .send(requestPayload.toString());
                                }),
                chunk -> {
                    AigcMix.getVerboseLogger()
                           .info("sse chunk", c -> c.put("chunk", chunk).put("request_id", requestId));
                    return cutterProcessFunc.apply(chunk);
                },
                cutterTimeout
        );
    }

}
