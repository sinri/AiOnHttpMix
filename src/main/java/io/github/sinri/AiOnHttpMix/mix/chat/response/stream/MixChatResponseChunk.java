package io.github.sinri.AiOnHttpMix.mix.chat.response.stream;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.stream.GPTResponseChunk;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.stream.QwenResponseChunk;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.stream.DoubaoResponseChunk;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolCall;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface MixChatResponseChunk extends JsonifiableEntity<MixChatResponseChunk> {
    static MixChatResponseChunk create() {
        return new MixChatResponseChunkImpl();
    }

    static MixChatResponseChunk wrap(JsonObject x) {
        return new MixChatResponseChunkImpl(x);
    }

    static MixChatResponseChunk from(GPTResponseChunk src) {
        MixChatResponseChunk chunk = MixChatResponseChunk.create();

        chunk.setCreated(src.getCreated());
        chunk.setId(src.getId());
        chunk.setModel(src.getModel());
        chunk.setObject(src.getObject());

        chunk.setChoices(src.getChoices()
                            .stream()
                            .map(c -> {
                                MixChatResponseChunkChoice x = MixChatResponseChunkChoice.create();

                                x.setFinishReason(c.getFinishReason());
                                x.setIndex(c.getIndex());

                                var delta = c.getDelta();
                                x.setRole(delta.getRole());
                                x.setContent(delta.getContent());

                                x.setToolCalls(delta.getToolCalls()
                                                    .stream()
                                                    .map(tcc -> (ToolCall) tcc)
                                                    .toList());

                                return x;
                            })
                            .toList()
        );

        return chunk;
    }

    static MixChatResponseChunk from(DoubaoResponseChunk src) {
        MixChatResponseChunk chunk = MixChatResponseChunk.create();

        chunk.setCreated(src.getCreated());
        chunk.setId(src.getId());
        chunk.setModel(src.getModel());
        chunk.setObject(src.getObject());

        chunk.setChoices(src.getChoices()
                            .stream()
                            .map(c -> {
                                MixChatResponseChunkChoice x = MixChatResponseChunkChoice.create();

                                x.setFinishReason(c.getFinishReason());
                                x.setIndex(c.getIndex());

                                var delta = c.getDelta();
                                x.setRole(delta.getRole());
                                x.setContent(delta.getContent());
                                x.setReasoningContent(delta.getReasoningContent());

                                x.setToolCalls(delta.getToolCalls()
                                                    .stream()
                                                    .map(tcc -> (ToolCall) tcc)
                                                    .toList());

                                return x;
                            })
                            .toList()
        );

        return chunk;
    }

    static MixChatResponseChunk from(QwenResponseChunk src) {
        MixChatResponseChunk chunk = MixChatResponseChunk.create();

        chunk.setId(src.getRequestId());

        chunk.setChoices(src.getOutput().getChoices()
                            .stream()
                            .map(c -> {
                                MixChatResponseChunkChoice x = MixChatResponseChunkChoice.create();

                                x.setFinishReason(c.getFinishReason());
                                var message = c.getMessage();
                                x.setRole(message.getRole());
                                x.setContent(message.getContent());
                                x.setReasoningContent(message.getReasoningContent());

                                x.setToolCalls(message.getToolCalls());

                                return x;
                            })
                            .toList()
        );

        return chunk;
    }

    default Integer getCreated() {
        return readInteger("created");
    }

    default MixChatResponseChunk setCreated(Integer created) {
        return this.write("created", created);
    }

    default String getId() {
        return this.readString("id");
    }

    default MixChatResponseChunk setId(String id) {
        return this.write("id", id);
    }

    default String getModel() {
        return this.readString("model");
    }

    default MixChatResponseChunk setModel(String model) {
        return this.write("model", model);
    }

    default String getObject() {
        return this.readString("object");
    }

    default MixChatResponseChunk setObject(String object) {
        return this.write("object", object);
    }

    default List<MixChatResponseChunkChoice> getChoices() {
        List<JsonObject> array = readJsonObjectArray("choices");
        if (array == null)
            return List.of();
        return array.stream().map(MixChatResponseChunkChoice::wrap).toList();
    }

    default MixChatResponseChunk setChoices(List<MixChatResponseChunkChoice> choices) {
        return this.write("choices", new JsonArray(choices.stream()
                                                          .map(JsonifiableEntity::toJsonObject)
                                                          .toList()));
    }
}
