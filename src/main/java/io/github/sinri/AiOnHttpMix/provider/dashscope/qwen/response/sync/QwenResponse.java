package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.sync;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;
/**
 * @since 2.0.0
 */
public interface QwenResponse extends UnmodifiableJsonifiableEntity {
    static QwenResponse wrap(JsonObject rawResponse) {
        return new QwenResponseImpl(rawResponse);
    }

    /**
     * 本次请求的状态码。
     *
     * @return 200 表示请求成功，否则表示请求失败。
     */
    default Integer getStatusCode() {
        return readInteger("status_code");
    }

    /**
     * @return 本次调用的唯一标识符。
     */
    default String getRequestId() {
        return readString("request_id");
    }

    /**
     * @return 错误码，调用成功时为空值。
     */
    default String getCode() {
        return readString("code");
    }

    /**
     * @return 调用结果信息。
     */
    default QwenResponseOutput getOutput() {
        return QwenResponseOutput.wrap(readJsonObject("output"));
    }

    /**
     * @return 本次chat请求使用的Token信息。
     */
    default JsonObject getUsage() {
        return readJsonObject("usage");
    }

}
