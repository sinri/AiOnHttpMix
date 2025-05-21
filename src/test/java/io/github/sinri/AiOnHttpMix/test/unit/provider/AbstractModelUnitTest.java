package io.github.sinri.AiOnHttpMix.test.unit.provider;

import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;
import io.github.sinri.keel.facade.tesuto.unit.KeelUnitTest;
import org.junit.Before;

/**
 * 针对同一系列的LLM服务，基于指定的{@link ChatModelServiceAdapter}（对应{@link ModelSpecification}）进行单元测试的抽象类。
 */
public abstract class AbstractModelUnitTest<M extends ChatModel>
        extends KeelUnitTest {
    private ChatModelServiceAdapter serviceAdapter;

    abstract protected M getModel();

    abstract protected ChatModelServiceAdapter buildServiceAdapter();

    @Before
    @Override
    public void setUp() {
        serviceAdapter = buildServiceAdapter();
    }

    protected ChatModelServiceAdapter getServiceAdapter() {
        return serviceAdapter;
    }
}
