package io.github.sinri.AiOnHttpMix.test.dashscope;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.embedding.DashscopeTextEmbeddingGenerateRequest;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.embedding.DashscopeTextEmbeddingGenerateResponseOutput;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class TextEmbeddingTest extends DashscopeTestCore {
    private QwenKit qwenKit;
    private DashscopeTextEmbeddingGenerateRequest textEmbeddingGenerateRequest;

    @Override
    protected @NotNull Future<Void> starting() {
        return super.starting()
                .compose(v -> {
                    qwenKit = new QwenKit();
                    textEmbeddingGenerateRequest = DashscopeTextEmbeddingGenerateRequest.create()
                            .setModel(QwenKit.TextEmbeddingModel.TEXT_EMBEDDING_V2)
                            .setInputTexts(List.of("电子商务时代已经到来", "零售业逐渐步入下一个十年"));
                    return Future.succeededFuture();
                });
    }

    @TestUnit
    public Future<Void> test1() {
        String requestId = UUID.randomUUID().toString();
        return qwenKit.generateTextEmbedding(
                        getServiceMeta(),
                        textEmbeddingGenerateRequest.toJsonObject(),
                        requestId
                )
                .compose(resp -> {
                    getLogger().info("resp", resp);
                    return Future.succeededFuture();
                });
    }

    @TestUnit
    public Future<Void> test2() {
        String requestId = UUID.randomUUID().toString();
        return qwenKit.generateTextEmbedding(
                        getServiceMeta(),
                        textEmbeddingGenerateRequest,
                        requestId
                )
                .compose(resp -> {
                    List<DashscopeTextEmbeddingGenerateResponseOutput.Embedding> embeddings = resp.getOutput().getEmbeddings();
                    embeddings.forEach(e -> {
                        getLogger().info("Embedding #" + e.getTextIndex(), new JsonObject()
                                .put("array", e.getTensor().toJsonArray())
                        );

                    });
                    return Future.succeededFuture();
                });
    }
}
