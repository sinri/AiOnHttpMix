package io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.txt;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.response.ChatMessageResponseImpl;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface QwenChatMessageResponseMixin extends ChatResponseBase {
    static QwenKit.ChatMessageResponse wrap(JsonObject jsonObject) {
        return new ChatMessageResponseImpl(jsonObject);
    }

    OutputForMessageResponse getOutput();

    interface OutputForMessageResponse extends UnmodifiableJsonifiableEntity {

        /**
         * 当result_format设置为message时返回该字段。
         */
        @Nullable
        List<OutputForMessageResponse.Choice> getChoices();

        interface Choice extends UnmodifiableJsonifiableEntity {
            static Choice wrap(JsonObject jsonObject) {
                return new ChatMessageResponseImpl.ChoiceImpl(jsonObject);
            }

            /**
             * 有三种情况：
             * 正在生成时为null，
             * 生成结束时如果由于停止token导致则为stop，
             * 生成结束时如果因为生成长度过长导致则为length。
             */
            @Nullable
            default String getFinishReason() {
                return readString("finish_reason");
            }

            default QwenKit.Message getMessage() {
                return QwenKit.Message.wrap(readJsonObject("message"));
            }
        }
    }
}
