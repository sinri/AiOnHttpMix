package io.github.sinri.AiOnHttpMix.test.volces;

import io.github.sinri.AiOnHttpMix.deepseek.DeepseekKit;
import io.github.sinri.AiOnHttpMix.deepseek.chat.DeepseekChatRequest;
import io.github.sinri.AiOnHttpMix.deepseek.chat.DeepseekChatResponse;
import io.github.sinri.AiOnHttpMix.deepseek.chat.message.DeepseekMessageInRequest;
import io.github.sinri.AiOnHttpMix.deepseek.chat.message.DeepseekMessageInResponse;
import io.github.sinri.AiOnHttpMix.deepseek.chat.message.DeepseekRole;
import io.github.sinri.keel.facade.async.KeelAsyncKit;
import io.vertx.core.json.JsonObject;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class VolcesDeepSeekReasonerUnitTest extends VolcesTestCore {

    private DeepseekKit deepseekKit;
    private DeepseekChatRequest chatRequest;

    @Override
    protected String getServiceName() {
        return "DeepSeek-R1";
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        deepseekKit = new DeepseekKit();

        chatRequest = DeepseekChatRequest.create()
                .addMessage(DeepseekMessageInRequest.create()
                        .setRole(DeepseekRole.system)
                        .setContent("你是一个家庭社会学专家。")
                )
                .addMessage(DeepseekMessageInRequest.create()
                        .setRole(DeepseekRole.user)
                        .setContent("我准备开展一个单亲家庭条件下影响幼儿心理健全的因素的研究，请提供专业的课题实施方案建议。")
                );
    }

    @Test
    public void test1() {
        KeelAsyncKit.pseudoAwait(promise -> {
            // chatRequest.setModel(getServiceName());
            String requestId = UUID.randomUUID().toString();

            JsonObject requestJsonObject = chatRequest.toJsonObject();
            getLogger().info(x -> x.message("req").context(requestJsonObject));
            deepseekKit.chat(getServiceMeta(), requestJsonObject, requestId)
                    .onSuccess(resp -> {
                        getLogger().info(x -> x.message("resp").context(resp));
                        promise.complete();
                    })
                    .onFailure(throwable -> {
                        promise.fail(throwable);
                    });
        });
    }

    @Test
    public void test2() {
        KeelAsyncKit.pseudoAwait(promise -> {
            // chatRequest.setModel(getServiceName());
            String requestId = UUID.randomUUID().toString();
            deepseekKit.chat(getServiceMeta(), chatRequest, requestId)
                    .onSuccess(resp -> {
                        getLogger().info(x -> x.message("resp").context(resp.cloneAsJsonObject()));

                        DeepseekChatResponse.Choice firstChoice = resp.getChoices().get(0);
                        DeepseekMessageInResponse message = firstChoice.getMessage();
                        String reasoningContent = message.getReasoningContent();
                        DeepseekRole role = message.getRole();
                        String content = message.getContent();

                        getLogger().info(x -> x.message("first choice: ")
                                .context("role", role.name())
                                .context("reasoning", reasoningContent)
                                .context("content", content)
                        );

                        promise.complete();
                    })
                    .onFailure(throwable -> {
                        promise.fail(throwable);
                    });
        });
    }

    @Test
    public void test3() {
        KeelAsyncKit.pseudoAwait(promise -> {
            // chatRequest.setModel(getServiceName());
            String requestId = UUID.randomUUID().toString();
            deepseekKit.chatStreamWithChunkHandler(
                            getServiceMeta(),
                            chatRequest,
                            chunk -> {
                                getLogger().info(x -> x.message("chunk").context(chunk.cloneAsJsonObject()));
                            },
                            requestId
                    )
                    .onSuccess(resp -> {
                        getLogger().info(x -> x.message("resp over"));
                        promise.complete();
                    })
                    .onFailure(throwable -> {
                        promise.fail(throwable);
                    });
        });
    }

    @Test
    public void test4() {
        KeelAsyncKit.pseudoAwait(promise -> {
            // chatRequest.setModel(getServiceName());
            String requestId = UUID.randomUUID().toString();
            deepseekKit.chatSSEWithBuffer(
                            getServiceMeta(),
                            chatRequest,
                            requestId
                    )
                    .onSuccess(resp -> {
                        getLogger().info(x -> x.message("resp"));
                        DeepseekChatResponse.Choice choice = resp.getChoices().get(0);
                        getLogger().info("Role: " + choice.getMessage().getRole());
                        getLogger().info("ReasoningContent: " + choice.getMessage().getReasoningContent());
                        getLogger().info("Content: " + choice.getMessage().getContent());
                        promise.complete();
                    })
                    .onFailure(throwable -> {
                        promise.fail(throwable);
                    });
        });
    }
}
