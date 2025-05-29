package io.github.sinri.AiOnHttpMix.mix.chat.message;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.GPTMessage;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.GPTMessageInTextRequest;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.GPTMessageInVisionRequest;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.vision.GPTVisionMessageContent;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.vision.GPTVisionMessageContentImageUrl;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.message.QwenMessage;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.message.QwenMessageInChatRequest;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.message.QwenMessageInVisionRequest;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.DoubaoMessage;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.DoubaoMessageInChatRequest;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.DoubaoMessageInVisionRequest;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonToolCall;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

class MixChatMessageImpl extends JsonifiableEntityImpl<MixChatMessage>
        implements MixChatMessage {
    private final static String KEY_ROLE = "role";
    private final static String KEY_TEXT_CONTENT = "text_content";
    private final static String KEY_VISION_CONTENT = "vision_content";
    private final static String KEY_TOOL_CALL_ID = "tool_call_id";
    private final static String KEY_TOOL_CALLS = "tool_calls";
    private final static String KEY_REASONING_CONTENT = "reasoning_content";

    public MixChatMessageImpl() {
        super();
    }

    public MixChatMessageImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public MixChatMessage setRole(String role) {
        return write(KEY_ROLE, role);
    }

    @Override
    public String getRole() {
        return readString(KEY_ROLE);
    }

    @Override
    public MixChatMessage setTextContent(String content) {
        return write(KEY_TEXT_CONTENT, content);
    }

    @Override
    public String getTextContent() {
        return readString(KEY_TEXT_CONTENT);
    }

    @Override
    public MixChatMessage setReasoningContent(String reasoningContent) {
        return write(KEY_REASONING_CONTENT, reasoningContent);
    }

    @Override
    public String getReasoningContent() {
        return readString(KEY_REASONING_CONTENT);
    }

    @Override
    public MixChatMessage setToolCalls(List<ToolCall> toolCalls) {
        return write(KEY_TOOL_CALLS, new JsonArray(toolCalls
                .stream()
                .map(ToolCall::toJsonObject)
                .collect(Collectors.toList())));
    }

    @Override
    public List<ToolCall> getToolCalls() {
        var a = readJsonObjectArray(KEY_TOOL_CALLS);
        if (a == null) {
            return List.of();
        } else {
            return a.stream().map(CommonToolCall::new).collect(Collectors.toList());
        }
    }

    @Override
    public String getToolCallId() {
        return readString(KEY_TOOL_CALL_ID);
    }

    private JsonObject toCommonChatRequestJsonObject() {
        JsonObject j = new JsonObject();
        j.put("role", this.getRole());
        String textContent = this.getTextContent();
        if (textContent != null) {
            j.put("content", textContent);
        }
        var reasoningContent = this.getReasoningContent();
        if (reasoningContent != null) {
            j.put("reasoning_content", reasoningContent);
        }
        List<ToolCall> toolCalls = this.getToolCalls();
        if (!toolCalls.isEmpty()) {
            JsonArray array = new JsonArray();
            toolCalls.forEach(toolCall -> array.add(new JsonObject()
                    .put("id", toolCall.getId())
                    .put("type", toolCall.getType())
                    .put("function",
                            toolCall.getFunction() == null ? null
                                    : new JsonObject()
                                    .put("name", toolCall.getFunction().getName())
                                    .put("arguments", toolCall.getFunction().getArguments()))
                    .put("index", toolCall.getIndex())));
            j.put("tool_calls", array);
        }
        String toolCallId = this.getToolCallId();
        if (toolCallId != null) {
            j.put("tool_call_id", toolCallId);
        }
        return j;
    }

    @Override
    public GPTMessage toGPTMessageInTextChatRequest() {
        var j = toCommonChatRequestJsonObject();
        // note: GPT vision chat is not implemented
        return GPTMessageInTextRequest.wrap(j);
    }

    @Override
    public QwenMessage toQwenMessageInTextChatRequest() {
        var j = toCommonChatRequestJsonObject();
        return QwenMessageInChatRequest.wrap(j);
    }

    @Override
    public DoubaoMessage toDoubaoMessageInTextChatRequest() {
        var j = toCommonChatRequestJsonObject();
        return DoubaoMessageInChatRequest.wrap(j);
    }

    @Override
    public GPTMessage toGPTMessageInVisionChatRequest() {
        var x = GPTMessageInVisionRequest.create();

        x.setRole(getRole());
        x.setVisionContent(getVisionContent().stream().map(a -> {
            GPTVisionMessageContent b = GPTVisionMessageContent.create();
            switch (a.getType()) {
                case text -> b.setText(a.getText());
                case image -> b.setImageUrl(GPTVisionMessageContentImageUrl.create().setUrl(a.getImage()));
            }
            return b;
        }).toList());

        return x;
    }

    @Override
    public QwenMessage toQwenMessageInVisionChatRequest() {
        var j = toCommonChatRequestJsonObject();

        List<MixChatVisionContentElement> visionContent = getVisionContent();
        if (visionContent != null) {
            j.put(
                    "content",
                    new JsonArray(visionContent.stream()
                                               .map(MixChatVisionContentElement::toQwenVisionContent)
                                               .map(JsonifiableEntity::toJsonObject)
                                               .toList())
            );
        }

        return QwenMessageInVisionRequest.wrap(j);
    }

    @Override
    public DoubaoMessage toDoubaoMessageInVisionChatRequest() {
        var j = toCommonChatRequestJsonObject();

        List<MixChatVisionContentElement> visionContent = getVisionContent();
        if (visionContent != null) {
            j.put(
                    "content",
                    new JsonArray(visionContent.stream()
                                               .map(MixChatVisionContentElement::toDoubaoVisionContent)
                                               .map(JsonifiableEntity::toJsonObject)
                                               .toList())
            );
        }

        return DoubaoMessageInVisionRequest.wrap(j);
    }

    @Override
    public Integer getIndex() {
        return readInteger("index");
    }

    @Override
    public MixChatMessage setIndex(Integer index) {
        return write("index", index);
    }

    @Override
    public String getFinishReason() {
        return readString("finish_reason");
    }

    @Override
    public MixChatMessage setFinishReason(String finishReason) {
        return write("finish_reason", finishReason);
    }

    @Nonnull
    @Override
    public MixChatMessage getImplementation() {
        return this;
    }

    @Override
    public MixChatMessage setToolCallId(String toolCallId) {
        return write(KEY_TOOL_CALL_ID, toolCallId);
    }

    @Override
    public List<MixChatVisionContentElement> getVisionContent() {
        //List<MixChatVisionContentElement> list=new ArrayList<>();
        var a = readJsonObjectArray(KEY_VISION_CONTENT);
        if (a == null) {
            return List.of();
        } else {
            return a.stream().map(MixChatVisionContentElement::wrap).toList();
        }
    }

    @Override
    public MixChatMessage setVisionContent(List<MixChatVisionContentElement> content) {
        return write(KEY_VISION_CONTENT, new JsonArray(content.stream().map(JsonifiableEntity::toJsonObject).toList()));
    }
}
