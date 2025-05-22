package io.github.sinri.AiOnHttpMix.test.unit.provider.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.QwenServiceAdapter;
import io.github.sinri.AiOnHttpMix.test.unit.provider.AbstractModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.models.dashscope.qwen.QwenChatModelSeries;
import io.github.sinri.AiOnHttpMix.utils.specification.DashscopeModelSpecification;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import org.junit.Assert;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

/**
 * 针对Qwen系列LLM（即{@link DashscopeModelSpecification}），基于{@link QwenServiceAdapter}的单元测试抽象类。
 */
public abstract class AbstractQwenModelUnitTest extends AbstractModelUnitTest<QwenChatModelSeries> {
    private final QwenChatModelSeries qwenPlusLatest;
    private final QwenKit qwenKit;
    public AbstractQwenModelUnitTest() {
        qwenPlusLatest = QwenChatModelSeries.model(QwenChatModelSeries.MODEL_NAME_OF_QWEN_PLUS_LATEST);
        qwenKit =new QwenKit();
    }

    public QwenKit getKit() {
        return qwenKit;
    }

    @Override
    protected QwenChatModelSeries getModel() {
        return qwenPlusLatest;
    }

    @Override
    protected final ChatModelServiceAdapter buildServiceAdapter() {
        KeelConfigElement dashscopeConfig = Keel.getConfiguration().extract("provider", "dashscope", "qwen");
        Assert.assertNotNull(dashscopeConfig);
        return getModel().buildServiceAdapter(dashscopeConfig);
    }

    @Override
    protected QwenServiceAdapter getServiceAdapter() {
        return (QwenServiceAdapter) super.getServiceAdapter();
    }
}
