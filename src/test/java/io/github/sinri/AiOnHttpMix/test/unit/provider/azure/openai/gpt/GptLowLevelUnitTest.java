package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.test.unit.provider.core.AbstractModelRawUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.azure.openai.gpt.GPTVisionModelSeries;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.Test;

public class GptLowLevelUnitTest extends AbstractGptUnitTest implements AbstractModelRawUnitTest<GPTVisionModelSeries> {
    private JsonObject generateRequest(boolean useStreamIncrement) {
        JsonObject request = new JsonObject();
        request.put("messages", new JsonArray()
                .add(new JsonObject()
                        .put("role", "user")
                        .put("content", "介绍浮潜的方法")));
        request.put("temperature", 0.7);
        request.put("max_tokens", 1000);
        request.put("top_p", 0.95);
        if (useStreamIncrement) {
            request.put("stream", true);
        }
        return request;

    }

    @Test
    public void testSync() {
        async(() -> {
            return toTestSync(generateRequest(false));
        });
    }

    @Test
    public void test2() {
        async(() -> {
            return toTestStream(generateRequest(true));
        });
    }

    @Test
    public void test3() {
        JsonObject request = new JsonObject();
        request.put("messages", new JsonArray()
                       .add(new JsonObject()
                               .put("role", "system")
                               .put("content", """
                                               # 系统时间
                                               2025年05月28日 11:01:47 Wednesday
                                               
                                               # 角色
                                               
                                               万能答疑助手。中文名：乐小宝。英文名：QeeBot。你是“乐其集团”旗下“尤里卡（Eureka）"项目组研发的智能聊天机器人。
                                               注：“乐其集团”即“乐其电商”
                                               
                                               # 语言
                                               
                                               简体中文
                                               
                                               # 任务目标
                                               
                                               准确理解并快速解答用户提问。
                                               
                                               # 限制条件
                                               
                                               - 回答应简洁明了，避免冗长复杂的表述，必要时使用markdown排版。
                                               - 尊重用户，保持礼貌和友好的态度，对你无法处理的问题能够友好地说明情况和建议。
                                               - 注重安全和准确性，拒绝回答涉及不当内容的问题。遵守中华人民共和国的法律，并在提供服务时严格遵循相关规定。
                                               - “bing_search”插件没有在tools中时，你绝对不能调用这个插件！但是你可以建议用户开启位于输入框最右边的“联网搜索”按钮。
                                               - 如果输入框最右边没有“联网搜索”的按钮，你需要提示用户在屏幕左上角切换一个支持“联网搜索”的模型。
                                               
                                               # 特定技能
                                               
                                               - 意图识别：仔细分析用户的问题，确定其真实诉求，必要时可以对提问进行补充和改写。
                                               - 实时数据查询：当“bing_search”的插件可以使用且你需要获取最新新闻或资讯进行回答时，你才可以调用“bing_search”来查询互联网上的最新信息。
                                               - 上下文理解：特别关注与当前会话内容相结合的内容，确保结果与之前的上下文相一致。
                                               - 推理和分析：你可以根据上下文对话内容提供推理服务，比如向用户确认是否需要补充信息，或根据用户的需求分析具体任务，提供步骤拆解，与用户达成一致后执行。
                                               """)
                       )
                       .add(new JsonObject()
                               .put("role", "user")
                               .put("content", new JsonArray()
                                       .add(new JsonObject()
                                               .put("type", "text")
                                               .put("text", "观察这张图片，描述图片内容")
                                       )
                                       .add(new JsonObject()
                                               .put("type", "image_url")
                                               .put("image_url", new JsonObject()
                                                       .put("url", "https://cdl-pc-prod.oss-cn-hangzhou.aliyuncs.com/ai/eureka/chat/d328250bee32c02a1980a606a350f1f2.png?Expires=4902001306&OSSAccessKeyId=LTAI5tAdY33vnWKK7TGgomTd&Signature=kAdE3a%2Fbq%2Bu8m1DlAMfgu4w7HBU%3D"))
                                       )
                               ))
               )
               .put("stream", true);
        async(() -> {
            return toTestStream(request);
        });
    }
}
