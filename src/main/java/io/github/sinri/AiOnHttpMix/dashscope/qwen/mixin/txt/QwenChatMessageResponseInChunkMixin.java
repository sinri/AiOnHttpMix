package io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.txt;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.chunk.ChatMessageResponseInChunkImpl;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public interface QwenChatMessageResponseInChunkMixin extends ChatResponseBase {
    OutputChunkForMessageResponse getOutput();

    interface OutputChunkForMessageResponse extends UnmodifiableJsonifiableEntity {
        static OutputChunkForMessageResponse wrap(JsonObject jsonObject) {
            return new ChatMessageResponseInChunkImpl.OutputChunkForMessageResponseImpl(jsonObject);
        }
        /*
        {
            "choices":[
                {
                    "message":{
                        "content":"一个大型组织可能会",
                        "role":"assistant"
                    },
                    "finish_reason":"null"
                }
            ]
         }
         ---
         {
            "choices":[
                {
                    "message":{
                        "tool_calls":[
                            {
                                "index":0,
                                "id":"call_fb1668a569444758942988",
                                "type":"function",
                                "function":{
                                    "name":"searchDataSet",
                                    "arguments":""
                                }
                            }
                        ],
                        "content":"",
                        "role":"assistant"
                    },
                    "finish_reason":"null"
                }
            ]
         }
         */

        @NotNull
        default List<Choice> getChoices() {
            List<JsonObject> choices = readJsonObjectArray("choices");
            Objects.requireNonNull(choices);
            return choices.stream().map(Choice::wrap).toList();
        }

        interface Choice extends UnmodifiableJsonifiableEntity {
            static Choice wrap(JsonObject jsonObject) {
                return new ChatMessageResponseInChunkImpl.ChoiceImpl(jsonObject);
            }

            default QwenKit.Message getMessage() {
                return QwenKit.Message.wrap(readJsonObject("message"));
            }

            @Nullable
            default String getFinishReason() {
                return readString("finish_reason");
            }


//            @Deprecated(forRemoval = true)
//            interface Message extends QwenKit.Message {
//                static Message wrap(JsonObject jsonObject) {
//                    return new ChatMessageResponseInChunkImpl.MessageImpl(jsonObject);
//                }
//            }
        }
    }

}
