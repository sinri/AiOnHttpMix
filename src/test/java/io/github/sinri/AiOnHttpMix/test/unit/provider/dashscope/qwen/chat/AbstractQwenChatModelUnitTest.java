package io.github.sinri.AiOnHttpMix.test.unit.provider.dashscope.qwen.chat;

import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.QwenServiceAdapter;
import io.github.sinri.AiOnHttpMix.test.unit.provider.core.AbstractModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.ServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.models.dashscope.qwen.QwenTextModelSeries;
import io.github.sinri.AiOnHttpMix.utils.specification.DashscopeModelSpecification;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import org.junit.Assert;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

/**
 * 针对Qwen系列LLM（即{@link DashscopeModelSpecification}），基于{@link QwenServiceAdapter}的单元测试抽象类。
 */
public abstract class AbstractQwenChatModelUnitTest extends AbstractModelUnitTest<QwenTextModelSeries> {
    private final QwenKit qwenKit;

    public AbstractQwenChatModelUnitTest() {
        qwenKit = new QwenKit((QwenServiceAdapter) getServiceAdapter());
    }

    public QwenKit getKit() {
        return qwenKit;
    }

    @Override
    protected QwenTextModelSeries buildModel() {
        return QwenTextModelSeries.model(QwenTextModelSeries.MODEL_NAME_OF_QWEN_PLUS_LATEST);
    }

    @Override
    protected final ServiceAdapter buildServiceAdapter() {
        KeelConfigElement dashscopeConfig = Keel.getConfiguration().extract("provider", "dashscope", "qwen");
        Assert.assertNotNull(dashscopeConfig);
        return getModel().buildServiceAdapter(dashscopeConfig);
    }

}
