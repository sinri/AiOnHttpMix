package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.moonshot;

import io.github.sinri.AiOnHttpMix.test.unit.provider.volces.AbstractVolcesModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.volces.moonshot.VolcesMoonshotModelSeries;

public class AbstractVolcesMoonshotModelUnitTest extends AbstractVolcesModelUnitTest<VolcesMoonshotModelSeries> {
    private final VolcesMoonshotModelSeries moonshotV1;

    public AbstractVolcesMoonshotModelUnitTest() {
        super();
        moonshotV1 = VolcesMoonshotModelSeries.model(VolcesMoonshotModelSeries.MODEL_NAME_OF_MOONSHOT_V1_128k);
    }

    @Override
    protected VolcesMoonshotModelSeries getModel() {
        return moonshotV1;
    }

}
