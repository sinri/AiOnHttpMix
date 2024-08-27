package io.github.sinri.AiOnHttpMix.dashscope.qwen.text.chunk;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.QwenResponseBase;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.message.QwenMessage;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public interface QwenResponseChunk extends QwenResponseBase {
    static QwenResponseChunk parse(String string) {
        return wrap(new JsonObject(string));
    }

    static QwenResponseChunk wrap(JsonObject jsonObject) {
        return new QwenResponseChunkImpl(jsonObject);
    }
    OutputChunkForMessageResponse getOutput();

    interface OutputChunkForMessageResponse extends UnmodifiableJsonifiableEntity {
        static OutputChunkForMessageResponse wrap(JsonObject jsonObject) {
            return new QwenResponseChunkImpl.OutputChunkForMessageResponseImpl(jsonObject);
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
                return new QwenResponseChunkImpl.ChoiceImpl(jsonObject);
            }

            default QwenMessage getMessage() {
                return QwenMessage.wrap(readJsonObject("message"));
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
