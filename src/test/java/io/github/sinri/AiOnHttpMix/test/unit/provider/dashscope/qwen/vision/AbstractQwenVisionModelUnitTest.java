package io.github.sinri.AiOnHttpMix.test.unit.provider.dashscope.qwen.vision;

import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.QwenServiceAdapter;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.message.vision.Content;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.tool.QwenToolDefinition;
import io.github.sinri.AiOnHttpMix.test.unit.provider.AbstractModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.models.dashscope.qwen.QwenVisionModelSeries;
import io.github.sinri.AiOnHttpMix.utils.specification.DashscopeModelSpecification;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import io.vertx.json.schema.common.dsl.Schemas;
import org.junit.Assert;
import org.junit.Before;

import java.util.List;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

/**
 * 针对Qwen系列LLM（即{@link DashscopeModelSpecification}），基于{@link QwenServiceAdapter}的单元测试抽象类。
 */
public abstract class AbstractQwenVisionModelUnitTest extends AbstractModelUnitTest<QwenVisionModelSeries> {
    private final QwenVisionModelSeries model;
    private final QwenKit qwenKit;
    protected QwenRequest requestWithoutToolCall;
    protected QwenRequest requestWithToolCall;


    public AbstractQwenVisionModelUnitTest() {
        model = QwenVisionModelSeries.model(QwenVisionModelSeries.MODEL_NAME_OF_QWEN_VL_PLUS);
        qwenKit = new QwenKit();
    }


    @Before
    @Override
    public void setUp() {
        super.setUp();
        var imageUrl = "https://mmbiz.qpic.cn/sz_mmbiz_jpg/MWpJc0Ao1ic034EWO3nPkVh12H6Y44vjekM7JUBOyYKViartbR9TbUvB4KgxpXJ7O8G7HdDHpiaibATCqLHmJe6IPA/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1";
        //"https://p9-arcosite.byteimg.com/tos-cn-i-goo7wpa0wc/f9a8b28c25ac4d86979de8f52e73def6~tplv-goo7wpa0wc-image.image";
        requestWithoutToolCall = QwenRequest.create()
                                            .input(input -> input
                                                    .addUserVisionMessage(
                                                            List.of(
                                                                    Content.create()
                                                                           .setText("这个图片里的人类在干什么"),
                                                                    Content.create()
                                                                           .setImage(imageUrl)
                                                            )
                                                    )
                                            );
        requestWithToolCall = QwenRequest.create()
                                         .input(input -> input
                                                 .addUserVisionMessage(
                                                         List.of(
                                                                 Content.create()
                                                                        .setText("这个图片里的地点当前的天气是什么"),
                                                                 Content.create()
                                                                        .setImage(imageUrl)
                                                         )
                                                 )
                                         )
                                         .parameters(p -> p
                                                 .addTool(new QwenToolDefinition(f -> f
                                                         .name("query_current_weather")
                                                         .description("查询某地点当前的天气")
                                                         .parameters(Schemas.objectSchema()
                                                                            .property("place", Schemas.stringSchema())
                                                                            .toJson()
                                                         ))));
    }

    public QwenKit getKit() {
        return qwenKit;
    }

    @Override
    protected QwenVisionModelSeries getModel() {
        return model;
    }

    @Override
    protected final ChatModelServiceAdapter buildServiceAdapter() {
        KeelConfigElement dashscopeConfig = Keel.getConfiguration().extract("provider", "dashscope", "qwen");
        Assert.assertNotNull(dashscopeConfig);
        return getModel().buildServiceAdapter(dashscopeConfig);
    }

    @Override
    protected QwenServiceAdapter getServiceAdapter() {
        return (QwenServiceAdapter) super.getServiceAdapter();
    }
}
