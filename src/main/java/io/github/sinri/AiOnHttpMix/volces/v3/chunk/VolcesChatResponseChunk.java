package io.github.sinri.AiOnHttpMix.volces.v3.chunk;

import io.github.sinri.AiOnHttpMix.volces.v3.VolcesChatRole;
import io.github.sinri.AiOnHttpMix.volces.v3.response.VolcesChatUsage;
import io.github.sinri.AiOnHttpMix.volces.v3.tool.SharedFunctionCall;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface VolcesChatResponseChunk extends UnmodifiableJsonifiableEntity {
    static VolcesChatResponseChunk wrap(JsonObject jsonObject) {
        return new VolcesChatResponseChunkImpl(jsonObject);
    }
    /**
     * @return 一次 chat completion 接口调用的唯一标识。
     */
    default String getId() {
        return readString("id");
    }

    /**
     * @return 本次对话生成时间戳（秒）。
     */
    default Integer getCreated() {
        return readInteger("created");
    }

    /**
     * @return 实际使用的模型名称和版本。
     */
    default String getModel() {
        return readString("model");
    }

    /**
     * @return 固定为 chat.completion。
     */
    default String getObject() {
        return readString("object");
    }

    @Nullable
    default VolcesChatUsage getUsage() {
        JsonObject entries = readJsonObject("usage");
        if (entries == null) {
            return null;
        }
        return VolcesChatUsage.wrap(entries);
    }

    @Nullable
    default List<StreamChoice> getChoices() {
        List<JsonObject> array = readJsonObjectArray("choices");
        if (array == null) return null;
        List<StreamChoice> result = new ArrayList<>();
        array.forEach(x -> {
            StreamChoice wrap = StreamChoice.wrap(x);
            result.add(wrap);
        });
        return result;
    }

    interface StreamChoice extends UnmodifiableJsonifiableEntity {
        static StreamChoice wrap(JsonObject object) {
            return new VolcesChatResponseChunkImpl.StreamChoiceImpl(object);
        }

        /**
         * @return 当前元素在 choices 列表的索引
         */
        default Integer getIndex() {
            return readInteger("index");
        }

        /**
         * 可能的值包括：
         * stop：模型输出自然结束，或因命中请求参数 stop 中指定的字段而被截断
         * length：模型输出因达到请求参数 max_token 指定的最大 token 数量而被截断
         * content_filter：模型输出被内容审核拦截
         * tool_calls：模型调用了工具
         *
         * @return 模型停止生成 token 的原因。
         */
        default String getFinishReason() {
            return readString("finish_reason");
        }

        default ChoiceDelta getDelta() {
            return ChoiceDelta.wrap(readJsonObject("delta"));
        }

        // logprobs 当前内容的对数概率信息
    }

    interface ChoiceDelta extends UnmodifiableJsonifiableEntity {
        static ChoiceDelta wrap(JsonObject object) {
            return new VolcesChatResponseChunkImpl.ChoiceDeltaImpl(object);
        }

        default VolcesChatRole getRole() {
            return VolcesChatRole.valueOf(readString("role"));
        }

        default String getContent() {
            return readString("content");
        }

        default List<ChoiceDeltaToolCall> getToolCalls() {
            List<JsonObject> array = readJsonObjectArray("tool_calls");
            if (array == null) return null;
            List<ChoiceDeltaToolCall> list = new ArrayList<>();
            array.forEach(x -> {
                ChoiceDeltaToolCall wrap = ChoiceDeltaToolCall.wrap(x);
                list.add(wrap);
            });
            return list;
        }
    }

    interface ChoiceDeltaToolCall extends UnmodifiableJsonifiableEntity {
        static ChoiceDeltaToolCall wrap(JsonObject object) {
            return new VolcesChatResponseChunkImpl.ChoiceDeltaToolCallImpl(object);
        }

        /**
         * @return 当前元素在 tool_calls 列表的索引
         */
        default Integer getIndex() {
            return readInteger("index");
        }

        default String getId() {
            return readString("id");
        }

        /**
         * @return 工具类型，当前仅支持function
         */
        default String getType() {
            return readString("type");
        }

        default FunctionCallChunk getFunction() {
            return FunctionCallChunk.wrap(readJsonObject("function"));
        }
    }

    interface FunctionCallChunk extends SharedFunctionCall {
        static FunctionCallChunk wrap(JsonObject object) {
            return new VolcesChatResponseChunkImpl.FunctionCallChunkImpl(object);
        }
    }
}
