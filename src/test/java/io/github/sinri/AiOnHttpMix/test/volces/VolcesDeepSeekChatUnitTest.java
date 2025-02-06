package io.github.sinri.AiOnHttpMix.test.volces;

import io.github.sinri.AiOnHttpMix.deepseek.chat.DeepseekChatRequest;

public class VolcesDeepSeekChatUnitTest extends VolcesPureUnitTest {
    //private DeepseekClient deepseekClient;
    private DeepseekChatRequest chatRequest;

    @Override
    protected String getServiceName() {
        return "DeepSeek-V3";
    }
//
//    @Before
//    @Override
//    public void setUp() throws Exception {
//        super.setUp();
//        deepseekClient = new DeepseekClient();
//
//        chatRequest = DeepseekChatRequest.create()
//                .addMessage(DeepseekMessageInRequest.create()
//                        .setRole(DeepseekMessageInRequest.Role.system)
//                        .setContent("你是一个家庭社会学专家。")
//                )
//                .addMessage(DeepseekMessageInRequest.create()
//                        .setRole(DeepseekMessageInRequest.Role.user)
//                        .setContent("我准备开展一个单亲家庭条件下影响幼儿心理健全的因素的研究，请提供专业的课题实施方案建议。")
//                );
//    }
//
//    @Test
//    public void test1() {
//        KeelAsyncKit.pseudoAwait(promise -> {
//            // chatRequest.setModel(getServiceName());
//            String requestId = UUID.randomUUID().toString();
//
//            JsonObject requestJsonObject = chatRequest.toJsonObject();
//            getLogger().info(x -> x.message("req").context(requestJsonObject));
//            deepseekClient.chat(getServiceMeta(), requestJsonObject, requestId)
//                    .onSuccess(resp -> {
//                        getLogger().info(x -> x.message("resp").context(resp));
//                        promise.complete();
//                    })
//                    .onFailure(throwable -> {
//                        promise.fail(throwable);
//                    });
//        });
//    }
//
//    @Test
//    public void test2() {
//        KeelAsyncKit.pseudoAwait(promise -> {
//            // chatRequest.setModel(getServiceName());
//            String requestId = UUID.randomUUID().toString();
//            deepseekClient.chat(getServiceMeta(), chatRequest, requestId)
//                    .onSuccess(resp -> {
//                        getLogger().info(x -> x.message("resp").context(resp.cloneAsJsonObject()));
//
//                        DeepseekChatResponse.Choice firstChoice = resp.getChoices().get(0);
//                        DeepseekMessageInResponse message = firstChoice.getMessage();
//                        //String reasoningContent = message.getReasoningContent();
//                        DeepseekMessageInRequest.Role role = message.getRole();
//                        String content = message.getContent();
//
//                        getLogger().info(x -> x.message("first choice: ")
//                                .context("role", role.name())
//                                //.context("reasoning", reasoningContent)
//                                .context("content", content)
//                        );
//
//                        promise.complete();
//                    })
//                    .onFailure(throwable -> {
//                        promise.fail(throwable);
//                    });
//        });
//    }


    @Override
    public void test1() {
        super.test1();
    }
}
