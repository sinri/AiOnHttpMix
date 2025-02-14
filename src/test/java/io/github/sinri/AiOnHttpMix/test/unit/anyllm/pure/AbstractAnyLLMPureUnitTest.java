package io.github.sinri.AiOnHttpMix.test.unit.anyllm.pure;

import io.github.sinri.AiOnHttpMix.test.unit.anyllm.AbstractAnyLLMUnitTest;
import io.github.sinri.AiOnHttpMix.utils.ServiceMeta;

public abstract class AbstractAnyLLMPureUnitTest<S extends ServiceMeta> extends AbstractAnyLLMUnitTest {
    abstract protected S generateServiceMeta();
}
