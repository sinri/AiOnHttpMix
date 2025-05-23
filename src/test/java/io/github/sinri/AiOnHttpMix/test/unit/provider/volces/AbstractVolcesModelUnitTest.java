package io.github.sinri.AiOnHttpMix.test.unit.provider.volces;

import io.github.sinri.AiOnHttpMix.provider.volces.VolcesKit;
import io.github.sinri.AiOnHttpMix.provider.volces.VolcesServiceAdapter;
import io.github.sinri.AiOnHttpMix.test.unit.provider.core.AbstractModelUnitTest;
import io.github.sinri.AiOnHttpMix.utils.ServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.models.volces.VolcesChatModelSeries;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;
import org.junit.Assert;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public abstract class AbstractVolcesModelUnitTest<M extends VolcesChatModelSeries> extends AbstractModelUnitTest<M> {
    private final VolcesKit kit;

    public AbstractVolcesModelUnitTest() {
        super();
        kit = new VolcesKit((VolcesServiceAdapter) getServiceAdapter());
    }

    protected VolcesKit getKit() {
        return kit;
    }

    @Override
    protected final ServiceAdapter buildServiceAdapter() {
        KeelConfigElement doubaoConfig = Keel.getConfiguration()
                                             .extract("provider", "volces");
        Assert.assertNotNull(doubaoConfig);

        return getModel().buildServiceAdapter(doubaoConfig);
    }
}
