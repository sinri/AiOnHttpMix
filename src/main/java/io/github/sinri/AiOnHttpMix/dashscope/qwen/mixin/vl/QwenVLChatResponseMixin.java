package io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.vl;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.vl.VLChatResponseImpl;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface QwenVLChatResponseMixin extends UnmodifiableJsonifiableEntity {
    // output
    interface Output extends UnmodifiableJsonifiableEntity {
        static Output wrap(JsonObject json) {
            return new VLChatResponseImpl.OutputImpl(json);
        }

        default List<Choice> getChoices() {
            List<JsonObject> choices = readJsonObjectArray("choices");
            if (choices == null) return null;
            return choices.stream().map(Choice::wrap).toList();
        }
    }

    interface Choice extends UnmodifiableJsonifiableEntity {
        static Choice wrap(JsonObject json) {
            return new VLChatResponseImpl.ChoiceImpl(json);
        }

        default QwenKit.VLChatOutputMessage getMessage() {
            JsonObject message = readJsonObject("message");
            if (message == null) return null;
            return QwenKit.VLChatOutputMessage.wrap(message);
        }

        default String getFinishReason() {
            return readString("finish_reason");
        }
    }

    default Output getOutput() {
        JsonObject x = readJsonObject("output");
        if (x == null) return null;
        return Output.wrap(x);
    }

    default QwenKit.VLChatUsage getUsage() {
        JsonObject usage = readJsonObject("usage");
        if (usage == null) return null;
        return QwenKit.VLChatUsage.wrap(usage);
    }

    /**
     * @return 本次请求的系统唯一码
     */
    default String getRequestId() {
        return readString("request_id");
    }
}
