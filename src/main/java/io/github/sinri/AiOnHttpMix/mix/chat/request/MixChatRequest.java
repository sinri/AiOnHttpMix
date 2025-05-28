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

/**
 * MixChatRequest 接口定义了混合聊天请求的结构和操作方法，
 * 支持多种大模型（如 OpenAI GPT、Qwen、Doubao）请求的统一封装与转换。
 * 该接口允许设置模型、请求ID、超时时间、流式响应、消息列表和工具列表等参数，
 * 并可根据当前配置生成对应平台的请求对象。
 */
public interface MixChatRequest extends JsonifiableEntity<MixChatRequest> {

    /**
     * 创建一个空的 MixChatRequest 实例。
     * @return 新的 MixChatRequest 实例
     */
    static MixChatRequest create() {
        return new MixChatRequestImpl();
    }

    /**
     * 通过 JsonObject 包装生成 MixChatRequest 实例。
     * @param jsonObject 包含请求参数的 JsonObject
     * @return 包装后的 MixChatRequest 实例
     */
    static MixChatRequest wrap(JsonObject jsonObject) {
        return new MixChatRequestImpl(jsonObject);
    }

    /**
     * 获取当前请求所选用的支持模型枚举。
     * @return SupportedModelEnum 枚举值
     */
    SupportedModelEnum getSupportedModelEnum();

    /**
     * 设置当前请求所选用的支持模型枚举。
     * @param supportedModelEnum 支持的模型枚举
     * @return 当前 MixChatRequest 实例
     */
    MixChatRequest setSupportedModelEnum(SupportedModelEnum supportedModelEnum);

    /**
     * 获取当前请求所选用的聊天模型对象。
     * @return ChatModel 实例
     */
    ChatModel getChatModel();

    /**
     * 转换为 OpenAI GPTRequest 对象。
     * @return GPTRequest 实例
     */
    GPTRequest toGPTRequest();

    /**
     * 转换为 QwenRequest 对象。
     * @return QwenRequest 实例
     */
    QwenRequest toQwenRequest();

    /**
     * 转换为 DoubaoRequest 对象。
     * @return DoubaoRequest 实例
     */
    DoubaoRequest toDoubaoRequest();

    /**
     * 获取请求唯一标识 request_id。
     * @return 请求 ID 字符串
     */
    String getRequestId();

    /**
     * 设置请求唯一标识 request_id。
     * @param requestId 请求 ID 字符串
     * @return 当前 MixChatRequest 实例
     */
    MixChatRequest setRequestId(String requestId);

    /**
     * 获取请求超时时间（毫秒），默认 180_000 ms。
     * @return 超时时间（毫秒）
     */
    long getTimeout();

    /**
     * 设置请求超时时间（毫秒）。
     * @param timeout 超时时间（毫秒）
     * @return 当前 MixChatRequest 实例
     */
    MixChatRequest setTimeout(long timeout);

    /**
     * 设置是否为流式响应。
     * @param stream 是否流式
     * @return 当前 MixChatRequest 实例
     */
    MixChatRequest setStream(boolean stream);

    /**
     * 添加一条聊天消息到消息列表。
     * @param message 聊天消息
     * @return 当前 MixChatRequest 实例
     */
    MixChatRequest addMessage(MixChatMessage message);

    /**
     * 获取所有聊天消息列表。
     * @return 聊天消息列表
     */
    List<MixChatMessage> getMessages();

    /**
     * 添加一个工具定义到工具列表。
     * @param toolDefinition 工具定义
     * @return 当前 MixChatRequest 实例
     */
    MixChatRequest addTool(CommonToolDefinition toolDefinition);

    /**
     * 获取所有工具定义列表。
     * @return 工具定义列表
     */
    List<CommonToolDefinition> getTools();
}
