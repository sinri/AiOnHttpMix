package io.github.sinri.AiOnHttpMix.provider.volces.doubao.request;

import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface ThinkingOptions extends JsonifiableEntity<ThinkingOptions> {
    static ThinkingOptions create() {
        return new ThinkingOptionsImpl();
    }

    static ThinkingOptions wrap(JsonObject jsonObject) {
        return new ThinkingOptionsImpl(jsonObject);
    }

    /**
     * @param type 取值范围：{@code enabled}， {@code disabled}，{@code auto}。
     *             {@code enabled}：开启思考模式，模型一定先思考后回答。
     *             {@code disabled}：关闭思考模式，模型直接回答问题，不会进行思考。
     *             {@code auto}：自动思考模式，模型根据问题自主判断是否需要思考，简单题目直接回答。该取值只有doubao-1-5-thinking-pro-m-250428模型支持。
     */
    default ThinkingOptions type(String type) {
        return write("type", type);
    }

    default String type() {
        return readString("type");
    }
}
