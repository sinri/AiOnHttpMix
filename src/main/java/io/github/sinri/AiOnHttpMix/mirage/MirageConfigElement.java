package io.github.sinri.AiOnHttpMix.mirage;

import io.github.sinri.keel.facade.configuration.KeelConfigElement;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * MirageConfigElement 用于封装 Mirage 相关的配置信息。
 * 继承自 KeelConfigElement，提供对 domain、client_code、client_secret 等字段的读取方法。
 * 该类通常用于读取和管理 Mirage 服务的配置信息。
 */
public class MirageConfigElement extends KeelConfigElement {
    /**
     * 通过另一个 KeelConfigElement 实例构造 MirageConfigElement。
     * 通常用于将已有的配置元素包装为 Mirage 配置。
     *
     * @param another 另一个 KeelConfigElement 实例
     */
    public MirageConfigElement(@Nonnull KeelConfigElement another) {
        super(another);
    }

    /**
     * 获取 Mirage 服务的域名（domain）配置。
     * @return 域名字符串
     */
    public String getDomain() {
        return readString(List.of("domain"));
    }

    /**
     * 获取 Mirage 服务的 client_code 配置。
     * @return client_code 字符串
     */
    public String getClientCode() {
        return readString(List.of("client_code"));
    }

    /**
     * 获取 Mirage 服务的 client_secret 配置。
     * @return client_secret 字符串
     */
    public String getClientSecret() {
        return readString(List.of("client_secret"));
    }
}
