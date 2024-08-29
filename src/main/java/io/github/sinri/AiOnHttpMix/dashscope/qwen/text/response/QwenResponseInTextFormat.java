package io.github.sinri.AiOnHttpMix.dashscope.qwen.text.response;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.QwenResponseBase;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

public interface QwenResponseInTextFormat extends QwenResponseBase {

    static QwenResponseInTextFormat wrap(int statusCode, JsonObject jsonObject) {
        return new QwenResponseInTextFormatImpl(statusCode, jsonObject);
    }

    OutputForTextResponse getOutput();

    interface OutputForTextResponse extends UnmodifiableJsonifiableEntity {
        /**
         * 当result_format设置为text时返回该字段。
         *
         * @return 模型输出的内容。
         */
        @Nullable
        default String getText() {
            return readString("text");
        }

        /**
         * 有三种情况：
         * 正在生成时为null，
         * 生成结束时如果由于停止token导致则为stop，
         * 生成结束时如果因为生成长度过长导致则为length。
         * 当result_format设置为text时返回该字段。
         */
        @Nullable
        default String getFinishReason() {
            return readString("finish_reason");
        }
    }
}
