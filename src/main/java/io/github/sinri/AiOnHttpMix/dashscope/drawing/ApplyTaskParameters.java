package io.github.sinri.AiOnHttpMix.dashscope.drawing;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

public class ApplyTaskParameters extends SimpleJsonifiableEntity {
    public ApplyTaskParameters() {
        super();
    }

    public ApplyTaskParameters(JsonObject jsonObject) {
        super(jsonObject);
    }

    public String getStyle() {
        return readString("style");
    }

    /**
     * 输出图像的风格，目前支持以下风格取值：
     * "<photography>" 摄影,
     * "<portrait>" 人像写真,
     * "<3d cartoon>" 3D卡通,
     * "<anime>" 动画,
     * "<oil painting>" 油画,
     * "<watercolor>"水彩,
     * "<sketch>" 素描,
     * "<chinese painting>" 中国画,
     * "<flat illustration>" 扁平插画,
     * "<auto>" 默认
     */
    public ApplyTaskParameters setStyle(String style) {
        this.toJsonObject().put("style", style);
        return this;
    }

    public String getSize() {
        return readString("size");
    }

    /**
     * 生成图像的分辨率，目前仅支持'1024*1024', '720*1280', '1280*720'三种分辨率，默认为1024*1024像素。
     */
    public ApplyTaskParameters setSize(String size) {
        this.toJsonObject().put("size", size);
        return this;
    }

    public int getN() {
        return Objects.requireNonNullElse(readInteger("n"), 1);
    }

    /**
     * 本次请求生成的图片数量，目前支持1~4张，默认为1。
     */
    public ApplyTaskParameters setN(int n) {
        this.toJsonObject().put("n", n);
        return this;
    }

    public Integer getSeed() {
        return readInteger("seed");
    }

    /**
     * 图片生成时候的种子值，
     * 如果不提供，则算法自动用一个随机生成的数字作为种子，
     * 如果给定了，则根据 batch 数量分别生成 seed, seed+1, seed+2, seed+3 为参数的图片。
     */
    public ApplyTaskParameters setSeed(int seed) {
        this.toJsonObject().put("seed", seed);
        return this;
    }
}
