package io.github.sinri.AiOnHttpMix.dashscope.conversation;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Parameters extends SimpleJsonifiableEntity {

    public Parameters() {
        super();
        setResultFormat(ResultFormat.message);
    }

    public Parameters(JsonObject jsonObject) {
        super(jsonObject);
        setResultFormat(ResultFormat.message);
    }

    public Parameters setResultFormat(@NotNull ResultFormat resultFormat) {
        this.jsonObject.put("result_format", resultFormat.name());
        return this;
    }

    public Parameters setResultFormat(String resultFormat) {
        return this.setResultFormat(ResultFormat.valueOf(resultFormat));
    }

    /**
     * 生成时，核采样方法的概率阈值。
     * 例如，取值为0.8时，仅保留累计概率之和大于等于0.8的概率分布中的token，作为随机采样的候选集。
     * 取值范围为（0,1.0)，取值越大，生成的随机性越高；取值越低，生成的随机性越低。
     * 默认值 0.5。
     * 注意，取值不要大于等于1
     */
    public Parameters setTopP(float topP) {
        this.jsonObject.put("top_p", topP);
        return this;
    }

    /**
     * 生成时，采样候选集的大小。
     * 例如，取值为50时，仅将单次生成中得分最高的50个token组成随机采样的候选集。
     * 取值越大，生成的随机性越高；
     * 取值越小，生成的确定性越高。
     * 注意：如果top_k的值大于100，top_k将采用默认值0，表示不启用top_k策略，此时仅有top_p策略生效。
     */
    public Parameters setTopK(int topK) {
        this.jsonObject.put("top_k", topK);
        return this;
    }

    /**
     * 生成时，随机数的种子，用于控制模型生成的随机性。
     * 如果使用相同的种子，每次运行生成的结果都将相同；当需要复现模型的生成结果时，可以使用相同的种子。
     * seed参数支持无符号64位整数类型。
     * 默认值 1234.
     */
    public Parameters setSeed(int seed) {
        this.jsonObject.put("seed", seed);
        return this;
    }

    /**
     * 用于控制随机性和多样性的程度。
     * 具体来说，temperature值控制了生成文本时对每个候选词的概率分布进行平滑的程度。
     * 较高的temperature值会降低概率分布的峰值，使得更多的低概率词被选择，生成结果更加多样化；
     * 而较低的temperature值则会增强概率分布的峰值，使得高概率词更容易被选择，生成结果更加确定。
     * 取值范围： (0, 2),系统默认值1.0
     */
    public Parameters setTemperature(float temperature) {
        if (temperature >= 2 || temperature <= 0) throw new IllegalArgumentException();
        this.jsonObject.put("temperature", temperature);
        return this;
    }

    /**
     * 生成时，是否参考夸克搜索的结果。
     * 注意：打开搜索并不意味着一定会使用搜索结果；
     * 如果打开搜索，模型会将搜索结果作为prompt，进而“自行判断”是否生成结合搜索结果的文本，默认为false
     */
    public Parameters setEnableSearch(boolean enableSearch) {
        this.jsonObject.put("enable_search", enableSearch);
        return this;
    }

    /**
     * 用于控制流式输出模式，默认False，即后面内容会包含已经输出的内容；
     * 设置为True，将开启增量输出模式，后面输出不会包含已经输出的内容，您需要自行拼接整体输出，参考流式输出示例代码。
     * 该参数只能与stream输出模式配合使用。
     */
    public Parameters setIncrementalOutput(boolean incrementalOutput) {
        this.jsonObject.put("incremental_output", incrementalOutput);
        return this;
    }

    /**
     * 目前仅支持function，
     * 并且即使输入多个function，模型仅会选择其中一个生成结果。
     * 模型根据tools参数内容可以生产函数调用的参数
     *
     * @param tools 型可选调用的工具列表。
     */
    public Parameters setTools(@NotNull List<ToolMeta> tools) {
        JsonArray array = new JsonArray();
        tools.forEach(tool -> {
            array.add(tool.toJsonObject());
        });
        this.jsonObject.put("tools", array);
        return this;
    }

    public enum ResultFormat {
        text, message
    }
}
