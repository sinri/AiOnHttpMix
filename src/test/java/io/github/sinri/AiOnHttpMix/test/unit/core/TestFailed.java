package io.github.sinri.AiOnHttpMix.test.unit.core;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD})
public @interface TestFailed {
    String time();

    String note();
}
