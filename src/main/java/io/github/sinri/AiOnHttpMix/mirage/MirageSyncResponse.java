package io.github.sinri.AiOnHttpMix.mirage;

import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMResponse;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class MirageSyncResponse extends UnmodifiableJsonifiableEntityImpl {
    public MirageSyncResponse(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    public String getRequestId() {
        return readString("request_id");
    }

    public JsonObject getData() {
        return readJsonObject("data");
    }

    /*
    {
        "request_id":"10.8.85.217-141946-fc266802-fcd5-4c79-8ace-e43db39518ec",
        "code":"OK",
        "data":{
            "choices":[
                {
                    "finish_reason":"stop",
                    "content":"在中华传统文化中，“阴阳”是一个非常重要的哲学概念，它来源于古代中国的自然观察和哲学思考。阴阳理论认为宇宙间的一切事物都可以用“阴”和“阳”两种基本属性来描述。“阴”通常与黑暗、寒冷、湿润、消极、内在等性质相关联；而“阳”则与光明、温暖、干燥、积极、外在等性质相联系。这两种属性是对立统一的关系，它们相互依存，又相互转化，共同构成了世界的多样性和动态平衡。\n\n在实践中，阴阳的概念被广泛应用于中医、风水、占卜、武术等多个领域。例如，在中医里，人体健康被视为体内阴阳平衡的结果，疾病则是由于这种平衡被打破造成的。通过调整饮食、生活方式或者使用草药治疗等方式，可以恢复体内的阴阳平衡，从而达到治病的目的。\n\n在《周易》这部古老的哲学著作中，阴阳理论得到了系统的阐述和发展，成为后来许多中华文化思想的基础之一。而在日本文化中，“阴阳”的概念也随着中国的文化影响传入，并在日本的宗教、艺术、建筑等方面产生了深远的影响，比如“阴阳道”，就是一种结合了中国阴阳五行学说和日本本土信仰的学问体系。\n\n作为现代人理解古代智慧的一种方式，阴阳的概念至今仍然具有启示意义，提醒人们在生活中注意平衡和谐的重要性。",
                    "function_calls":[]
                }
            ]
        }
     }
     */

    public AnyLLMResponse toAnyLLMResponse() {
        return AnyLLMResponse.wrap(getData());
    }
}
