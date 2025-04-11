package io.github.sinri.AiOnHttpMix.test.unit.mirage.vl;

import io.github.sinri.AiOnHttpMix.mirage.vl.MirageVLSDK;
import io.vertx.core.Future;

import java.util.function.Function;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public interface AnyMirageVLUnitTestCommonMixin {
    default MirageVLSDK generateMirageVLSDK() {
        var domain = Keel.config("mirage.domain");
        var clientCode = Keel.config("mirage.client_code");
        var clientSecret = Keel.config("mirage.client_secret");

        return new MirageVLSDK(domain, clientCode, clientSecret);
    }

    default Future<Void> withMirage(Function<MirageVLSDK, Future<Void>> function) {
        return Future.succeededFuture()
                     .compose(v -> {
                         MirageVLSDK mirageVLSDK = generateMirageVLSDK();
                         return function.apply(mirageVLSDK);
                     });
    }

    String getModel();

    String getService();
}
