package io.github.sinri.AiOnHttpMix.test.unit.mix.mixin.vision;

import io.github.sinri.AiOnHttpMix.test.unit.mix.mixin.MixCoreMixin;

public interface MixVisionCoreMixin extends MixCoreMixin {
    default String getImageUrl() {
        return "https://mmbiz.qpic.cn/sz_mmbiz_jpg/MWpJc0Ao1ic034EWO3nPkVh12H6Y44vjekM7JUBOyYKViartbR9TbUvB4KgxpXJ7O8G7HdDHpiaibATCqLHmJe6IPA/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1";
    }
}
