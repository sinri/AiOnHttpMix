package io.github.sinri.AiOnHttpMix.dashscope.conversation;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Response extends SimpleJsonifiableEntity {
    public Response(int statusCode, JsonObject jsonObject) {
        super(jsonObject);
        this.jsonObject.put("status_code", statusCode);
    }

    public Integer statusCode() {
        return readInteger("status_code");
    }

    public String errorCode() {
        return readString("code");
    }

    public String errorMessage() {
        return readString("message");
    }

    /**
     * @return 本次请求的系统唯一码
     */
    public String requestId() {
        return readString("request_id");
    }

    public OutputForMessageStyle output() {
        return new OutputForMessageStyle(readJsonObject("output"));
    }

    public Usage usage() {
        return new Usage(readJsonObject("usage"));
    }

    /**
     * 入参result_format=text时候的返回值
     */
    @Deprecated
    public static class OutputForTestStyle extends SimpleJsonifiableEntity {
        public OutputForTestStyle(JsonObject jsonObject) {
            super(jsonObject);
        }

        /**
         * @return 包含本次请求的算法输出内容。
         */
        public String text() {
            return readString("text");
        }

        /**
         * 有三种情况：正在生成时为null，生成结束时如果由于停止token导致则为stop，生成结束时如果因为生成长度过长导致则为length。
         *
         * @return null|"stop"|"length"
         */
        public String finishReason() {
            return readString("finish_reason");
        }
    }

    public static class OutputForMessageStyle extends SimpleJsonifiableEntity {
        public OutputForMessageStyle(JsonObject jsonObject) {
            super(jsonObject);
        }

        public List<Choice> choices() {
            List<Choice> list = new ArrayList<>();
            List<JsonObject> choices = readJsonObjectArray("choices");
            if (choices != null) {
                choices.forEach(x -> {
                    list.add(new Choice(x));
                });
            }
            return list;
        }
    }

    public static class Choice extends SimpleJsonifiableEntity {
        public Choice(JsonObject jsonObject) {
            super(jsonObject);
        }

        /**
         * 停止原因
         *
         * @return null：生成过程中;stop：stop token导致结束;length：生成长度导致结束
         */
        public String finishReason() {
            return readString("finish_reason");
        }

        /**
         * message每个元素形式为 `{"role":"角色", "content": "内容"}`。
         * 角色当前可选值：system、user、assistant。
         * 未来可以扩展到更多role，content则包含本次请求算法输出的内容。
         */
        public Message message() {
            return new Message(Role.valueOf(readString("message", "role")), readString("message", "content"));
        }
    }

    public static class Usage extends SimpleJsonifiableEntity {
        public Usage(JsonObject jsonObject) {
            super(jsonObject);
        }

        /**
         * @return 本次请求算法输出内容的 token 数目。
         */
        public Integer outputTokens() {
            return readInteger("output_tokens");
        }

        /**
         * @return 本次请求输入内容的 token 数目。在打开了搜索的情况下，输入的 token 数目因为还需要添加搜索相关内容支持，所以会超出客户在请求中的输入。
         */
        public Integer inputTokens() {
            return readInteger("input_tokens");
        }
    }
}
