package io.github.sinri.AiOnHttpMix.test.unit.provider.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.message.QwenMessageInRequest;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.message.QwenMessageInResponse;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.request.parameters.QwenRequestSearchOptions;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.response.sync.QwenResponseOutput;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.response.sync.QwenResponseOutputChoice;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.response.sync.QwenResponseOutputSearchInfo;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.tool.QwenToolDefinition;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolCall;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.common.dsl.Schemas;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 基于{@link QwenKit}，测试同步LLM调用。
 */
public class QwenSyncUnitTest extends AbstractQwenKitUnitTest {
    /**
     * 测试常规单轮对话。
     */
    @Test
    public void test1() {
        async(() -> {
            QwenRequest request = QwenRequest.create()
                                             .input(x -> x
                                                     .addMessage(
                                                             QwenMessageInRequest.createAsUserInRequest("以“东汉末年”开头写一部开高达征服世界的故事")
                                                     ))
                                             .parameters(x -> x
                                                     .maxTokens(1024));
            getUnitTestLogger().info("req", request.toJsonObject());
            return getQwenKit().chat(
                                       getServiceAdapter(),
                                       qwenPlus,
                                       request,
                                       UUID.randomUUID().toString()
                               )
                               .compose(resp -> {
                                   getUnitTestLogger().info("resp", resp.cloneAsJsonObject());

                                   QwenResponseOutput output = resp.getOutput();
                                   List<QwenResponseOutputChoice> choices = output.getChoices();
                                   QwenResponseOutputChoice choice = choices.get(0);
                                   QwenMessageInResponse message = choice.getMessage();
                                   String content = message.getContent();
                                   getUnitTestLogger().info("content: " + content);

                                   return Future.succeededFuture();
                               });
        });
    }

    /**
     * 测试工具（函数）调用，并以调用结果追加一轮对话。
     */
    @Test
    public void test2() {
        async(() -> {
            QwenRequest request = QwenRequest.create()
                                             .input(x -> x
                                                     .addMessage(QwenMessageInRequest.createAsSystemInRequest(
                                                             "你需要根据用户的输入作答，并将答案包装为一个 json object 输出，格式为 `{\"answer\":\"\"}`。"
                                                     ))
                                                     .addMessage(QwenMessageInRequest.createAsUserInRequest(
                                                             "2025年5月3日，杭州的天气是什么"
                                                     ))
                                             )
                                             .parameters(x -> x
                                                     .maxTokens(1024)
                                                     .addTool(new QwenToolDefinition(f -> f
                                                             .name("query_weather")
                                                             .parameters(Schemas.objectSchema()
                                                                                .property("date", Schemas.stringSchema())
                                                                                .property("place", Schemas.stringSchema())
                                                                                .toJson()
                                                             )
                                                     ))
                                             );
            getUnitTestLogger().info("req", request.toJsonObject());

            AtomicReference<QwenMessageInResponse> toolCallMessageRef = new AtomicReference<>();
            AtomicReference<String> toolCallIdRef = new AtomicReference<>();

            return getQwenKit().chat(
                                       getServiceAdapter(),
                                       qwenPlus,
                                       request,
                                       UUID.randomUUID().toString()
                               )
                               .compose(resp -> {
                                   getUnitTestLogger().info("resp", resp.cloneAsJsonObject());

                                   QwenResponseOutput output = resp.getOutput();
                                   List<QwenResponseOutputChoice> choices = output.getChoices();
                                   QwenResponseOutputChoice choice = choices.get(0);
                                   QwenMessageInResponse message = choice.getMessage();

                                   // for later
                                   toolCallMessageRef.set(message);

                                   String content = message.getContent();
                                   getUnitTestLogger().info("content: " + content);

                                   List<ToolCall> toolCalls = message.getToolCalls();
                                   Assert.assertFalse(toolCalls.isEmpty());
                                   ToolCall toolCall = toolCalls.get(0);

                                   String toolCallId = toolCall.getId();
                                   toolCallIdRef.set(toolCallId);

                                   FunctionToolCall function = toolCall.getFunction();

                                   Assert.assertEquals("query_weather", function.getName());
                                   var arg = new JsonObject(function.getArguments());
                                   getUnitTestLogger().info("to call function " + function.getName() + "(" + arg + ")...");
                                   return Future.succeededFuture(arg);
                               })
                               .compose(arg -> {
                                   String date = arg.getString("date");
                                   String place = arg.getString("place");
                                   return Future.succeededFuture(place + "在" + date + "的天气为晴天，偶有西风，温度30到34摄氏度。");
                               })
                               .compose(answer -> {
                                   request.input(x -> x
                                           .addMessage(toolCallMessageRef.get())
                                           .addMessage(QwenMessageInRequest.createAsToolOutputInRequest(
                                                   answer, toolCallIdRef.get()
                                           ))
                                   );

                                   return getQwenKit().chat(
                                           getServiceAdapter(),
                                           qwenPlus,
                                           request,
                                           UUID.randomUUID().toString()
                                   );
                               })
                               .compose(resp -> {
                                   getUnitTestLogger().info("resp", resp.cloneAsJsonObject());

                                   QwenResponseOutput output = resp.getOutput();
                                   List<QwenResponseOutputChoice> choices = output.getChoices();
                                   QwenResponseOutputChoice choice = choices.get(0);
                                   QwenMessageInResponse message = choice.getMessage();

                                   String content = message.getContent();
                                   getUnitTestLogger().info("content: " + content);

                                   return Future.succeededFuture();
                               });
        });
    }

    /**
     * 测试搜索机能开启的常规单轮对话。
     */
    @Test
    public void test3() {
        async(() -> {
            QwenRequest request = QwenRequest.create()
                                             .input(x -> x
                                                     .addMessage(
                                                             QwenMessageInRequest.createAsUserInRequest("2025年的梅雨时期是哪段时间？")
                                                     ))
                                             .parameters(x -> x
                                                     .maxTokens(1024)
                                                     .enableSearch(true)
                                                     .searchOptions(QwenRequestSearchOptions.create()
                                                                                            .enableCitation(true)
                                                                                            .citationFormat("[ref_<number>]")
                                                                                            .enableSource(true)
                                                     )
                                             );
            getUnitTestLogger().info("req", request.toJsonObject());
            return getQwenKit().chat(
                                       getServiceAdapter(),
                                       qwenPlus,
                                       request,
                                       UUID.randomUUID().toString()
                               )
                               .compose(resp -> {
                                   getUnitTestLogger().info("resp", resp.cloneAsJsonObject());

                                   QwenResponseOutput output = resp.getOutput();
                                   List<QwenResponseOutputChoice> choices = output.getChoices();
                                   QwenResponseOutputChoice choice = choices.get(0);
                                   QwenMessageInResponse message = choice.getMessage();
                                   String content = message.getContent();
                                   getUnitTestLogger().info("content: " + content);

                                   QwenResponseOutputSearchInfo searchInfo = output.getSearchInfo();
                                   List<QwenResponseOutputSearchInfo.SearchResult> searchResults = searchInfo.getSearchResults();
                                   searchResults.forEach(searchResult -> {
                                       getUnitTestLogger().info("search result " + searchResult.getIndex() + " from " + searchResult.getSiteName() + ": " + searchResult.getTitle());
                                   });

                                   return Future.succeededFuture();
                               });
        });
    }

    /**
     * parameter.enable_thinking only support stream call
     */
    public void test4() {
        async(() -> {
            QwenRequest request = QwenRequest.create()
                                             .input(x -> x
                                                     .addMessage(
                                                             QwenMessageInRequest.createAsUserInRequest("夏天有什么解暑的东西推荐啊，已知西瓜吃吃太麻烦，雪糕最近又越来越贵……")
                                                     ))
                                             .parameters(x -> x
                                                     .enableThinking(true)
                                             );
            getUnitTestLogger().info("req", request.toJsonObject());
            return getQwenKit().chat(
                                       getServiceAdapter(),
                                       qwenPlusLatest,
                                       request,
                                       UUID.randomUUID().toString()
                               )
                               .compose(resp -> {
                                   getUnitTestLogger().info("resp", resp.cloneAsJsonObject());

                                   QwenResponseOutput output = resp.getOutput();
                                   List<QwenResponseOutputChoice> choices = output.getChoices();
                                   QwenResponseOutputChoice choice = choices.get(0);
                                   QwenMessageInResponse message = choice.getMessage();
                                   String content = message.getContent();
                                   getUnitTestLogger().info("content: " + content);

                                   return Future.succeededFuture();
                               });
        });
    }
}
