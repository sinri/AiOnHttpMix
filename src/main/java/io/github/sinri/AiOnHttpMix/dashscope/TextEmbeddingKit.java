package io.github.sinri.AiOnHttpMix.dashscope;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class TextEmbeddingKit {

    public final static String TextEmbeddingV1 = "text-embedding-v1";
    public final static String TextEmbeddingV2 = "text-embedding-v2";
    private final String apiKey;


    public TextEmbeddingKit(String apiKey) {
        this.apiKey = apiKey;

    }

//    public TextEmbeddingKit() {
//        this(Keel.config("dashscope.api_key"));
//    }

    /**
     * @param textList 文本内容，需要计算的输入字符串，支持中英文。支持多条文本输入，每次请求最多 25 条；每一条最长支持 2048 tokens。
     */
    public Future<Response> call(
            String model,
            List<String> textList,
            TextType text_type
    ) {
        String url = "https://dashscope.aliyuncs.com/api/v1/services/embeddings/text-embedding/text-embedding";
        JsonArray texts = new JsonArray();
        textList.forEach(texts::add);
        JsonObject parameters = new JsonObject().put("text_type", text_type.name());
        JsonObject body = new JsonObject()
                .put("model", model)
                .put("input", new JsonObject()
                        .put("texts", texts))
                .put("parameters", parameters);
        return WebClient.create(Keel.getVertx())
                .postAbs(url)
                .putHeader("Content-Type", "application/json")
                .bearerTokenAuthentication(this.apiKey)
                .sendJsonObject(body)
                .compose(bufferHttpResponse -> {
                    JsonObject resp = bufferHttpResponse.bodyAsJsonObject();
                    if (bufferHttpResponse.statusCode() == 200 && resp != null) {
//                        this.logger.info(eventLog -> eventLog
//                                .message("api responded 200")
//                                .context(c -> c
//                                        .put("input", body)
//                                        .put("output", resp)
//                                )
//                        );
                        Response response = new Response(bufferHttpResponse.statusCode(), resp);
                        return Future.succeededFuture(response);
                    } else {
//                        this.logger.error(eventLog -> eventLog
//                                .message("api responded " + bufferHttpResponse.statusCode())
//                                .context(c -> c
//                                        .put("input", body)
//                                        .put("output", bufferHttpResponse.bodyAsString())
//                                )
//                        );
                        return Future.failedFuture(new Exception("FAILED [" + bufferHttpResponse.statusCode() + "] " + bufferHttpResponse.bodyAsString()));
                    }
                }, throwable -> {
//                    this.logger.exception(throwable, eventLog -> eventLog
//                            .message("api failed")
//                            .context(c -> c.put("input", body))
//                    );
                    return Future.failedFuture(new Exception("FAILED", throwable));
                });
    }

    public static class Response extends SimpleJsonifiableEntity {
        public Response(int statusCode, JsonObject jsonObject) {
            super(jsonObject);
            this.toJsonObject().put("status_code", statusCode);
        }

        public Integer getStatusCode() {
            return readInteger("status_code");
        }

        public JsonArray getEmbeddingsArray() {
            return readJsonArray("output", "embeddings");
        }

        public Map<Integer, EmbeddingTensor> getEmbeddings() {
            JsonArray array = getEmbeddingsArray();
            Map<Integer, EmbeddingTensor> map = new TreeMap<>();
            if (array != null) {
                array.forEach(item -> {
                    JsonObject x = (JsonObject) item;
                    JsonArray embedding = x.getJsonArray("embedding");
                    Integer textIndex = x.getInteger("text_index");
                    EmbeddingTensor embeddingTensor = new EmbeddingTensor(embedding);
                    map.put(textIndex, embeddingTensor);
                });
            }
            return map;
        }

        public Integer getUsageTotalTokens() {
            return readInteger("usage", "total_tokens");
        }

        public String getRequestId() {
            return readString("request_id");
        }
    }

    public static class EmbeddingTensor {
        private final List<Double> array;

        public EmbeddingTensor() {
            array = new ArrayList<>();
        }

        public EmbeddingTensor(JsonArray jsonArray) {
            array = new ArrayList<>();
            jsonArray.forEach(item -> {
                if (item instanceof Number) {
                    array.add(((Number) item).doubleValue());
                } else {
                    throw new IllegalArgumentException();
                }
            });
        }

        public EmbeddingTensor(List<Double> list) {
            this.array = list;
        }

        public JsonArray toJsonArray() {
            return new JsonArray(array);
        }
    }

    /**
     * 文本转换为向量后可以应用于检索、聚类、分类等下游任务，
     * 对检索这类非对称任务为了达到更好的检索效果建议区分查询文本（query）和底库文本（document）类型,
     * 聚类、分类等对称任务可以不用特殊指定，采用系统默认值"document"即可
     */
    public enum TextType {
        query,
        document
    }

}
