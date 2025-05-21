package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.moonshot;

import io.github.sinri.AiOnHttpMix.test.unit.provider.volces.AbstractVolcesServiceAdapterUnitTest;
import io.github.sinri.AiOnHttpMix.utils.models.volces.moonshot.VolcesMoonshotModelSeries;

public class AbstractVolcesMoonshotServiceAdapterUnitTest extends AbstractVolcesServiceAdapterUnitTest {
    protected final VolcesMoonshotModelSeries moonshotV1;

    public AbstractVolcesMoonshotServiceAdapterUnitTest() {
        super();
        moonshotV1 = new VolcesMoonshotModelSeries.Builder().build(VolcesMoonshotModelSeries.MODEL_NAME_OF_MOONSHOT_V1_128k);
    }
}
