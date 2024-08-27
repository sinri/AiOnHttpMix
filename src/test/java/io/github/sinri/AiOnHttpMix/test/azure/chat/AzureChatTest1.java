package io.github.sinri.AiOnHttpMix.test.azure.chat;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGptRole;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.request.OpenAIChatGptRequest;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseChoice;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Test for Azure with Non SSE query.
 */
public class AzureChatTest1 extends AzureChatTestCore {
    private OpenAIChatGptRequest parameters;

    @Override
    protected @NotNull Future<Void> starting() {
        return super.starting()
                .compose(v -> {
                    parameters = OpenAIChatGptRequest.create()
                            .addMessage(m -> m.system("你是一个专业的IT工程师。"))
                            .addMessage(m -> m.user("Java 17和Java 21的差别是什么？"));
                    return Future.succeededFuture();
                });
    }

    @TestUnit(skip = false)
    public Future<Void> test1() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", parameters.toJsonObject());

        return new ChatGPTKit()
                .chat(getServiceMeta(), parameters.toJsonObject(), requestId)
                .compose(resp -> {
                    getLogger().info("RESP", resp);
                    return Future.succeededFuture();
                });
        /*
        ```json
        {
            "choices":[
                {
                    "content_filter_results":{
                        "hate":{"filtered":false,"severity":"safe"},
                        "self_harm":{"filtered":false,"severity":"safe"},
                        "sexual":{"filtered":false,"severity":"safe"},
                        "violence":{"filtered":false,"severity":"safe"}
                    },
                    "finish_reason":"stop",
                    "index":0,
                    "logprobs":null,
                    "message":{
                        "content":"随着Java 版本的更新，Java 17和Java 21之间有一些重要的变化和改进。以下是一些关键的差异和新特性：\n\n### Java 17的新特性和改进\nJava 17是一个长期支持（LTS）版本，发布于2021年9月。以下是一些Java 17的主要新特性和改进：\n1. **密封类（Sealed Classes）**：允许开发者限制其他类扩展或实现一个类。\n2. **模式匹配（Pattern matching for 'instanceof'）**：简化了使用`instanceof`的代码。\n3. **文本块（Text Blocks）**：使多行字符串更容易编写和阅读。\n4. **隐藏类（Hidden Classes）**：专门用于框架，允许定义类只在反射使用。\n5. **新包装流API（Wrapper Stream API）**：提供更多操作流的方法。\n6. **Patter Maching for switch (JEP 406)**：增强了switch的功能，引入了模式匹配。\n7. **环境化计算表达式（ENC）**：简化了对表达式的计算。\n\n### Java 21的新特性和改进\nJava 21预期在2023年发布。虽然一些特性可能仍在开发中，但以下是几个预期的新特性：\n\n1. **记录模式（Record Patterns）**：允许更加灵活地解构和处理记录类型。\n2. **焰火API（Firework API）**：简化并增强了并行编程。\n3. **通用模式匹配（General Pattern Matching for Java）**：扩大模式匹配的场景，包括在更多地方使用模式匹配。\n4. **数据类（Data Classes）**：进一步简化了数据类的定义和使用。\n5. **改进的垃圾回收（Improved Garbage Collection）**：包括新的GC算法和优化，提高应用程序的性能。\n6. **更多的API改进和增强功能**：在标准库和JVM性能方面都有增强。\n\n### 总结\n- **长期支持**：Java 17是LTS版本，通常业务会更多地选择LTS版本用于生产环境。\n- **语言特性**：Java 21预计会在语言特性和标准库上进行进一步的增强，提供更多现代化开发工具。\n- **性能和优化**：Java 21将包含更多性能和优化改进，尤其是在垃圾回收和并行计算方面。\n\n确保查看官方的Java文档和JEP（Java Enhancement Proposals）以获得最新和详细的信息。",
                        "role":"assistant"
                    }
                }
            ],
            "created":1724131855,
            "id":"chatcmpl-9yBkFMZOmT4C6htqr1C4iQxhKnfJp",
            "model":"gpt-4o-2024-05-13",
            "object":"chat.completion",
            "prompt_filter_results":[
                {
                    "prompt_index":0,
                    "content_filter_results":{
                        "hate":{"filtered":false,"severity":"safe"},
                        "self_harm":{"filtered":false,"severity":"safe"},
                        "sexual":{"filtered":false,"severity":"safe"},
                        "violence":{"filtered":false,"severity":"safe"}
                    }
                }
            ],
            "system_fingerprint":"fp_80a1bad4c7",
            "usage":{"completion_tokens":567,"prompt_tokens":32,"total_tokens":599}
        }
        ```
         */

    }


    @TestUnit(skip = false)
    public Future<Void> test3() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", parameters.toJsonObject());

        return new ChatGPTKit()
                .chat(getServiceMeta(), parameters, requestId)
                .compose(resp -> {
                    OpenAIChatGptResponseChoice choice = resp.getChoices().get(0);
                    var message = choice.getMessage();
                    ChatGptRole role = message.getRole();
                    String content = message.getContent();
                    getLogger().info("RESP | " + role + " | " + content);
                    return Future.succeededFuture();
                });
    }


}
