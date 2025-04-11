package io.github.sinri.AiOnHttpMix.test.unit.mirage.vl;

import io.github.sinri.AiOnHttpMix.mirage.vl.MirageVLRequestEntity;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMRole;
import io.github.sinri.AiOnHttpMix.mix.vl.AnyVLLMMessageComponent;
import io.github.sinri.AiOnHttpMix.mix.vl.AnyVLLMMessageComponentType;
import io.github.sinri.AiOnHttpMix.mix.vl.AnyVLLMRoleMessagePair;
import io.github.sinri.AiOnHttpMix.test.unit.core.AnyUnitTest;

import java.util.List;

public abstract class AnyMirageVLUnitTest extends AnyUnitTest
        implements AnyMirageVLUnitTestCommonMixin, AnyMirageVLUnitTestWithoutFCMixin {

    @Override
    public void setUp() {
        super.setUp();
    }

    @Override
    public MirageVLRequestEntity generateVLRequestWithoutFC() {
        MirageVLRequestEntity request = new MirageVLRequestEntity();
        request.addToPrompt(new AnyVLLMRoleMessagePair(AnyLLMRole.user, List.of(
                new AnyVLLMMessageComponent(AnyVLLMMessageComponentType.text, "介绍图中的旗帜由来"),
                new AnyVLLMMessageComponent(AnyVLLMMessageComponentType.image, "https://pics6.baidu.com/feed/8601a18b87d6277f6438cea9e7199b3ee924fc2e.jpeg@f_auto?token=cb586c2ff83aefa74a3365453f55eaba")
        )));
        request.setMaxExecutionSeconds(30);
        return request;
    }
}
