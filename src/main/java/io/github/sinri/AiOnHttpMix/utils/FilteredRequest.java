package io.github.sinri.AiOnHttpMix.utils;

public class FilteredRequest extends RuntimeException {
    public FilteredRequest() {
        super("该请求犯了天条已被过滤。");
    }
}
