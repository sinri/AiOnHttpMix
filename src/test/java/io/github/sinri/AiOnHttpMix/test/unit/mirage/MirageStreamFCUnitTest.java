package io.github.sinri.AiOnHttpMix.test.unit.mirage;

import io.github.sinri.AiOnHttpMix.mix.chat.message.MixChatMessage;
import io.github.sinri.AiOnHttpMix.mix.chat.request.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum;
import io.github.sinri.AiOnHttpMix.utils.tools.FunctionParameterDefinition;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonFunctionToolDefinition;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonToolDefinition;
import io.vertx.core.Future;
import io.vertx.json.schema.common.dsl.SchemaType;
import org.junit.Test;

import java.util.List;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class MirageStreamFCUnitTest extends AbstractMirageUnitTest {
    private MixChatRequest buildMirageRequestEntity(SupportedModelEnum supportedModelEnum) {
        MixChatRequest request = MixChatRequest.create(supportedModelEnum);
        request
                .addMessage(MixChatMessage.create()
                                          .setRole("user")
                                          .setTextContent("今天是 " + Keel.datetimeHelper().getCurrentDate())
                )
                .addMessage(MixChatMessage.create()
                                          .setRole("user")
                                          .setTextContent("今天的上野公园天气如何，适合游玩吗")
                );
        request.addTool(new CommonToolDefinition(new CommonFunctionToolDefinition()
                .name("query_weather")
                .description("查询某城市（地区）的天气")
                .parameters(List.of(
                        new FunctionParameterDefinition(SchemaType.STRING, "place", "地点，一般到一个城市或者地区"),
                        new FunctionParameterDefinition(SchemaType.STRING, "date", "日期，Y-m-d")
                ))
        ));
        return request;
    }


    private Future<Void> act(SupportedModelEnum supportedModelEnum) {
        return getMirageKit().requestStream(
                                     true,
                                     buildMirageRequestEntity(supportedModelEnum),
                                     fragmentData -> {
                                         getUnitTestLogger().info("fragment data: \n" + fragmentData);
                                         return Future.succeededFuture();
                                     }
                             )
                             .compose(resp -> {
                                 getUnitTestLogger().info("fin");
                                 return Future.succeededFuture();
                             });
    }

    @Test
    public void test1() {
        //        AigcMix.enableVerboseLogger();
        async(() -> act(SupportedModelEnum.ChatGPT4o));
    }

    @Test
    public void test2() {
        //        AigcMix.enableVerboseLogger();
        async(() -> act(SupportedModelEnum.QwenPlusLatest));
    }

    /**
     * Model `SupportedModelEnum.Doubao1d5ThinkingPro250415` is slow in sync mode.
     */
    @Test
    public void test3() {
        //        AigcMix.enableVerboseLogger();
        async(() -> act(SupportedModelEnum.Doubao1d5ThinkingPro250415));
    }

    @Test
    public void test4() {
        //        AigcMix.enableVerboseLogger();
        async(() -> act(SupportedModelEnum.DeepSeekChatOnVolces));
    }
}
