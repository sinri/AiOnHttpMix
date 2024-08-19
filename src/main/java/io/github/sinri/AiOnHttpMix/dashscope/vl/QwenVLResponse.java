package io.github.sinri.AiOnHttpMix.dashscope.vl;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QwenVLResponse extends SimpleJsonifiableEntity {
    public QwenVLResponse(int statusCode, JsonObject jsonObject) {
        super(jsonObject);
        this.jsonObject.put("status_code", statusCode);
    }

    public int getStatusCode() {
        return Objects.requireNonNull(readInteger("status_code"));
    }

    public Output getOutput() {
        return new Output(Objects.requireNonNull(readJsonObject("output")));
    }

    public Usage getUsage() {
        return new Usage(Objects.requireNonNull(readJsonObject("usage")));
    }

    public String getRequestId() {
        return readString("request_id");
    }

    public static class Output extends SimpleJsonifiableEntity {

        public Output(JsonObject output) {
            super(output);
        }

        public List<Choice> getChoices() {
            List<Choice> choices = new ArrayList<>();
            for (JsonObject x : Objects.requireNonNull(readJsonObjectArray("choices"))) {
                choices.add(new Choice(x));
            }
            return choices;
        }

        public static class Choice extends SimpleJsonifiableEntity {
            public Choice(JsonObject jsonObject) {
                super(jsonObject);
            }

            public String getFinishReason() {
                return readString("finish_reason");
            }

            public QwenVLMessage getMessage() {
                return new QwenVLMessage(readJsonObject("message"));
            }
        }
    }

    public static class Usage extends SimpleJsonifiableEntity {
        public Usage(JsonObject x) {
            super(x);
        }

        /**
         * 有三种情况：正在生成时为null，生成结束时如果由于停止token导致则为stop，生成结束时如果因为生成长度过长导致则为length。
         */
        public String getFinishReason() {
            return readString("finish_reason");
        }

        /**
         * 本次请求算法输出内容的 token 数目。
         */
        public Integer getOutputTokens() {
            return readInteger("output_tokens");
        }

        /**
         * 本次请求输入内容的 token 数目。在打开了搜索的情况下，输入的 token 数目因为还需要添加搜索相关内容支持，所以会超出客户在请求中的输入。
         */
        public Integer getInputTokens() {
            return readInteger("input_tokens");
        }
    }

}
