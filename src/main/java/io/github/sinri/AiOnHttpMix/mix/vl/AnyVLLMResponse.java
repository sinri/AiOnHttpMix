package io.github.sinri.AiOnHttpMix.mix.vl;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.vl.QwenVLMessageContentItem;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.vl.QwenVLOutputMessage;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.vl.QwenVLResponse;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.vl.QwenVLRole;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMRole;
import io.github.sinri.AiOnHttpMix.volces.v3.VolcesChatRole;
import io.github.sinri.AiOnHttpMix.volces.v3.response.VolcesChatResponse;
import io.github.sinri.AiOnHttpMix.volces.v3.response.VolcesChatResponseChoice;
import io.github.sinri.AiOnHttpMix.volces.v3.response.VolcesChatResponseMessage;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface AnyVLLMResponse {
    static AnyVLLMResponse fromMirageVLResponseData(JsonObject jsonObject) {
        String role = jsonObject.getString("role");
        var resp = new AnyVLLMResponseImpl(AnyLLMRole.valueOf(role));

        JsonArray content = jsonObject.getJsonArray("content");
        content.forEach(item -> {
            var x = (JsonObject) item;
            if (x.containsKey("text")) {
                resp.addContentComponent(new AnyVLLMMessageComponent(AnyVLLMMessageComponentType.text, x.getString("text")));
            }
        });

        return resp;
    }

    static AnyVLLMResponse from(VolcesChatResponse volcesResp) {
        List<VolcesChatResponseChoice> choices = volcesResp.getChoices();
        VolcesChatResponseChoice choice = choices.get(0);
        VolcesChatResponseMessage message = choice.getMessage();
        VolcesChatRole role = message.getRole();
        AnyLLMRole anyLLMRole = role.toAnyLLMRole();
        String text = message.getContent();

        AnyVLLMResponseImpl anyVLLMResponse = new AnyVLLMResponseImpl(anyLLMRole);
        anyVLLMResponse.addContentComponent(new AnyVLLMMessageComponent(AnyVLLMMessageComponentType.text, text));
        return anyVLLMResponse;
    }

    static AnyVLLMResponse from(QwenVLResponse qwenResp) {
        QwenVLResponse.Output output = qwenResp.getOutput();
        List<QwenVLResponse.Choice> choices = output.getChoices();
        QwenVLResponse.Choice choice = choices.get(0);
        QwenVLOutputMessage message = choice.getMessage();
        QwenVLRole role = message.getRole();
        AnyLLMRole anyLLMRole = role.toAnyLLMRole();

        AnyVLLMResponseImpl anyVLLMResponse = new AnyVLLMResponseImpl(anyLLMRole);
        List<QwenVLMessageContentItem> items = message.getContent();
        items.forEach(item -> {
            String text = item.getText();
            if (text != null) {
                anyVLLMResponse.addContentComponent(new AnyVLLMMessageComponent(AnyVLLMMessageComponentType.text, text));
            }
            String image = item.getImage();
            if (image != null) {
                anyVLLMResponse.addContentComponent(new AnyVLLMMessageComponent(AnyVLLMMessageComponentType.image, image));
            }
        });
        return anyVLLMResponse;
    }

    AnyLLMRole getRole();

    List<AnyVLLMMessageComponent> getContent();

    default JsonObject toJsonObject() {
        JsonArray array = new JsonArray();
        getContent().forEach(c -> {
            array.add(c.toJsonObject());
        });
        return new JsonObject()
                .put("role", getRole().name())
                .put("content", array);
    }
}
