package io.github.sinri.AiOnHttpMix.dashscope.vl;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public class QwenVLParameters extends SimpleJsonifiableEntity {
    public QwenVLParameters() {
        super();
    }

    public QwenVLParameters(JsonObject jsonObject) {
        super(jsonObject);
    }

    /**
     * 生成时，核采样方法的概率阈值。
     * 例如，取值为0.8时，仅保留累计概率之和大于等于0.8的概率分布中的token，作为随机采样的候选集。
     * 取值范围为(0,1.0)，取值越大，生成的随机性越高；取值越低，生成的随机性越低。
     * 默认值 0.8。注意，取值不要大于等于1
     *
     * @param topP (0,1,0), default 0.8.
     */
    public QwenVLParameters setTopP(float topP) {
        this.jsonObject.put("top_p", topP);
        return this;
    }

    /**
     * 生成时，采样候选集的大小。
     * 例如，取值为50时，仅将单次生成中得分最高的50个token组成随机采样的候选集。
     * 取值越大，生成的随机性越高；取值越小，生成的确定性越高。
     * 注意：如果top_k的值大于100，top_k将采用默认值100
     *
     * @param topK [1,100], default 100.
     */
    public QwenVLParameters setTopK(int topK) {
        this.jsonObject.put("top_k", topK);
        return this;
    }

    /**
     * 生成时，随机数的种子，用于控制模型生成的随机性。
     * 如果使用相同的种子，每次运行生成的结果都将相同；当需要复现模型的生成结果时，可以使用相同的种子。
     * seed参数支持无符号64位整数类型。
     *
     * @param seed 默认值 1234
     */
    public QwenVLParameters setSeed(int seed) {
        this.jsonObject.put("seed", seed);
        return this;
    }

    /**
     * 是否使用增量输出。
     *
     * @param incrementalOutput 当使用增量输出时每次流式返回的序列仅包含最新生成的增量内容，默认值为false，即输出完整的全量内容
     */
    public QwenVLParameters useIncrementalOutput(boolean incrementalOutput) {
        this.jsonObject.put("incremental_output", incrementalOutput);
        return this;
    }
}
