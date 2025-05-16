package io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.utils.AbnormalResponse;
import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;
import io.github.sinri.AiOnHttpMix.utils.series.ChatModelSeries;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;

import java.util.HashSet;
import java.util.Set;
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
public class ChatGPTServiceAdapter implements ChatModelServiceAdapter {
    private final static Set<ChatModelSeries> supportedChatModelSeriesSet = new HashSet<>();

    static {
        supportedChatModelSeriesSet.add(ChatModelSeries.chatgpt);
    }

    private final String apiKey;
    private final String resourceName;
    private final String deployment;
    private final String apiVersion;

    /**
     * 构造方法。
     *
     * @param apiKey      Azure OpenAI API 密钥
     * @param resourceName Azure 资源名称
     * @param deployment  部署名称
     * @param apiVersion  API 版本号
     */
    public ChatGPTServiceAdapter(String apiKey, String resourceName, String deployment, String apiVersion) {
        this.apiKey = apiKey;
        this.resourceName = resourceName;
        this.deployment = deployment;
        this.apiVersion = apiVersion;
    }

    /**
     * 获取服务提供方。
     *
     * @return ServiceProvider.azureOpenAI
     */
    @Override
    public ServiceProvider getServiceProvider() {
        return ServiceProvider.azureOpenAI;
    }

    /**
     * 获取支持的模型系列。
     *
     * @return 支持的 ChatModelSeries 集合
     */
    @Override
    public Set<ChatModelSeries> getChatModelSeries() {
        return supportedChatModelSeriesSet;
    }

    /**
     * 生成 Azure OpenAI 服务主机名。
     *
     * @return 主机名
     */
    private String generateHost() {
        return resourceName + ".openai.azure.com";
    }

    /**
     * 生成 API 路径。
     *
     * @param api API 路径（以 / 开头）
     * @return 完整 URI
     */
    private String generateUri(String api) {
        return "/openai/deployments/" + deployment + api + "?api-version=" + apiVersion;
    }

    /**
     * 生成完整请求 URL。
     *
     * @param api API 路径（以 / 开头）
     * @return 完整 URL
     */
    private String generateUrl(String api) {
        return "https://" + generateHost() + generateUri(api);
    }

    /**
     * 发起 ChatGPT 聊天补全请求。
     *
     * @param chatModel  聊天模型
     * @param requestBody 请求体
     * @param requestId   请求 ID
     */
    @Override
    public Future<JsonObject> request(ChatModel chatModel, JsonObject requestBody, String requestId) {
        if (!isChatModelSupported(chatModel)) {
            throw new IllegalArgumentException("ChatModel is not supported by this ChatModelServiceAdapter.");
        }

        var url = generateUrl("/chat/completions");
        AigcMix.getVerboseLogger().info(x -> x
                .message("Start AzureOpenAIServiceMeta.request")
                .context(
                        j -> j
                                .put("api", url)
                                .put("input", requestBody)
                                .put("requestId", requestId)
                )
        );

        return Keel.useWebClient(webClient -> {
            return webClient
                    .postAbs(url)
                    .putHeader("Content-Type", "application/json")
                    .putHeader("api-key", apiKey)
                    .sendJsonObject(requestBody)
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
                    });
        });
    }

    /**
     * 发起流式聊天补全请求。
     *
     * @param chatModel         聊天模型
     * @param parameters        请求参数
     * @param cutterProcessFunc SSE 数据处理函数
     * @param cutterTimeout     超时时间（毫秒）
     * @param requestId         请求 ID
     */
    @Override
    public Future<Void> requestStream(ChatModel chatModel, JsonObject parameters, Function<String, Future<Void>> cutterProcessFunc, long cutterTimeout, String requestId) {
        if (!isChatModelSupported(chatModel)) {
            throw new IllegalArgumentException("ChatModel is not supported by this ChatModelServiceAdapter.");
        }

        var uri = generateUri("/chat/completions");
        return ChatModelServiceAdapter.callStreamWithCutter(
                new HttpClientOptions()
                        .setKeepAlive(true)
                        .setSsl(true)
                        .setDefaultHost(generateHost())
                        .setDefaultPort(443),
                client -> client.request(HttpMethod.POST, uri)
                                .compose(httpClientRequest -> {
                                    httpClientRequest
                                            .putHeader("Content-Type", "application/json")
                                            .putHeader("api-key", apiKey);
                                    return httpClientRequest
                                            .send(parameters.toString());
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
