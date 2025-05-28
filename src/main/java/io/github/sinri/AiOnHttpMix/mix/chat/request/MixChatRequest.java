package io.github.sinri.AiOnHttpMix.mix.chat.request;

import io.github.sinri.AiOnHttpMix.mix.chat.message.MixChatMessage;
import io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.request.GPTRequest;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.request.DoubaoRequest;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonToolDefinition;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface MixChatRequest extends JsonifiableEntity<MixChatRequest> {

    static MixChatRequest create() {
        return new MixChatRequestImpl();
    }

    static MixChatRequest wrap(JsonObject jsonObject) {
        return new MixChatRequestImpl(jsonObject);
    }

    SupportedModelEnum getSupportedModelEnum();

    MixChatRequest setSupportedModelEnum(SupportedModelEnum supportedModelEnum);

    ChatModel getChatModel();

    GPTRequest toGPTRequest();

    QwenRequest toQwenRequest();

    DoubaoRequest toDoubaoRequest();

    String getRequestId();

    MixChatRequest setRequestId(String requestId);

    long getTimeout();

    MixChatRequest setTimeout(long timeout);

    MixChatRequest setStream(boolean stream);

    MixChatRequest addMessage(MixChatMessage message);

    List<MixChatMessage> getMessages();

    MixChatRequest addTool(CommonToolDefinition toolDefinition);

    List<CommonToolDefinition> getTools();
}
