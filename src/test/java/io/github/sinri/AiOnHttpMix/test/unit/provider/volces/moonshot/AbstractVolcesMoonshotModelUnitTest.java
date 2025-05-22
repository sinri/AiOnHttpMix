package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.moonshot;

import io.github.sinri.AiOnHttpMix.test.unit.provider.volces.AbstractVolcesModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.volces.moonshot.VolcesMoonshotChatModelSeries;

public class AbstractVolcesMoonshotModelUnitTest extends AbstractVolcesModelUnitTest<VolcesMoonshotChatModelSeries> {
    private final VolcesMoonshotChatModelSeries moonshotV1;

    public AbstractVolcesMoonshotModelUnitTest() {
        super();
        moonshotV1 = VolcesMoonshotChatModelSeries.model(VolcesMoonshotChatModelSeries.MODEL_NAME_OF_MOONSHOT_V1_128k);
    }

    @Override
    protected VolcesMoonshotChatModelSeries getModel() {
        return moonshotV1;
    }

}
