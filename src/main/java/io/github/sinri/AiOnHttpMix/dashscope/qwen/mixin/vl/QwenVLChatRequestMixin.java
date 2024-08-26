package io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.vl;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.vl.VLChatRequestImpl;
import io.github.sinri.keel.core.SelfInterface;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

public interface QwenVLChatRequestMixin<E> extends JsonifiableEntity<E>, SelfInterface<E> {
    default E setModel(QwenKit.QwenVLModel model) {
        toJsonObject().put("model", model.getModelCode());
        return getImplementation();
    }

    @Nullable
    default QwenKit.QwenVLModel getModel() {
        String model = readString("model");
        if (model == null) return null;
        return QwenKit.QwenVLModel.fromModelCode(model);
    }

    default E setInput(Input input) {
        toJsonObject().put("input",input.toJsonObject());
        return getImplementation();
    }

    default E setParameters(Parameters parameters) {
        toJsonObject().put("parameters",parameters.toJsonObject());
        return getImplementation();
    }

    interface Input extends JsonifiableEntity<Input> {

        static Input create() {
            return new VLChatRequestImpl.InputImpl();
        }
        static Input wrap(JsonObject input) {
            return new VLChatRequestImpl.InputImpl(input);
        }

        /**
         * @param message 多模态场景下的输入信息
         */
        default Input addMessage(QwenKit.VLChatInputMessage message) {
            JsonArray array = toJsonObject().getJsonArray("messages");
            if (array == null) {
                array = new JsonArray();
                toJsonObject().put("messages", array);
            }
            array.add(message.toJsonObject());
            return this;
        }
    }

    interface Parameters extends JsonifiableEntity<Parameters> {
        static Parameters create() {
            return new VLChatRequestImpl.ParametersImpl();
        }
        static Parameters wrap(JsonObject parameters) {
            return new VLChatRequestImpl.ParametersImpl(parameters);
        }

        /**
         * 例如，取值为0.8时，仅保留累计概率之和大于等于0.8的概率分布中的token，作为随机采样的候选集。
         * 取值范围为(0,1.0)，取值越大，生成的随机性越高；取值越低，生成的随机性越低。
         * 默认值 0.8。注意，取值不要大于等于1
         *
         * @param top_p 生成时，核采样方法的概率阈值。
         */
        default Parameters setTopP(float top_p) {
            toJsonObject().put("top_p", top_p);
            return this;
        }

        /**
         * 例如，取值为50时，仅将单次生成中得分最高的50个token组成随机采样的候选集。
         * 取值越大，生成的随机性越高；取值越小，生成的确定性越高。
         * 注意：如果top_k的值大于100，top_k将取值100。
         *
         * @param top_k 生成时，采样候选集的大小。
         */
        default Parameters setTopK(int top_k) {
            toJsonObject().put("top_k", top_k);
            return this;
        }

        /**
         * qwen-vl-max 和 qwen-vl-plus 最大值和默认值均为1500。
         *
         * @param max_tokens 用于限制模型生成token的数量，表示生成token个数的上限。
         */
        default Parameters setMaxTokens(int max_tokens) {
            toJsonObject().put("max_tokens", max_tokens);
            return this;
        }

        /**
         * 如果使用相同的种子，每次运行生成的结果都将相同；当需要复现模型的生成结果时，可以使用相同的种子。
         * seed参数支持无符号64位整数类型。
         *
         * @param seed 生成时，随机数的种子，用于控制模型生成的随机性。
         */
        default Parameters setSeed(int seed) {
            toJsonObject().put("seed", seed);
            return this;
        }

        /**
         * 当使用增量输出时每次流式返回的序列仅包含最新生成的增量内容，默认值为false，即输出完整的全量内容
         *
         * @param incremental_output 是否使用增量输出。
         */
        default Parameters setIncrementalOutput(boolean incremental_output) {
            toJsonObject().put("incremental_output", incremental_output);
            return this;
        }
    }
}
