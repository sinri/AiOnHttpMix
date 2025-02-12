package io.github.sinri.AiOnHttpMix.test.unit.volces.v3;

import io.github.sinri.AiOnHttpMix.test.unit.LLMUnitTestCoverageForNonFC;
import io.github.sinri.AiOnHttpMix.volces.v3.VolcesChatRole;
import io.github.sinri.AiOnHttpMix.volces.v3.chunk.VolcesChatResponseChunk;
import io.github.sinri.AiOnHttpMix.volces.v3.request.VolcesChatRequest;
import io.github.sinri.AiOnHttpMix.volces.v3.response.VolcesChatResponseChoice;
import io.github.sinri.AiOnHttpMix.volces.v3.response.VolcesChatResponseMessage;
import io.vertx.core.Future;
import org.junit.Test;

import java.util.List;

public class VolcesKitWithoutFCUnitTest extends AbstractVolcesKitTestUnit
        implements LLMUnitTestCoverageForNonFC<VolcesChatRequest> {
    @Test
    @Override
    public void testSyncWithoutToolCall() {
        this.async(() -> getKit()
                .chat(getServiceMeta(), generateRequest(), generateRequestId())
                .compose(resp -> {
                    List<VolcesChatResponseChoice> choices = resp.getChoices();
                    VolcesChatResponseChoice choice = choices.get(0);
                    VolcesChatResponseMessage message = choice.getMessage();
                    VolcesChatRole role = message.getRole();
                    String content = message.getContent();
                    getUnitTestLogger().info("From " + role + ": " + content);
                    return Future.succeededFuture();
                })
        );
    }

    @Test
    @Override
    public void testStreamWithoutToolCall() {
        this.async(() -> getKit()
                .chatStreamWithChunkHandler(getServiceMeta(), generateRequest(), chunk -> {
                    List<VolcesChatResponseChunk.StreamChoice> choices = chunk.getChoices();
                    if (choices != null) {
                        VolcesChatResponseChunk.StreamChoice choice = choices.get(0);
                        VolcesChatResponseChunk.ChoiceDelta delta = choice.getDelta();
                        VolcesChatRole role = delta.getRole();
                        String content = delta.getContent();
                        getUnitTestLogger().info("From " + role + ": " + content);
                    }
                }, 0, generateRequestId())
        );
    }

    @Test
    @Override
    public void testStreamBufferWithoutToolCall() {
        this.async(() -> getKit()
                .chatStreamWithBuffer(getServiceMeta(), generateRequest(), 0, generateRequestId())
                .compose(resp -> {
                    List<VolcesChatResponseChoice> choices = resp.getChoices();
                    VolcesChatResponseChoice choice = choices.get(0);
                    VolcesChatResponseMessage message = choice.getMessage();
                    VolcesChatRole role = message.getRole();
                    String content = message.getContent();
                    getUnitTestLogger().info("From " + role + ": " + content);
                    return Future.succeededFuture();
                })
        );
    }

    @Override
    public VolcesChatRequest generateRequest() {
        return VolcesChatRequest.create()
                                .addMessage(msg -> msg.setRole(VolcesChatRole.system)
                                                      .setContent("你是公司法务"))
                                .addMessage(msg -> msg.setRole(VolcesChatRole.user)
                                                      .setContent("使用市面上云平台部署的大模型服务，将公司内数据通过RAG的方式作为大模型的输入，存在数据安全风险吗？"))
                ;
    }

    @Override
    protected String getServiceName() {
        return "doubao-pro-128k";
    }
}
