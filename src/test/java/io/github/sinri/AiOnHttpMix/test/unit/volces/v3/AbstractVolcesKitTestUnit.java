package io.github.sinri.AiOnHttpMix.test.unit.volces.v3;

import io.github.sinri.AiOnHttpMix.test.unit.AnyKitUnitTest;
import io.github.sinri.AiOnHttpMix.volces.core.VolcesServiceMeta;
import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public abstract class AbstractVolcesKitTestUnit extends AnyKitUnitTest<VolcesServiceMeta, VolcesKit> {
    abstract protected String getServiceName();

    @Override
    protected VolcesServiceMeta generateServiceMeta() {
        String serviceName = getServiceName();
        String apiKey = Keel.config("volces." + serviceName + ".apiKey");
        String model = Keel.config("volces." + serviceName + ".model");

        assert apiKey != null;
        assert model != null;
        return new VolcesServiceMeta(apiKey, model);
    }

    @Override
    protected VolcesKit generateKit() {
        return new VolcesKit();
    }
}
