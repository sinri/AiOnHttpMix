package io.github.sinri.AiOnHttpMix.test.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.embedding.DashscopeTextEmbeddingGenerateRequest;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.embedding.DashscopeTextEmbeddingGenerateResponseOutput;
import io.github.sinri.AiOnHttpMix.test.dashscope.DashscopeTestCore;
import io.github.sinri.keel.facade.async.KeelAsyncKit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

public class TextEmbeddingTest extends DashscopeTestCore {
    private QwenKit qwenKit;
    private DashscopeTextEmbeddingGenerateRequest textEmbeddingGenerateRequest;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        qwenKit = new QwenKit();
        textEmbeddingGenerateRequest = DashscopeTextEmbeddingGenerateRequest.create()
                .setModel(QwenKit.TextEmbeddingModel.TEXT_EMBEDDING_V2)
                .setInputTexts(List.of("电子商务时代已经到来", "零售业逐渐步入下一个十年"));
    }

    @Test
    public void test1() {
        String requestId = UUID.randomUUID().toString();
        KeelAsyncKit.pseudoAwait(promise -> {
            qwenKit.generateTextEmbedding(
                            getServiceMeta(),
                            textEmbeddingGenerateRequest.toJsonObject(),
                            requestId
                    )
                    .compose(resp -> {
                        getLogger().info("resp", resp);
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void test2() {
        String requestId = UUID.randomUUID().toString();
        KeelAsyncKit.pseudoAwait(promise -> {
            qwenKit.generateTextEmbedding(
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
                    })
                    .onComplete(promise);
        });
    }
}
