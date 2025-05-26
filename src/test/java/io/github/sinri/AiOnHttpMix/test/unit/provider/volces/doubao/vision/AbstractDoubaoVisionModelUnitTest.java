package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.doubao.vision;

import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.DoubaoMessageInVisionRequest;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.vision.ContentImageUrl;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.vision.DoubaoVisionContent;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.request.DoubaoRequest;
import io.github.sinri.AiOnHttpMix.test.unit.provider.volces.AbstractVolcesVisionModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.volces.doubao.DoubaoVisionModelSeries;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonFunctionToolDefinition;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonToolDefinition;
import io.vertx.json.schema.common.dsl.Schemas;
import org.junit.Before;

import java.util.List;

public class AbstractDoubaoVisionModelUnitTest extends AbstractVolcesVisionModelUnitTest<DoubaoVisionModelSeries> {

    protected DoubaoRequest requestWithoutToolCall;
    protected DoubaoRequest requestWithToolCall;

    public AbstractDoubaoVisionModelUnitTest() {
        super();
    }

    @Override
    protected DoubaoVisionModelSeries buildModel() {
        return DoubaoVisionModelSeries.model(DoubaoVisionModelSeries.MODEL_NAME_OF_DOUBAO_1d5_VISION_PRO_32K_250115);
    }

    @Before
    @Override
    public void setUp() {
        super.setUp();

        getUnitTestLogger().info("Test with model " + getModel().getModelName());

        var imageUrl = "https://p9-arcosite.byteimg.com/tos-cn-i-goo7wpa0wc/f9a8b28c25ac4d86979de8f52e73def6~tplv-goo7wpa0wc-image.image";
        requestWithoutToolCall = DoubaoRequest.create()
                                              //.addMessage(DoubaoMessageInChatRequest.createAsSystemMessage())
                                              .addMessage(DoubaoMessageInVisionRequest.createAsUserMessage(
                                                      List.of(
                                                              DoubaoVisionContent.create()
                                                                                 .setType("text")
                                                                                 .setText("这个图片里的动物吃什么"),
                                                              DoubaoVisionContent.create()
                                                                                 .setType("image_url")
                                                                                 .setImageUrl(ContentImageUrl.create()
                                                                                                 .setUrl(imageUrl)
                                                                     )
                                                      )
                                              ));
        requestWithToolCall = DoubaoRequest.create()
                                           //.addMessage(DoubaoMessageInChatRequest.createAsSystemMessage())
                                           .addMessage(DoubaoMessageInVisionRequest.createAsUserMessage(
                                                   List.of(
                                                           DoubaoVisionContent.create()
                                                                              .setType("text")
                                                                              .setText("这个图片里的地点当前的天气是什么"),
                                                           DoubaoVisionContent.create()
                                                                              .setType("image_url")
                                                                              .setImageUrl(ContentImageUrl.create()
                                                                                              .setUrl(imageUrl)
                                                                  )
                                                   )
                                           ))
                                           .addTool(new CommonToolDefinition(new CommonFunctionToolDefinition()
                                                   .name("query_current_weather")
                                                   .description("查询某地点当前的天气")
                                                   .parameters(Schemas.objectSchema()
                                                                      .property("place", Schemas.stringSchema())
                                                                      .toJson()
                                                   )));
    }
}
