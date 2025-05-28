package io.github.sinri.AiOnHttpMix.mirage;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.mix.chat.request.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.chat.response.MixChatResponse;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.GPTKit;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.provider.volces.VolcesKit;
import io.github.sinri.AiOnHttpMix.utils.ServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.specification.DashscopeModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.GPTModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.VolcesModelSpecification;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;

import java.util.function.Function;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

/**
 * MirageKit 是与 Mirage 服务进行交互的核心工具类，
 * 主要用于封装与 Mirage 服务的同步和流式对话请求，
 * 并根据不同的 ChatModel 适配响应数据的解析与处理。
 * <p>
 * 该类支持 NyaCode 编码传输以绕过防火墙，
 * 并通过校验码机制保障请求安全。
 * <p>
 * 典型用法：
 * 
 * <pre>
 *     MirageKit kit = new MirageKit(config);
 *     kit.requestSync(...);
 *     kit.requestStream(...);
 * </pre>
 */
public class MirageKit {
    /**
     * Mirage 服务域名（不含协议头）。
     */
    private final String mirageDomain;
    /**
     * 客户端标识码，用于身份认证。
     */
    private final String clientCode;
    /**
     * 客户端密钥，用于校验请求。
     */
    private final String clientSecret;

    private MirageKit(String mirageDomain, String clientCode, String clientSecret) {
        this.mirageDomain = mirageDomain;
        this.clientCode = clientCode;
        this.clientSecret = clientSecret;
    }

    /**
     * 通过完整参数构造 MirageKit 实例。
     *
     * @param mirageDomain Mirage 服务域名
     * @param clientCode   客户端标识码
     * @param clientSecret 客户端密钥
     */
    public MirageKit(MirageConfigElement configElement) {
        this(configElement.getDomain(), configElement.getClientCode(), configElement.getClientSecret());
    }

    /**
     * 根据指定的 ChatModel 类型，获取将 SSE 原始 Fragment 字符串
     * 转换为标准 Chunk JsonObject 的异步转换器。
     *
     * @param chatModel 聊天模型规范
     * @return fragment to JsonObject 的异步转换函数
     * @throws IllegalArgumentException 不支持的模型类型
     */
    private static Function<String, Future<JsonObject>> getTransformerOfFragmentToDataForChatModel(
            ChatModel chatModel) {
        Function<String, Future<JsonObject>> transformer;
        if (chatModel instanceof GPTModelSpecification) {
            transformer = fragment -> {
                return GPTKit.parseStreamFragmentToChunk(fragment)
                        .compose(chunk -> {
                            return Future.succeededFuture(chunk.cloneAsJsonObject());
                        });
            };
        } else if (chatModel instanceof VolcesModelSpecification) {
            transformer = fragment -> {
                return VolcesKit.parseStreamFragmentToChunk(fragment)
                        .compose(chunk -> {
                            return Future.succeededFuture(chunk.cloneAsJsonObject());
                        });
            };
        } else if (chatModel instanceof DashscopeModelSpecification) {
            transformer = fragment -> {
                return QwenKit.parseStreamFragmentToChunk(fragment)
                        .compose(chunk -> {
                            return Future.succeededFuture(chunk.cloneAsJsonObject());
                        });
            };
        } else {
            throw new IllegalArgumentException("model is not supported");
        }
        return transformer;
    }

    /**
     * 获取 Mirage 服务域名。
     *
     * @return Mirage 域名字符串
     */
    public String getMirageDomain() {
        return mirageDomain;
    }

    /**
     * 构建请求体 JsonObject。
     *
     * @param useNyaCode     是否使用 NyaCode 编码
     * @param mixChatRequest MixChatRequest 对象
     * @return 构建好的请求体 JsonObject
     */
    protected JsonObject buildRequestBody(boolean useNyaCode, MixChatRequest mixChatRequest) {
        return buildRequestBody(useNyaCode, mixChatRequest.toJsonObject());
    }

    /**
     * 构建请求体 JsonObject（内部实现，支持原始 JsonObject 传入）。
     *
     * @param useNyaCode        是否使用 NyaCode 编码
     * @param rawMixChatRequest 原始 MixChatRequest 的 JsonObject 表达
     * @return 构建好的请求体 JsonObject
     */
    private JsonObject buildRequestBody(boolean useNyaCode, JsonObject rawMixChatRequest) {
        var timestamp = System.currentTimeMillis();
        String checksum = Keel.digestHelper().md5(clientCode + "@" + timestamp + "@" + clientSecret);

        JsonObject body = new JsonObject()
                .put("client_code", clientCode)
                .put("timestamp", timestamp)
                .put("checksum", checksum);

        body.put("use_nyacode", useNyaCode);
        if (useNyaCode) {
            body.put("request", Keel.stringHelper().encodeToNyaCode(rawMixChatRequest.toString()));
        } else {
            body.put("request", rawMixChatRequest);
        }
        return body;
    }

    /**
     * 以同步方式向 Mirage 服务发起对话请求。
     *
     * @param useNyaCode     是否使用 NyaCode 编码
     * @param mixChatRequest MixChatRequest 对象
     */
    public Future<MixChatResponse> requestSync(boolean useNyaCode, MixChatRequest mixChatRequest) {
        var body = buildRequestBody(useNyaCode, mixChatRequest);
        return Keel.useWebClient(webClient -> {
            var url = "https://" + getMirageDomain() + "/mirage/aigc/mix/sync";

            AigcMix.getVerboseLogger().info("MirageSDK.requestSync POST " + url + "\n" + body);

            return webClient.postAbs(url)
                    .sendJsonObject(body)
                    .compose(bufferHttpResponse -> {
                        if (bufferHttpResponse.statusCode() != 200) {
                            return Future.failedFuture(new Exception("Status Code:" + bufferHttpResponse.statusCode()
                                    + "; " + bufferHttpResponse.bodyAsString()));
                        }

                        AigcMix.getVerboseLogger()
                                .info("MirageSDK.requestSync::bufferHttpResponse | "
                                        + bufferHttpResponse.bodyAsString());

                        var resp = bufferHttpResponse.bodyAsJsonObject();
                        MixChatResponse mixChatResponse = MixChatResponse.wrap(resp.getJsonObject("data"));
                        return Future.succeededFuture(mixChatResponse);
                    });
        });
    }

    /**
     * 以流式方式向 Mirage 服务发起对话请求。
     *
     * @param useNyaCode            是否使用 NyaCode 编码
     * @param mixChatRequest        MixChatRequest 对象，包含模型与元数据
     * @param fragmentDataProcessor 片段数据处理器，入参为片段 Json 字符串，返回 Future<Void>
     */
    public Future<Void> requestStream(boolean useNyaCode, MixChatRequest mixChatRequest,
            Function<String, Future<Void>> fragmentDataProcessor) {
        var body = buildRequestBody(useNyaCode, mixChatRequest);

        ChatModel chatModel = mixChatRequest.getChatModel();
        Function<String, Future<JsonObject>> transformerOfFragmentToData = getTransformerOfFragmentToDataForChatModel(
                chatModel);

        return ServiceAdapter.callStreamWithCutter(
                new HttpClientOptions()
                        .setKeepAlive(true)
                        .setSsl(true)
                        .setDefaultHost(getMirageDomain())
                        .setDefaultPort(443),
                client -> client
                        .request(HttpMethod.POST, "/mirage/aigc/mix/stream")
                        .compose(httpClientRequest -> {
                            httpClientRequest.putHeader("Content-Type", "application/json");
                            return httpClientRequest
                                    .send(body.toString());
                        }),
                fragment -> {
                    return transformerOfFragmentToData.apply(fragment)
                            .compose(jsonObject -> {
                                return fragmentDataProcessor.apply(jsonObject.toString());
                            });
                },
                mixChatRequest.getTimeout());
    }
}
