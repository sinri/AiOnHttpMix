package io.github.sinri.AiOnHttpMix.dashscope.qwen.text.response;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.QwenResponseBase;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.message.QwenMessage;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface QwenResponseInMessageFormat extends QwenResponseBase {
    static QwenResponseInMessageFormat wrap(int statusCode, JsonObject jsonObject) {
        return new QwenResponseInMessageFormatImpl(statusCode, jsonObject);
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
                return new QwenResponseInMessageFormatImpl.ChoiceImpl(jsonObject);
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

            default QwenMessage getMessage() {
                return QwenMessage.wrap(readJsonObject("message"));
            }
        }
    }
}
