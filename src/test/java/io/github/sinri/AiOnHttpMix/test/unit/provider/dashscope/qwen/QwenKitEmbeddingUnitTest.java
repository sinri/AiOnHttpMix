package io.github.sinri.AiOnHttpMix.test.unit.provider.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.embedding.DashscopeTextEmbeddingGenerateRequest;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.embedding.DashscopeTextEmbeddingGenerateResponseOutput;
import io.github.sinri.AiOnHttpMix.test.unit.core.AnyKitUnitTestRequestMixin;
import io.github.sinri.AiOnHttpMix.test.unit.core.TestPassed;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.junit.Test;

import java.util.List;

public class QwenKitEmbeddingUnitTest extends AbstractQwenKitUnitTest
        implements AnyKitUnitTestRequestMixin<DashscopeTextEmbeddingGenerateRequest> {

    @Override
    public DashscopeTextEmbeddingGenerateRequest generateRequest() {
        return DashscopeTextEmbeddingGenerateRequest.create()
                                                    .setModel(QwenKit.TextEmbeddingModel.TEXT_EMBEDDING_V2)
                                                    .setInputTexts(List.of("电子商务时代已经到来", "零售业逐渐步入下一个十年"));
    }

    @Test
    @TestPassed(time = "2025-02-13")
    public void test() {
        this.async(() -> getKit()
                .generateTextEmbedding(
                        getServiceMeta(),
                        generateRequest(),
                        generateRequestId()
                )
                .compose(resp -> {
                    DashscopeTextEmbeddingGenerateResponseOutput output = resp.getOutput();
                    List<DashscopeTextEmbeddingGenerateResponseOutput.Embedding> embeddings = output.getEmbeddings();
                    for (var embedding : embeddings) {
                        getUnitTestLogger().info("Embedding #" + embedding.getTextIndex(), new JsonObject()
                                .put("array", embedding.getTensor().toJsonArray())
                        );
                    }
                    return Future.succeededFuture();
                })
        );
    }
}
