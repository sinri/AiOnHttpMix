package io.github.sinri.AiOnHttpMix.test.dashscope;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request.QwenRequest;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class QwenTest5 extends DashscopeTestCore {
    private QwenKit qwenKit;
    private QwenRequest chatRequest;

    @Override
    protected @NotNull Future<Void> starting() {
        return super.starting()
                .compose(v -> {
                    qwenKit = new QwenKit();
                    chatRequest = QwenRequest.create()
                            .setModel(QwenKit.QwenModel.QWEN_PLUS)
                            .handleInput(input -> input
                                    .addSystemMessage("你是个财经情报专员，通过搜索新闻来进行总结报告。")
                                    .addUserMessage("2024年8月的A股走势如何")
                            )
                            .handleParameters(p -> p
                                    .setResultFormat(QwenRequest.Parameters.ResultFormat.message)
                                    .setEnableSearch(true)
                                    .handleSearchOptions(x -> x
                                            .setEnableSource(true)
                                            .setEnableCitation(true)
                                            .setCitationFormat("[ref_<number>]")
                                            .setForcedSearch(true)
                                    )
                            );

//                    chatRequest.toJsonObject()
//                            .getJsonObject("parameters")
//                            .put("search_options", new JsonObject()
//                                    .put("enable_source", true)
//                                    .put("enable_citation", true)
//                                    .put("citation_format", "[ref_<number>]")
//                                    .put("forced_search", true)
////                                    .put("prepend_search_result", true)
////                                    .put("enable_search_extension", true)
//                            )
//                    ;
                    return Future.succeededFuture();
                });
    }

    @TestUnit
    public Future<Void> test1() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", chatRequest.toJsonObject());
        return qwenKit.chat(
                        getServiceMeta(),
                        chatRequest.toJsonObject(),
                        requestId
                )
                .compose(resp -> {
                    getLogger().info("resp", resp);
                    return Future.succeededFuture();
                });
    }
}
