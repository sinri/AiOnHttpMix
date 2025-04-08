package io.github.sinri.AiOnHttpMix.test.unit.anyllm;

import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMKit;
import io.vertx.core.Future;

import java.util.function.Function;

public interface AnyLLMUnitTestCommonMixin {
    AnyLLMKit createAnyLLMKit();

    default Future<Void> withAnyLLM(Function<AnyLLMKit, Future<Void>> function) {
        return Future.succeededFuture()
                     .compose(v -> {
                         AnyLLMKit anyLLMKit = createAnyLLMKit();
                         return function.apply(anyLLMKit);
                     });
    }

    //    default void async(Supplier<Future<Void>> testSupplier) {
    //        Keel.pseudoAwait(p -> {
    //            testSupplier.get().andThen(ar -> {
    //                if (ar.succeeded()) {
    //                    p.complete();
    //                } else {
    //                    p.fail(ar.cause());
    //                }
    //            });
    //        });
    //    }
}
