package io.github.sinri.AiOnHttpMix.test.unit.provider;

import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.series.ChatModelSeries;
import io.github.sinri.keel.facade.tesuto.unit.KeelUnitTest;
import org.junit.Before;

/**
 * 针对同一系列的LLM服务，基于指定的{@link ChatModelServiceAdapter}（对应{@link ChatModelSeries}）进行单元测试的抽象类。
 */
public abstract class AbstractServiceAdapterUnitTest<S extends ChatModelSeries, A extends ChatModelServiceAdapter>
        extends KeelUnitTest {
    private A serviceAdapter;

    abstract protected S getModelSeries();

    abstract protected A buildServiceAdapter();

    @Before
    @Override
    public void setUp() {
        serviceAdapter = buildServiceAdapter();
    }

    protected final A getServiceAdapter() {
        return serviceAdapter;
    }
}
