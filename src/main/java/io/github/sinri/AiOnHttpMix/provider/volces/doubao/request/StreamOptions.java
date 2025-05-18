package io.github.sinri.AiOnHttpMix.provider.volces.doubao.request;

import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface StreamOptions extends JsonifiableEntity<StreamOptions> {

    static StreamOptions create() {
        return new StreamOptionsImpl();
    }

    static StreamOptions wrap(JsonObject x) {
        return new StreamOptionsImpl(x);
    }

    /**
     * 是否包含本次请求的 token 用量统计信息。
     *
     * @param b {@code false}：不返回 token 用量信息。
     *          {@code true}：在 data: [DONE] 消息之前会返回一个额外的块，此块上的 usage 字段代表整个请求的 token 用量，choices 字段为空数组。所有其他块也将包含
     *          usage 字段，但值为 null。
     */
    default StreamOptions includeUsage(boolean b) {
        return this.write("include_usage", b);
    }

    default Boolean includeUsage() {
        return readBoolean("include_usage");
    }
}
