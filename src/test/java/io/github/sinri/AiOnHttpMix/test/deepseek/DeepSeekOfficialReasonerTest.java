package io.github.sinri.AiOnHttpMix.test.deepseek;

import io.github.sinri.AiOnHttpMix.deepseek.DeepseekKit;
import io.github.sinri.AiOnHttpMix.deepseek.chat.DeepseekChatRequest;
import io.github.sinri.AiOnHttpMix.deepseek.chat.message.DeepseekMessageInRequest;
import io.github.sinri.AiOnHttpMix.deepseek.chat.message.DeepseekMessageInResponse;
import io.github.sinri.AiOnHttpMix.deepseek.chat.message.DeepseekRole;
import io.github.sinri.AiOnHttpMix.deepseek.core.DeepseekModel;
import io.github.sinri.AiOnHttpMix.deepseek.core.DeepseekServiceMeta;
import io.github.sinri.AiOnHttpMix.test.BaseUnitTest;
import org.junit.Test;

import java.util.UUID;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class DeepSeekOfficialReasonerTest extends BaseUnitTest {
    private DeepseekServiceMeta serviceMeta;
    private DeepseekChatRequest chatRequest;
    private DeepseekChatRequest chatRequestFC;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        String apiKey = Keel.config("DeepSeek.main.apiKey");
        serviceMeta = new DeepseekServiceMeta(apiKey);

        chatRequest = DeepseekChatRequest.create();
        chatRequest
                .setModel(DeepseekModel.ReasonerModel)
                .addMessage(DeepseekMessageInRequest.create()
                        .setRole(DeepseekRole.system)
                        .setContent("你是一个专业的Java开发者")
                )
                .addMessage(DeepseekMessageInRequest.create()
                        .setRole(DeepseekRole.user)
                        .setContent("有一个项目，里面会设定定时任务，其中某些任务会有突发高内存占用，项目采用ZGC，请给点让项目不要老是挂掉的建议")
                );
    }

    @Test
    public void testSyncPure() {
        Keel.pseudoAwait(promise -> {

            String requestId = UUID.randomUUID().toString();

            DeepseekKit deepseekKit = new DeepseekKit();
            deepseekKit.chat(serviceMeta, chatRequest, requestId)
                    .onSuccess(resp -> {
                        DeepseekMessageInResponse message = resp.getChoices().get(0).getMessage();
                        getLogger().info("response", j -> j
                                .put("role", message.getRole().name())
                                .put("reasoning_content", message.getReasoningContent())
                                .put("content", message.getContent())
                        );
                        promise.complete();
                    })
                    .onFailure(e -> {
                        promise.fail(e);
                    });
        });
    }


    @Test
    public void testStreamPure() {
        Keel.pseudoAwait(promise -> {
            String requestId = UUID.randomUUID().toString();
            DeepseekKit deepseekKit = new DeepseekKit();
            deepseekKit.chatStreamWithStreamHandler(
                            serviceMeta,
                            chatRequest.toJsonObject(),
                            s -> {
                                getLogger().info("response chunk: " + s);
                            },
                            requestId
                    )
                    .onSuccess(resp -> {
                        getLogger().info("response end");
                        promise.complete();
                    })
                    .onFailure(e -> {
                        getLogger().exception(e);
                        promise.fail(e);
                    });
        });
    }
}
