package io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt.tool;

import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolCall;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

public class ChatGPTToolCall extends UnmodifiableJsonifiableEntityImpl implements ToolCall {
    public ChatGPTToolCall(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public String getType() {
        return readString("type");
    }

    @Override
    public String getId() {
        return readString("id");
    }

    @Override
    public Integer getIndex() {
        return readInteger("index");
    }

    @Override
    public FunctionToolCall getFunction() {
        var x=readJsonObject("function");
        if(x==null)return null;
        return new ChatGPTFunctionToolCall(x);
    }
}
