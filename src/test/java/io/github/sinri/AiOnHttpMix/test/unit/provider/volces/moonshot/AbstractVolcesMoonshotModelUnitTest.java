package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.moonshot;

import io.github.sinri.AiOnHttpMix.test.unit.provider.volces.AbstractVolcesTextModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.volces.moonshot.VolcesMoonshotTextModelSeries;

public class AbstractVolcesMoonshotModelUnitTest extends AbstractVolcesTextModelUnitTest<VolcesMoonshotTextModelSeries> {

    public AbstractVolcesMoonshotModelUnitTest() {
        super();
    }

    @Override
    protected VolcesMoonshotTextModelSeries buildModel() {
        return VolcesMoonshotTextModelSeries.model(VolcesMoonshotTextModelSeries.MODEL_NAME_OF_MOONSHOT_V1_128k);
    }
}
