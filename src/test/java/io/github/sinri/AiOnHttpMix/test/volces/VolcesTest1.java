package io.github.sinri.AiOnHttpMix.test.volces;

import io.github.sinri.AiOnHttpMix.volces.v3.VolcesChatRole;
import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.github.sinri.AiOnHttpMix.volces.v3.request.VolcesChatMessageForRequest;
import io.github.sinri.AiOnHttpMix.volces.v3.request.VolcesChatRequest;
import io.github.sinri.AiOnHttpMix.volces.v3.response.VolcesChatResponseChoice;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class VolcesTest1 extends VolcesTestCore {
    private VolcesKit volcesKit;
    private VolcesChatRequest chatCompletionsRequest;

    @Override
    protected @NotNull Future<Void> starting() {
        return super.starting()
                .compose(v -> {
                    volcesKit = new VolcesKit();
                    chatCompletionsRequest = VolcesChatRequest.create()
                            .addMessage(VolcesChatMessageForRequest.create()
                                    .setRole(VolcesChatRole.system)
                                    .setContent("你是一个专业的IT工程师。")
                            )
                            .addMessage(VolcesChatMessageForRequest.create()
                                    .setRole(VolcesChatRole.user)
                                    .setContent("Java 17和Java 21的差别是什么？")
                            );
                    return Future.succeededFuture();
                });
    }

    @TestUnit
    public Future<Void> test1() {
        String requestId = UUID.randomUUID().toString();
        return volcesKit.chat(
                        getServiceMeta(),
                        chatCompletionsRequest.toJsonObject(),
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
        return volcesKit.chat(
                        getServiceMeta(),
                        chatCompletionsRequest,
                        requestId
                )
                .compose(resp -> {
                    getLogger().info("resp");
                    List<VolcesChatResponseChoice> choices = resp.getChoices();
                    VolcesChatResponseChoice choice = choices.get(0);
                    var message = choice.getMessage();
                    getLogger().info("role: "+message.getRole());
                    getLogger().info("content: "+message.getContent());
                    return Future.succeededFuture();
                });
    }
}
