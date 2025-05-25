package io.github.sinri.AiOnHttpMix.mix.chat;

import io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.request.GPTRequest;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.request.DoubaoRequest;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonToolDefinition;
import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

class MixChatRequestImpl extends JsonifiableEntityImpl<MixChatRequest> implements MixChatRequest {
    public MixChatRequestImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    public MixChatRequestImpl() {
        super();
    }

    @Override
    public MixChatRequest setSupportedModelEnum(SupportedModelEnum supportedModelEnum) {
        return write("model", supportedModelEnum.name());
    }

    private SupportedModelEnum getSupportedModelEnum() {
        String s = readString("model");
        return SupportedModelEnum.valueOf(s);
    }

    @Override
    public ChatModel getChatModel() {
        return getSupportedModelEnum().getChatModel();
    }

    @Override
    public GPTRequest toGPTRequest() {
        GPTRequest request = GPTRequest.create();
        if (isStream()) {
            request.stream(true);
        }
        getMessages().forEach(m -> request.addMessage(m.toGPTMessageInChatRequest()));
        getTools().forEach(request::addTool);
        return request;
    }

    @Override
    public QwenRequest toQwenRequest() {
        QwenRequest request = QwenRequest.create();
        request.parameters(p -> p.resultFormat("message"));
        if (isStream()) {
            request.parameters(p -> p
                    .stream(true)
                    .incrementalOutput(true)
            );
        }
        getMessages().forEach(m -> request.input(i -> i
                .addMessage(m.toQwenMessageInChatRequest())
        ));
        getTools().forEach(t -> request
                .parameters(p -> p
                        .addTool(t)
                ));
        return request;
    }

    @Override
    public DoubaoRequest toDoubaoRequest() {
        DoubaoRequest request = DoubaoRequest.create();
        if (isStream()) {
            request.stream(true);
        }
        getMessages().forEach(m -> request.addMessage(m.toDoubaoMessageInChatRequest()));
        getTools().forEach(request::addTool);
        return request;
    }

    @Override
    public String getRequestId() {
        return readString("request_id");
    }

    @Override
    public long getTimeout() {
        return Objects.requireNonNullElse(readLong("timeout"), 180_000L);
    }

    @Override
    public MixChatRequest setTimeout(long timeout) {
        return write("timeout", timeout);
    }

    @Override
    public MixChatRequest setStream(boolean stream) {
        return write("stream", stream);
    }

    private boolean isStream() {
        Boolean stream = readBoolean("stream");
        return Boolean.TRUE.equals(stream);
    }

    @Override
    public MixChatRequest addMessage(MixChatMessage message) {
        ensureJsonArray("messages")
                .add(message.toJsonObject());
        return this;
    }

    @Override
    public List<MixChatMessage> getMessages() {
        List<JsonObject> array = readJsonObjectArray("messages");
        if (array == null) return List.of();
        return array.stream().map(MixChatMessage::wrap).toList();
    }

    @Override
    public MixChatRequest addTool(CommonToolDefinition toolDefinition) {
        ensureJsonArray("tools")
                .add(toolDefinition.toJsonObject());
        return this;
    }

    @Override
    public List<CommonToolDefinition> getTools() {
        List<JsonObject> array = readJsonObjectArray("tools");
        if (array == null) return List.of();
        return array.stream().map(CommonToolDefinition::new).toList();
    }

    @Nonnull
    @Override
    public MixChatRequest getImplementation() {
        return this;
    }
}
