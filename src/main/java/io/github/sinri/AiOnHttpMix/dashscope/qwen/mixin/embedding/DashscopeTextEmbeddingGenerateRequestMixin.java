package io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.embedding;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.keel.core.SelfInterface;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface DashscopeTextEmbeddingGenerateRequestMixin<E> extends JsonifiableEntity<E>, SelfInterface<E> {
    /**
     * @param textEmbeddingModel 指明需要调用的模型。
     */
    default E setModel(QwenKit.TextEmbeddingModel textEmbeddingModel) {
        toJsonObject().put("model", textEmbeddingModel.getModelCode());
        return getImplementation();
    }

    default QwenKit.TextEmbeddingModel getTextEmbeddingModel() {
        var model = readString("model");
        if (model == null) return null;
        return QwenKit.TextEmbeddingModel.fromModelCode(model);
    }

    /**
     * 取值：文本内容，需要计算的输入字符串，支持中英文。
     * 说明：支持多条文本输入，每次请求最多25条；每一条最长支持 2048 tokens。
     */
    default E setInputTexts(List<String> inputTexts) {
        toJsonObject()
                .put("input", new JsonObject()
                        .put("texts", new JsonArray(inputTexts))
                );
        return getImplementation();
    }

    default List<String> getInputTexts() {
        return readStringArray("input", "texts");
    }

    default E setParametersTextType(TextType textType) {
        toJsonObject()
                .put("parameters", new JsonObject()
                        .put("text_type", textType.name())
                );
        return getImplementation();
    }

    default TextType getParametersTextType() {
        return TextType.valueOf(readString("parameters", "text_type"));
    }

    enum TextType {
        query, document
    }
}
