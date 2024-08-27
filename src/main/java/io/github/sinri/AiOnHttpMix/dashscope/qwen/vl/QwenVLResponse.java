package io.github.sinri.AiOnHttpMix.dashscope.qwen.vl;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface QwenVLResponse extends UnmodifiableJsonifiableEntity {
    static QwenVLResponse wrap(JsonObject jsonObject) {
        return new QwenVLResponseImpl(jsonObject);
    }

    default QwenVLUsage getUsage() {
        JsonObject usage = readJsonObject("usage");
        if (usage == null) return null;
        return QwenVLUsage.wrap(usage);
    }

    interface Output extends UnmodifiableJsonifiableEntity {
        static Output wrap(JsonObject json) {
            return new QwenVLResponseImpl.OutputImpl(json);
        }

        default List<Choice> getChoices() {
            List<JsonObject> choices = readJsonObjectArray("choices");
            if (choices == null) return null;
            return choices.stream().map(Choice::wrap).toList();
        }
    }

    default Output getOutput() {
        JsonObject x = readJsonObject("output");
        if (x == null) return null;
        return Output.wrap(x);
    }

    interface Choice extends UnmodifiableJsonifiableEntity {
        static Choice wrap(JsonObject json) {
            return new QwenVLResponseImpl.ChoiceImpl(json);
        }

        default QwenVLOutputMessage getMessage() {
            JsonObject message = readJsonObject("message");
            if (message == null) return null;
            return QwenVLOutputMessage.wrap(message);
        }

        default String getFinishReason() {
            return readString("finish_reason");
        }
    }

    /**
     * @return 本次请求的系统唯一码
     */
    default String getRequestId() {
        return readString("request_id");
    }
}
