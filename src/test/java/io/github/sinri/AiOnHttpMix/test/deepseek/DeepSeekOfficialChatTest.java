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

import java.util.List;
import java.util.UUID;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class DeepSeekOfficialChatTest extends BaseUnitTest {
    private DeepseekServiceMeta serviceMeta;
    private DeepseekChatRequest chatRequestFC;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        String apiKey = Keel.config("DeepSeek.main.apiKey");
        serviceMeta = new DeepseekServiceMeta(apiKey);

        chatRequestFC = DeepseekChatRequest.create();
        chatRequestFC.setModel(DeepseekModel.ChatModel)
                .addTool(new DeepseekChatRequest.ToolDefinition.Builder()
                        .functionName("weather_query")
                        .functionDescription("Query weather for a city on certain date.")
                        .propertyAsString("city", "The name of a city")
                        .propertyAsString("date", "The date in YYYY-MM-DD format.")
                        .build()
                )
                .addMessage(DeepseekMessageInRequest.create()
                        .setRole(DeepseekRole.system)
                        .setContent("你是情报局的人")
                )
                .addMessage(DeepseekMessageInRequest.create()
                        .setRole(DeepseekRole.user)
                        .setContent("明天要去杭州的深度求索公司里调查，需不需要带伞？")
                );
    }

    @Test
    public void testSyncFC() {
        Keel.pseudoAwait(promise -> {
            String requestId = UUID.randomUUID().toString();

            DeepseekKit deepseekKit = new DeepseekKit();
            deepseekKit.chat(serviceMeta, chatRequestFC, requestId)
                    .onSuccess(resp -> {
                        getLogger().info(resp.toString());


                        DeepseekMessageInResponse message = resp.getChoices().get(0).getMessage();
                        getLogger().info("response", j -> j
                                .put("role", message.getRole().name())
                                .put("content", message.getContent())
                        );

                        List<DeepseekMessageInResponse.DeepseekToolCallInResponse> toolCalls = message.getToolCalls();
                        if (!toolCalls.isEmpty()) {
                            toolCalls.forEach(toolCall -> {
                                DeepseekMessageInResponse.DeepseekToolCallInResponse.FunctionCall function = toolCall.getFunction();
                                getLogger().info("ToolCall[" + toolCall.getType() + "] := " + function.getName() + "(" + function.getArguments() + ")");
                            });
                        }

                        promise.complete();
                    })
                    .onFailure(e -> {
                        promise.fail(e);
                    });
        });
    }

    @Test
    public void testStreamFC() {
        Keel.pseudoAwait(promise -> {
            String requestId = UUID.randomUUID().toString();
            DeepseekKit deepseekKit = new DeepseekKit();
            deepseekKit.chatStreamWithStreamHandler(
                            serviceMeta,
                            chatRequestFC.toJsonObject(),
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

    @Test
    public void testStreamFCWithBuffer() {
        Keel.pseudoAwait(promise -> {
            String requestId = UUID.randomUUID().toString();
            DeepseekKit deepseekKit = new DeepseekKit();
            deepseekKit.chatStreamWithBuffer(serviceMeta, chatRequestFC, requestId)
                    .onSuccess(resp -> {
                        getLogger().info(resp.toString());

                        DeepseekMessageInResponse message = resp.getChoices().get(0).getMessage();
                        getLogger().info("response", j -> j
                                .put("role", message.getRole().name())
                                .put("content", message.getContent())
                        );

                        List<DeepseekMessageInResponse.DeepseekToolCallInResponse> toolCalls = message.getToolCalls();
                        if (toolCalls != null && !toolCalls.isEmpty()) {
                            toolCalls.forEach(toolCall -> {
                                DeepseekMessageInResponse.DeepseekToolCallInResponse.FunctionCall function = toolCall.getFunction();
                                getLogger().info("ToolCall[" + toolCall.getType() + "] := " + function.getName() + "(" + function.getArguments() + ")");
                            });
                        }

                        promise.complete();
                    })
                    .onFailure(e -> {
                        promise.fail(e);
                    });
        });
    }
}
