package io.github.sinri.AiOnHttpMix.test.unit.mirage;

import io.github.sinri.AiOnHttpMix.mirage.MirageSDK;
import io.vertx.core.Future;

import java.util.function.Function;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public interface AnyMirageUnitTestCommonMixin {
    default MirageSDK generateMirageSDK() {
        var domain = Keel.config("mirage.domain");
        var clientCode = Keel.config("mirage.client_code");
        var clientSecret = Keel.config("mirage.client_secret");

        return new MirageSDK(domain, clientCode, clientSecret);
    }

    default Future<Void> withMirage(Function<MirageSDK, Future<Void>> function) {
        return Future.succeededFuture()
                     .compose(v -> {
                         MirageSDK mirageSDK = generateMirageSDK();
                         return function.apply(mirageSDK);
                     });
    }

    String getModel();

    String getService();
}
