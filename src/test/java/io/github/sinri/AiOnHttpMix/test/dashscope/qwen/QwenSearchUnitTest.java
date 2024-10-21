package io.github.sinri.AiOnHttpMix.test.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.test.dashscope.DashscopeTestCore;
import io.github.sinri.keel.facade.async.KeelAsyncKit;
import io.vertx.core.Future;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class QwenSearchUnitTest extends DashscopeTestCore {
    private QwenKit qwenKit;
    private QwenRequest chatRequest;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        qwenKit = new QwenKit();
        chatRequest = QwenRequest.create()
                .setModel(QwenKit.QwenModel.QWEN_PLUS)
                .handleInput(input -> input
                        .addSystemMessage("你是个财经情报专员，通过搜索新闻来进行总结报告。")
                        .addUserMessage("2024年10月的A股走势如何")
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
    }

    @Test
    public void test1() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", chatRequest.toJsonObject());
        KeelAsyncKit.pseudoAwait(promise -> {
            qwenKit.chat(
                            getServiceMeta(),
                            chatRequest.toJsonObject(),
                            requestId
                    )
                    .compose(resp -> {
                        getLogger().info("resp", resp);
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }
}
