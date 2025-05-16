package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.request;

import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.request.input.QwenRequestInput;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.request.parameters.QwenRequestParameters;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

/**
 * 以 DashScope 的方式调用通义千问API的请求体。
 *
 * @see <a href=
 *         "https://help.aliyun.com/zh/model-studio/use-qwen-by-calling-api">通义千问API参考</a>
 */
public interface QwenRequest extends JsonifiableEntity<QwenRequest> {
    static QwenRequest create() {
        return new QwenRequestImpl();
    }

    static QwenRequest wrap(JsonObject jsonObject) {
        return new QwenRequestImpl(jsonObject);
    }

    default QwenRequest model(String model) {
        return this.write("model", model);
    }

    default String model() {
        return readString("model");
    }

    default QwenRequest input(QwenRequestInput input) {
        return this.write("input", input.toJsonObject());
    }

    default QwenRequest input(Handler<QwenRequestInput> inputHandler) {
        QwenRequestInput x = QwenRequestInput.create();
        inputHandler.handle(x);
        return input(x);
    }

    default QwenRequestInput input() {
        return QwenRequestInput.wrap(readJsonObject("input"));
    }

    default QwenRequest parameters(QwenRequestParameters parameters) {
        return this.write("parameters", parameters.toJsonObject());
    }

    default QwenRequest parameters(Handler<QwenRequestParameters> parametersHandler) {
        QwenRequestParameters x = QwenRequestParameters.create();
        parametersHandler.handle(x);
        return parameters(x);
    }

    default QwenRequestParameters parameters() {
        return QwenRequestParameters.wrap(readJsonObject("parameters"));
    }
}
