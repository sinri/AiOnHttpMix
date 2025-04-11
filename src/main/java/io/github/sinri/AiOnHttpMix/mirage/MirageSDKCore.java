package io.github.sinri.AiOnHttpMix.mirage;

import io.github.sinri.AiOnHttpMix.mirage.chat.MirageRequestEntity;
import io.github.sinri.AiOnHttpMix.mirage.vl.MirageVLRequestEntity;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.vertx.core.json.JsonObject;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class MirageSDKCore {
    private final String mirageDomain;
    private final String clientCode;
    private final String clientSecret;

    public MirageSDKCore(String mirageDomain, String clientCode, String clientSecret) {
        this.mirageDomain = mirageDomain;
        this.clientCode = clientCode;
        this.clientSecret = clientSecret;
    }

    public String getMirageDomain() {
        return mirageDomain;
    }

    protected JsonObject buildRequestBody(
            String model,
            String service,
            boolean useNyaCode,
            MirageRequestEntity llmRequestBody
    ) {
        return buildRequestBody(model, service, useNyaCode, llmRequestBody.toJsonObject());
    }

    protected JsonObject buildRequestBody(
            String model,
            String service,
            boolean useNyaCode,
            MirageVLRequestEntity vllmRequestBody
    ) {
        return buildRequestBody(model, service, useNyaCode, vllmRequestBody.toJsonObject());
    }


    /**
     * @param model      模型的定义，一般约定使用 {@link SupportedModel#name()} 。
     * @param service    对应模型定义在Mirage服务内提供的服务；当同一个模型有不同部署时，通过服务区分。
     * @param useNyaCode 传输内容是否使用NyaCode编码绕过防火墙。
     */
    private JsonObject buildRequestBody(
            String model,
            String service,
            boolean useNyaCode,
            JsonObject llmRequestBody
    ) {
        var timestamp = System.currentTimeMillis();
        String checksum = Keel.digestHelper().md5(clientCode + "@" + timestamp + "@" + clientSecret);

        JsonObject body = new JsonObject()
                .put("client_code", clientCode)
                .put("timestamp", timestamp)
                .put("checksum", checksum);

        body.put("model", model);
        body.put("service", service);
        body.put("use_nyacode", useNyaCode);
        if (useNyaCode) {
            body.put("request", Keel.stringHelper().encodeToNyaCode(llmRequestBody.toString()));
        } else {
            body.put("request", llmRequestBody);
        }
        return body;
    }
}
