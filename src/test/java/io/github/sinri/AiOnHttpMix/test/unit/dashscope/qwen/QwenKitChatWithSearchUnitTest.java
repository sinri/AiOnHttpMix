package io.github.sinri.AiOnHttpMix.test.unit.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenModel;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenRole;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.message.QwenMessage;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.response.QwenResponseInMessageFormat;
import io.github.sinri.AiOnHttpMix.test.unit.AnyKitUnitTestRequestMixin;
import io.vertx.core.Future;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class QwenKitChatWithSearchUnitTest extends AbstractQwenKitUnitTest
        implements AnyKitUnitTestRequestMixin<QwenRequest> {
    @Override
    public QwenRequest generateRequest() {
        return QwenRequest.create()
                          .setModel(QwenModel.QWEN_PLUS.getModelCode())
                          .handleInput(input -> input
                                  .addSystemMessage("你是个财经情报专员，通过搜索新闻来进行总结报告。")
                                  .addUserMessage("2025年1月的A股走势如何")
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
        this.async(() -> getKit()
                .chatForMessageResponse(
                        getServiceMeta(),
                        generateRequest(),
                        generateRequestId()
                )
                .compose(resp -> {
                    List<QwenResponseInMessageFormat.OutputForMessageResponse.Choice> choices = resp.getOutput()
                                                                                                    .getChoices();
                    assert choices != null;
                    Assert.assertFalse(choices.isEmpty());
                    QwenResponseInMessageFormat.OutputForMessageResponse.Choice choice = choices.get(0);
                    QwenMessage message = choice.getMessage();
                    QwenRole role = message.getRole();
                    String content = message.getContent();
                    getUnitTestLogger().info("From " + role + ": " + content);
                    return Future.succeededFuture();
                })
        );
    }
}
