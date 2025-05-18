package io.github.sinri.AiOnHttpMix.utils.specification;

import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;

public class DoubaoModelSpecification implements ModelSpecification {
    public static final String SPECIFICATION_NAME = "Doubao";
    public static final String pathOfV3ChatCompletions = "/api/v3/chat/completions";
    public static final String hostOfV3ChatCompletions = "ark.cn-beijing.volces.com";

    DoubaoModelSpecification() {

    }

    @Override
    public ServiceProvider getServiceProvider() {
        return ServiceProvider.volces;
    }

    @Override
    public String getSpecificationName() {
        return SPECIFICATION_NAME;
    }
}
