package io.github.sinri.AiOnHttpMix.test.unit.anyvllm;

import io.github.sinri.AiOnHttpMix.mix.AnyLLMRole;
import io.github.sinri.AiOnHttpMix.mix.vl.*;
import io.github.sinri.AiOnHttpMix.test.unit.core.AnyUnitTest;
import io.vertx.core.Future;

import java.util.List;
import java.util.function.Function;

public abstract class AbstractAnyVLLMUnitTest extends AnyUnitTest {

    protected abstract AnyVLLMServiceAdapter buildAnyVLLMServiceAdapter();

    protected <R> Future<R> withAnyVLLMKit(Function<AnyVLLMKit, Future<R>> usage) {
        AnyVLLMKit anyVLLMKit = new AnyVLLMKit();
        anyVLLMKit.setServiceAdapter(buildAnyVLLMServiceAdapter());

        return usage.apply(anyVLLMKit);
    }

    protected AnyVLLMRequest buildVLLMRequest() {
        return AnyVLLMRequest.create()
                             .addMessage(AnyLLMRole.user, List.of(
                                     new AnyVLLMMessageComponent(AnyVLLMMessageComponentType.image, "https://pics6.baidu.com/feed/8601a18b87d6277f6438cea9e7199b3ee924fc2e.jpeg@f_auto?token=cb586c2ff83aefa74a3365453f55eaba"),
                                     new AnyVLLMMessageComponent(AnyVLLMMessageComponentType.text, "请介绍一下图中人物所在国家的地缘战略")
                             ));
    }
}
