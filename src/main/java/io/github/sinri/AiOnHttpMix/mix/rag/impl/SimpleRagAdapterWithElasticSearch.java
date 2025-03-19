package io.github.sinri.AiOnHttpMix.mix.rag.impl;

import io.github.sinri.AiOnHttpMix.mix.rag.AnyLLMRagAdapter;
import io.github.sinri.keel.core.TechnicalPreview;
import io.github.sinri.keel.integration.elasticsearch.ElasticSearchKit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple RAG (Retrieval-Augmented Generation) adapter that uses Elasticsearch for searching references.
 * This class is marked as a technical preview, indicating it is in an experimental phase and may undergo changes.
 *
 * @see AnyLLMRagAdapter
 * @since 1.2.6
 */
@TechnicalPreview(since = "1.2.6")
public class SimpleRagAdapterWithElasticSearch implements AnyLLMRagAdapter {
    private final ElasticSearchKit elasticSearchKit;

    public SimpleRagAdapterWithElasticSearch(ElasticSearchKit elasticSearchKit) {
        this.elasticSearchKit = elasticSearchKit;
    }

    @Override
    public Future<String> searchReferencesFromRAG(String message) {

        return null;
    }

    private Future<List<JsonObject>> knn(
            String index,
            String vector_field,
            JsonArray vector,
            int k,
            int num_candidates,
            @Nullable List<String> readFieldList
    ) {
        JsonArray readFieldArray = new JsonArray();
        if (readFieldList != null) {
            readFieldList.forEach(readFieldArray::add);
        }
        // follow ElasticSearch 8.x API
        return elasticSearchKit.callPost(
                                       "/" + index + "/_search",
                                       null,
                                       new JsonObject()
                                               .put("knn", new JsonObject()
                                                       .put("field", vector_field)
                                                       .put("query_vector", vector)
                                                       .put("num_candidates", num_candidates)
                                                       .put("k", k)
                                               )
                                               .put("fields", readFieldArray)
                               )
                               .compose(response -> {
                                   Boolean timedOut = response.getBoolean("timed_out");
                                   if (timedOut) {
                                       // the request timed out before completion; returned results may be partial or empty.
                                       return Future.failedFuture("RAG through ElasticSearch Timeout");
                                   }
                                   JsonArray hits = response.getJsonObject("hits").getJsonArray("hits");
                                   List<JsonObject> results = new ArrayList<>();
                                   hits.forEach(hit -> {
                                       JsonObject j = (JsonObject) hit;
                                       String id = j.getString("_id");
                                       JsonObject fields = j.getJsonObject("fields");
                                       fields.put("_id", id);
                                       results.add(fields);
                                   });
                                   return Future.succeededFuture(results);
                               });
    }

}
