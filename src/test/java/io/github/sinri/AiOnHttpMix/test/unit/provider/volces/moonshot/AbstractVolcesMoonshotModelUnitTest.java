package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.moonshot;

import io.github.sinri.AiOnHttpMix.test.unit.provider.volces.AbstractVolcesModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.volces.moonshot.VolcesMoonshotChatModelSeries;

public class AbstractVolcesMoonshotModelUnitTest extends AbstractVolcesModelUnitTest<VolcesMoonshotChatModelSeries> {

    public AbstractVolcesMoonshotModelUnitTest() {
        super();
    }

    @Override
    protected VolcesMoonshotChatModelSeries buildModel() {
        return VolcesMoonshotChatModelSeries.model(VolcesMoonshotChatModelSeries.MODEL_NAME_OF_MOONSHOT_V1_128k);
    }
}
