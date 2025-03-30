package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.v3;

import io.github.sinri.AiOnHttpMix.test.unit.core.LLMUnitTestCoverageForNonFC;
import io.github.sinri.AiOnHttpMix.volces.v3.VolcesChatRole;
import io.github.sinri.AiOnHttpMix.volces.v3.chunk.VolcesChatResponseChunk;
import io.github.sinri.AiOnHttpMix.volces.v3.request.VolcesChatRequest;
import io.github.sinri.AiOnHttpMix.volces.v3.response.VolcesChatResponseChoice;
import io.github.sinri.AiOnHttpMix.volces.v3.response.VolcesChatResponseMessage;
import io.github.sinri.AiOnHttpMix.volces.v3.visual.VolcesVisualChatMessageContent;
import io.github.sinri.AiOnHttpMix.volces.v3.visual.VolcesVisualChatMessageContentImageMeta;
import io.vertx.core.Future;
import org.junit.Test;

import java.util.List;

public class VolcesKitVisualWithoutFCUnitTest extends AbstractVolcesKitTestUnit
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
                    //                    List<VolcesVisualChatMessageContent> visualContent = message.getVisualContent();
                    //                    if(visualContent!=null) {
                    //                        visualContent.forEach(vc -> {
                    //                            getUnitTestLogger().info("Visual Content: ", vc.toJsonObject());
                    //                        });
                    //                    }
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
        String imageUrl = "https://www.news.cn/20250318/4f7d1f1aa7d84ca3884758b0b997decc/dc29ed3626f1478a89e21f51a93361ee.jpg";
        return VolcesChatRequest.create()
                                .addMessage(msg -> msg.setRole(VolcesChatRole.system)
                                                      .setContent("你是个摄影和后期的专业人士"))
                                .addMessage(msg -> msg.setRole(VolcesChatRole.user)
                                                      .setVisualContent(List.of(
                                                              VolcesVisualChatMessageContent.create()
                                                                                            .setText("分析一下这个图，优化一下色调"),
                                                              VolcesVisualChatMessageContent.create()
                                                                                            .setImageMeta(VolcesVisualChatMessageContentImageMeta.create()
                                                                                                                                                 .setImage(imageUrl)
                                                                                            )
                                                      ))
                                )
                ;
    }

    @Override
    protected String getServiceName() {
        return "doubao-1.5-vision-pro-32k";
    }
}
