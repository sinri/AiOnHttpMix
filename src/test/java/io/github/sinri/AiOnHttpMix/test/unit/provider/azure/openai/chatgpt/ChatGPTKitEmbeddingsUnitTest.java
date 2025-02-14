package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai.chatgpt;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.embeddings.OpenAIEmbeddingResponse;
import io.github.sinri.AiOnHttpMix.test.unit.core.TestPassed;
import io.vertx.core.Future;
import org.junit.Test;

import java.util.List;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class ChatGPTKitEmbeddingsUnitTest extends AbstractChatGPTKitChatUnitTest {
    @Test
    @TestPassed(time = "2025-02-13")
    public void fetchEmbeddingTensorForText() {
        async(() -> getKit().fetchEmbeddingTensorForText(
                                    getServiceMeta(),
                                    "强化边境防御措施，加强军备训练，确保在局势失控时能够迅速应对。此外，可以加强与附近友好藩镇的联盟，以形成制衡。",
                                    generateRequestId()
                            )
                            .compose(resp -> {
                                List<OpenAIEmbeddingResponse.Datum> data = resp.getData();
                                assert data != null;
                                for (OpenAIEmbeddingResponse.Datum datum : data) {
                                    List<Double> embedding = datum.getEmbedding();
                                    getUnitTestLogger().info("One Embedding Datum: " + Keel.stringHelper()
                                                                                           .joinStringArray(embedding, ", "));
                                }
                                return Future.succeededFuture();
                            })
        );
    }

    @Override
    protected String getServiceName() {
        return "text-embedding-3-large";
    }
}
