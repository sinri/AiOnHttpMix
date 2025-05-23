package io.github.sinri.AiOnHttpMix.test.unit.provider.core;

import io.github.sinri.AiOnHttpMix.utils.ServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;
import io.github.sinri.keel.facade.tesuto.unit.KeelUnitTest;

/**
 * 针对同一系列的LLM服务，基于指定的{@link ServiceAdapter}（对应{@link ModelSpecification}）进行单元测试的抽象类。
 */
public abstract class AbstractModelUnitTest<M extends ChatModel>
        extends KeelUnitTest
        implements ModelServiceMixin<M> {
    private final ServiceAdapter serviceAdapter;
    private final M model;

    public AbstractModelUnitTest() {
        model = buildModel();
        serviceAdapter = buildServiceAdapter();
    }

    abstract protected M buildModel();

    public final M getModel() {
        return model;
    }

    /**
     * For the created model (with {@link AbstractModelUnitTest#buildModel()}), build the service adapter.
     */
    abstract protected ServiceAdapter buildServiceAdapter();

    public final ServiceAdapter getServiceAdapter() {
        return serviceAdapter;
    }
}
