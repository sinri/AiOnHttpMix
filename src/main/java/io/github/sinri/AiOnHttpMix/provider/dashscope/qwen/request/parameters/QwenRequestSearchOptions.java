package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.request.parameters;

import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;
/**
 * @since 2.0.0
 */
public interface QwenRequestSearchOptions extends JsonifiableEntity<QwenRequestSearchOptions> {
    static QwenRequestSearchOptions create() {
        return new QwenRequestParametersImpl.QwenRequestSearchOptionsImpl();
    }

    static QwenRequestSearchOptions wrap(JsonObject jsonObject) {
        return new QwenRequestParametersImpl.QwenRequestSearchOptionsImpl(jsonObject);
    }

    /**
     * 是否在返回结果中展示搜索来源信息。
     */
    default QwenRequestSearchOptions enableSource(boolean enableSource) {
        this.write("enable_source", enableSource);
        return this;
    }

    default Boolean enableSource() {
        return readBoolean("enable_source");
    }

    /**
     * 是否在返回结果中展示引用信息。
     * 是否开启[1]或[ref_1]样式的角标标注功能。在enable_source为true时生效。
     */
    default QwenRequestSearchOptions enableCitation(boolean enableCitation) {
        this.write("enable_citation", enableCitation);
        return this.getImplementation();
    }

    default Boolean enableCitation() {
        return readBoolean("enable_citation");
    }

    /**
     * 角标样式。在enable_citation为true时生效。
     * 参数值：
     * {@code [<number>]}：角标形式为{@code [1]}；
     * {@code [ref_<number>]}：角标形式为{@code [ref_1]}。
     */
    default QwenRequestSearchOptions citationFormat(String citationFormat) {
        this.write("citation_format", citationFormat);
        return this.getImplementation();
    }

    default String citationFormat() {
        return readString("citation_format");
    }

    /**
     * 是否强制开启搜索。
     */
    default QwenRequestSearchOptions forcedSearch(boolean forcedSearch) {
        this.write("forced_search", forcedSearch);
        return this.getImplementation();
    }

    default Boolean forcedSearch() {
        return readBoolean("forced_search");
    }

    /**
     * 搜索互联网信息的数量。
     * <p>
     * 可选值：<br>
     * - "standard": 搜索5条互联网信息<br>
     * - "pro": 搜索10条互联网信息<br>
     * </p>
     */
    default QwenRequestSearchOptions searchStrategy(String searchStrategy) {
        this.write("search_strategy", searchStrategy);
        return this.getImplementation();
    }

    default String searchStrategy() {
        return readString("search_strategy");
    }
}
