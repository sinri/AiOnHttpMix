package io.github.sinri.AiOnHttpMix.test.unit.provider.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.QwenServiceAdapter;
import io.github.sinri.AiOnHttpMix.test.unit.provider.AbstractServiceAdapterUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.dashscope.qwen.QwenModelSeries;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.QwenModelSpecification;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import org.junit.Assert;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

/**
 * 针对Qwen系列LLM（即{@link QwenModelSpecification}），基于{@link QwenServiceAdapter}的单元测试抽象类。
 */
public abstract class AbstractQwenServiceAdapterUnitTest extends AbstractServiceAdapterUnitTest<QwenModelSpecification, QwenServiceAdapter> {
    protected final QwenModelSeries qwenPlusLatest;
    protected final QwenModelSeries qwenPlus;

    public AbstractQwenServiceAdapterUnitTest() {
        qwenPlusLatest = new QwenModelSeries.Builder().build(QwenModelSeries.MODEL_NAME_OF_QWEN_PLUS_LATEST);
        qwenPlus = new QwenModelSeries.Builder().build(QwenModelSeries.MODEL_NAME_OF_QWEN_PLUS);
    }

    @Override
    protected final QwenModelSpecification getModelSeries() {
        return ModelSpecification.qwen;
    }

    @Override
    protected final QwenServiceAdapter buildServiceAdapter() {
        KeelConfigElement dashscopeConfig = Keel.getConfiguration().extract("provider", "dashscope", "qwen");
        Assert.assertNotNull(dashscopeConfig);
        return (QwenServiceAdapter) getModelSeries().getServiceProvider()
                                                    .buildServiceAdapter(getModelSeries(), dashscopeConfig);
    }
}
