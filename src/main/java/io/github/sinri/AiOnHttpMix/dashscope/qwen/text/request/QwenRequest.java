package io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenModel;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenRole;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.message.QwenMessage;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.tool.QwenToolDefinition;
import io.github.sinri.keel.core.TechnicalPreview;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public interface QwenRequest extends JsonifiableEntity<QwenRequest> {
    static QwenRequest create() {
        return new QwenRequestImpl();
    }

    static QwenRequest wrap(JsonObject jsonObject) {
        return new QwenRequestImpl(jsonObject);
    }

    default QwenRequest handleInput(Handler<Input> handler) {
        Input input;
        var i = this.toJsonObject().getJsonObject("input");
        if (i != null) {
            input = Input.wrap(i);
        } else {
            input = Input.create();
        }
        handler.handle(input);
        return setInput(input);
    }

    default QwenRequest handleParameters(Handler<Parameters> handler) {
        Parameters parameters;
        var p = this.toJsonObject().getJsonObject("parameters");
        if (p != null) {
            parameters = Parameters.wrap(p);
        } else {
            parameters = Parameters.create();
        }
        handler.handle(parameters);
        return setParameters(parameters);
    }

    @Nullable
    default Input getInput() {
        JsonObject input = readJsonObject("input");
        if (input == null) return null;
        return new QwenRequestInputImpl(input);
    }

    default QwenRequest setInput(Input input) {
        this.toJsonObject().put("input", input.toJsonObject());
        return this;
    }

    default QwenModel getModel() {
        var model = readString("model");
        if (model == null) return null;
        return QwenModel.fromModelCode(model);
    }

    @Deprecated(since = "1.2.2")
    default QwenRequest setModel(QwenModel model) {
        this.toJsonObject().put("model", model.getModelCode());
        return this;
    }

    /**
     * For DeepSeek on Bailian Platform of Aliyun
     */
    @TechnicalPreview(since = "1.2.2")
    default QwenRequest setModel(String model) {
        this.toJsonObject().put("model", model);
        return this;
    }

    @Nullable
    default Parameters getParameters() {
        JsonObject parameters = readJsonObject("parameters");
        if (parameters == null) return null;
        return Parameters.wrap(parameters);
    }

    default QwenRequest setParameters(Parameters parameters) {
        this.toJsonObject().put("parameters", parameters.toJsonObject());
        return this;
    }

    interface Input extends JsonifiableEntity<Input> {
        static Input create() {
            return new QwenRequestInputImpl();
        }

        static Input wrap(JsonObject jsonObject) {
            return new QwenRequestInputImpl(jsonObject);
        }

        Input addMessage(QwenMessage message);

        default Input addSystemMessage(String content) {
            return addMessage(QwenMessage.create()
                                         .setRole(QwenRole.system)
                                         .setContent(content)
            );
        }

        default Input addUserMessage(String content) {
            return addMessage(QwenMessage.create()
                                         .setRole(QwenRole.user)
                                         .setContent(content)
            );
        }
    }


    interface Parameters extends JsonifiableEntity<Parameters> {
        static Parameters create() {
            return new QwenRequestParametersImpl();
        }

        static Parameters wrap(JsonObject jsonObject) {
            return new QwenRequestParametersImpl(jsonObject);
        }

        @Nullable
        default ResultFormat getResultFormat() {
            String resultFormat = readString("result_format");
            if (resultFormat == null) return null;
            return ResultFormat.valueOf(resultFormat);
        }

        /**
         * @param result_format 用于指定返回结果的格式，默认为text，也可设置为message。推荐优先使用message格式。
         */
        default Parameters setResultFormat(ResultFormat result_format) {
            this.toJsonObject().put("result_format", result_format.name());
            return this;
        }

        /**
         * seed支持无符号64位整数。在使用seed时，模型将尽可能生成相同或相似的结果，但目前不保证每次生成的结果完全相同。
         *
         * @param seed 生成时使用的随机数种子，用户控制模型生成内容的随机性。
         */
        default Parameters setSeed(int seed) {
            this.toJsonObject().put("seed", seed);
            return this;
        }

        /**
         * 其中qwen-turbo最大值和默认值为1500，qwen-max、qwen-max-1201 、qwen-max-longcontext 和 qwen-plus最大值和默认值均为2000。
         *
         * @param max_tokens 用于限制模型生成token的数量，表示生成token个数的上限。
         */
        default Parameters setMaxTokens(int max_tokens) {
            this.toJsonObject().put("max_tokens", max_tokens);
            return this;
        }

        /**
         * 例如，取值为0.8时，仅保留累计概率之和大于等于0.8的概率分布中的token，作为随机采样的候选集。 取值范围为（0,1.0)，取值越大，生成的随机性越高；取值越低，生成的随机性越低。注意，取值不要大于等于1。
         *
         * @param top_p 生成时，核采样方法的概率阈值。
         */
        default Parameters setTopP(float top_p) {
            if (top_p <= 0.0 || top_p >= 1.0) {
                throw new IllegalArgumentException("top_p must be in range (0.0,1.0)");
            }
            this.toJsonObject().put("top_p", top_p);
            return this;
        }

        /**
         * 例如，取值为50时，仅将单次生成中得分最高的50个token组成随机采样的候选集。 取值越大，生成的随机性越高；取值越小，生成的确定性越高。
         * 注意：如果top_k参数为空或者top_k的值大于100，表示不启用top_k策略，此时仅有top_p策略生效。
         *
         * @param top_k 生成时，采样候选集的大小。
         */
        default Parameters setTopK(int top_k) {
            this.toJsonObject().put("top_k", top_k);
            return this;
        }

        /**
         * 用于控制模型生成时连续序列中的重复度。 提高repetition_penalty时可以降低模型生成的重复度。 1.0表示不做惩罚。没有严格的取值范围。
         */
        default Parameters setRepetitionPenalty(float repetition_penalty) {
            this.toJsonObject().put("repetition_penalty", repetition_penalty);
            return this;
        }

        /**
         * 用户控制模型生成时整个序列中的重复度。 提高presence_penalty时可以降低模型生成的重复度，取值范围 [-2.0, 2.0]。
         */
        default Parameters setPresencePenalty(float presence_penalty) {
            if (presence_penalty < -2.0 || presence_penalty > 2.0) {
                throw new IllegalArgumentException("presence_penalty must be in range [-2.0,2.0]");
            }
            this.toJsonObject().put("presence_penalty", presence_penalty);
            return this;
        }

        /**
         * 用于控制随机性和多样性的程度。 具体来说，temperature值控制了生成文本时对每个候选词的概率分布进行平滑的程度。
         * 较高的temperature值会降低概率分布的峰值，使得更多的低概率词被选择，生成结果更加多样化； 而较低的temperature值则会增强概率分布的峰值，使得高概率词更容易被选择，生成结果更加确定。
         *
         * @param temperature 用于控制随机性和多样性的程度。取值范围：[0, 2)，不建议取值为0，无意义。
         */
        default Parameters setTemperature(float temperature) {
            this.toJsonObject().put("temperature", temperature);
            return this;
        }

        /**
         * stop参数用于实现内容生成过程的精确控制，在模型生成的内容即将包含指定的字符串或token_id时自动停止，生成的内容不包含指定的内容。 当模型将要生成指定的stop词语时停止。
         * 例如将stop指定为"你好"，则模型将要生成“你好”时停止。
         */
        default Parameters setStop(String stop) {
            this.toJsonObject().put("stop", stop);
            return this;
        }

        /**
         * stop参数用于实现内容生成过程的精确控制，在模型生成的内容即将包含指定的字符串或token_id时自动停止，生成的内容不包含指定的内容。
         * array中的元素可以为token_id或者字符串，或者元素为token_id的array。 当模型将要生成的token或其对应的token_id在stop中时，模型生成将会停止。
         * 例如将stop指定为["你好","天气"]或者[108386,104307]，则模型将要生成“你好”或者“天气”时停止。 如果将stop指定为[[108386, 103924],[35946,
         * 101243]]，则模型将要生成“你好啊”或者“我很好”时停止。 stop为array类型时，不可以将token_id和字符串同时作为元素输入，比如不可以指定stop为["你好",104307]。
         */
        default Parameters setStop(JsonArray stop) {
            this.toJsonObject().put("stop", stop);
            return this;
        }

        /**
         * 模型内置了互联网搜索服务，该参数控制模型在生成文本时是否参考使用互联网搜索结果。取值如下：
         * true：启用互联网搜索，模型会将搜索结果作为文本生成过程中的参考信息，但模型会基于其内部逻辑“自行判断”是否使用互联网搜索结果。 false（默认）：关闭互联网搜索。
         */
        default Parameters setEnableSearch(boolean enable_search) {
            this.toJsonObject().put("enable_search", enable_search);
            return this;
        }

        /**
         * 控制在流式输出模式下是否开启增量输出，即后续输出内容是否包含已输出的内容。 设置为True时，将开启增量输出模式，后面输出不会包含已经输出的内容，您需要自行拼接整体输出； 设置为False则会包含已输出的内容。
         * <p>
         * 默认False： I | I like | I like apple
         * </p><p>
         * True: I | like | apple
         * </p><p>
         * 该参数只能在开启SSE响应时使用。
         * </p><p>
         * 说明：incremental_output暂时无法和tools参数同时使用。
         * </p>
         */
        default Parameters setIncrementalOutput(boolean incremental_output) {
            this.toJsonObject().put("incremental_output", incremental_output);
            return this;
        }

        /**
         * 用于指定可供模型调用的工具列表。当输入多个工具时，模型会选择其中一个生成结果。 使用tools时需要同时指定result_format为message。 在function call流程中，无论是发起function
         * call的轮次，还是向模型提交工具函数的执行结果，均需设置tools参数。 说明：tools暂时无法和incremental_output参数同时使用。
         */
        default Parameters addTool(QwenToolDefinition toolDefinition) {
            JsonArray tools = this.toJsonObject().getJsonArray("tools");
            if (tools == null) {
                tools = new JsonArray();
                this.toJsonObject().put("tools", tools);
            }
            tools.add(toolDefinition.toJsonObject());
            return this;
        }

        /**
         * 在使用tools参数时，用于控制模型调用指定工具。 "none"表示不调用工具。tools参数为空时，默认值为"none"。
         * "auto"表示模型判断是否调用工具，可能调用也可能不调用。tools参数不为空时，默认值为"auto"
         * 说明：当前支持qwen-max/qwen-max-0428/qwen-max-0403/qwen-plus/qwen-turbo。
         *
         * @param tool_choice "none" or "auto"
         */
        default Parameters setToolChoice(String tool_choice) {
            this.toJsonObject().put("tool_choice", tool_choice);
            return this;
        }

        /**
         * 在使用tools参数时，用于控制模型调用指定工具。 说明：当前支持qwen-max/qwen-max-0428/qwen-max-0403/qwen-plus/qwen-turbo。
         *
         * @param functionName 期望被调用的工具名称
         */
        default Parameters setToolChoiceAsFunction(String functionName) {
            this.toJsonObject().put("tool_choice", new JsonObject()
                    .put("type", "function")
                    .put("function", new JsonObject()
                            .put("name", functionName)
                    )
            );
            return this;
        }

        /**
         * 尝鲜功能，阿里云还没有正式发布文档。
         */
        default Parameters handleSearchOptions(Handler<SearchOptions> searchOptionsHandler) {
            SearchOptions searchOptions = SearchOptions.create();
            searchOptionsHandler.handle(searchOptions);
            return this.setSearchOptions(searchOptions);
        }

        /**
         * 尝鲜功能，阿里云还没有正式发布文档。
         */
        @Nullable
        default SearchOptions getSearchOptions() {
            JsonObject entries = readJsonObject("search_options");
            if (entries == null) return null;
            return SearchOptions.wrap(entries);
        }

        /**
         * 尝鲜功能，阿里云还没有正式发布文档。
         */
        default Parameters setSearchOptions(SearchOptions searchOptions) {
            this.toJsonObject().put("search_options", searchOptions.toJsonObject());
            return this;
        }

        /**
         * @since 1.3.1 for Qwen-MT模型
         */
        @Nullable
        default TranslationOptions getTranslationOptions() {
            JsonObject x = this.toJsonObject().getJsonObject("translation_options");
            if (x == null) return null;
            return TranslationOptions.wrap(x);
        }

        /**
         * @since 1.3.1 for Qwen-MT模型
         */
        default Parameters setTranslationOptions(TranslationOptions translationOptions) {
            this.toJsonObject().put("translation_options", translationOptions.toJsonObject());
            return this;
        }

        /**
         * @since 1.3.1 for Qwen-MT模型
         */
        default Parameters handleTranslationOptions(Handler<TranslationOptions> translationOptionsHandler) {
            TranslationOptions translationOptions = TranslationOptions.create();
            translationOptionsHandler.handle(translationOptions);
            return setTranslationOptions(translationOptions);
        }

        enum ResultFormat {
            text, message
        }

        interface SearchOptions extends JsonifiableEntity<SearchOptions> {
            static SearchOptions create() {
                return new QwenRequestParametersSearchOptionsImpl()
                        .setCitationFormat("[ref_<number>]");
            }

            static SearchOptions wrap(JsonObject jsonObject) {
                return new QwenRequestParametersSearchOptionsImpl(jsonObject);
            }

            default boolean getEnableSource() {
                return this.toJsonObject().getBoolean("enable_source");
            }

            default SearchOptions setEnableSource(boolean enable_source) {
                this.toJsonObject().put("enable_source", enable_source);
                return this;
            }

            default boolean getEnableCitation() {
                return this.toJsonObject().getBoolean("enable_citation");
            }

            default SearchOptions setEnableCitation(boolean enable_citation) {
                this.toJsonObject().put("enable_citation", enable_citation);
                return this;
            }

            default String getCitationFormat() {
                return this.toJsonObject().getString("citation_format");
            }

            /**
             * @param citation_format {@code [ref_<number>]} or {@code [<number>] }
             */
            default SearchOptions setCitationFormat(String citation_format) {
                this.toJsonObject().put("citation_format", citation_format);
                return this;
            }

            default boolean getForcedSearch() {
                return this.toJsonObject().getBoolean("forced_search");
            }

            default SearchOptions setForcedSearch(boolean forced_search) {
                this.toJsonObject().put("forced_search", forced_search);
                return this;
            }
        }

        /**
         * Represents a set of options for translation, including the source and target languages.
         * This interface extends {@link JsonifiableEntity} to allow conversion to and from a JSON object.
         * It provides methods to get and set the source and target languages for translation.
         *
         * <p>Note: The actual implementation of this interface is expected to provide
         * functionality for reading from and writing to a JSON object, as well as
         * managing other translation-related properties such as terms, tm_list, and domains,
         * which are not fully specified in this documentation.
         *
         * @since 1.3.1
         */
        interface TranslationOptions extends JsonifiableEntity<TranslationOptions> {
            static TranslationOptions create() {
                return new QwenRequestParametersTranslationOptionsImpl();
            }

            static TranslationOptions wrap(JsonObject jsonObject) {
                return new QwenRequestParametersTranslationOptionsImpl(jsonObject);
            }

            @Nullable
            default Language getSourceLang() {
                String sourceLang = readString("source_lang");
                if (sourceLang == null) return null;
                return Language.valueOf(sourceLang);
            }

            default TranslationOptions setSourceLang(Language sourceLang) {
                this.toJsonObject().put("source_lang", sourceLang.getCode());
                return this;
            }

            @Nullable
            default Language getTargetLang() {
                String targetLang = readString("target_lang");
                if (targetLang == null) return null;
                return Language.valueOf(targetLang);
            }

            default TranslationOptions setTargetLang(Language targetLang) {
                this.toJsonObject().put("target_lang", targetLang.getCode());
                return this;
            }


            /**
             * 添加术语以干预翻译
             *
             * @param source 术语原文
             * @param target 术语译文
             */
            default TranslationOptions addTerm(String source, String target) {
                JsonArray terms = this.toJsonObject().getJsonArray("terms");
                if (terms == null) {
                    terms = new JsonArray();
                    this.toJsonObject().put("terms", terms);
                }
                terms.add(new JsonObject()
                        .put("source", source)
                        .put("target", target)
                );
                return this;
            }

            /**
             * @return 干预翻译的术语组装成的Map
             */
            default Map<String, String> getTerms() {
                JsonArray terms = this.toJsonObject().getJsonArray("terms");
                if (terms == null) {
                    return Map.of();
                }
                TreeMap<String, String> termsMap = new TreeMap<>();
                terms.forEach(x -> {
                    var y = (JsonObject) x;
                    String source = y.getString("source");
                    String target = y.getString("target");
                    termsMap.put(source, target);
                });
                return Collections.unmodifiableNavigableMap(termsMap);
            }


            /**
             * 使用翻译记忆
             *
             * @param source 模板句原文
             * @param target 模板句译文
             */
            default TranslationOptions addTemplate(String source, String target) {
                JsonArray terms = this.toJsonObject().getJsonArray("tm_list");
                if (terms == null) {
                    terms = new JsonArray();
                    this.toJsonObject().put("tm_list", terms);
                }
                terms.add(new JsonObject()
                        .put("source", source)
                        .put("target", target)
                );
                return this;
            }

            /**
             * @return 翻译记忆（翻译模板？）所组装出的Map
             */
            default Map<String, String> getTemplates() {
                JsonArray template = this.toJsonObject().getJsonArray("tm_list");
                if (template == null) {
                    return Map.of();
                }
                TreeMap<String, String> templateMap = new TreeMap<>();
                template.forEach(x -> {
                    var y = (JsonObject) x;
                    String source = y.getString("source");
                    String target = y.getString("target");
                    templateMap.put(source, target);
                });
                return Collections.unmodifiableNavigableMap(templateMap);
            }

            /**
             * 领域提示语句暂时只支持英文。
             *
             * @return 领域提示
             */
            default String getDomains() {
                return this.toJsonObject().getString("domains");
            }

            /**
             * 如果您希望翻译的风格更符合某个领域的特性，如法律、政务领域翻译用语应当严肃正式，社交领域用语应当口语化，可以用一段自然语言文本描述您的领域，将其提供给大模型作为提示。
             * 领域提示语句暂时只支持英文。
             *
             * @param domains 领域提示
             */
            default TranslationOptions setDomains(String domains) {
                this.toJsonObject().put("domains", domains);
                return this;
            }

            /**
             * @see <a href="https://help.aliyun.com/zh/model-studio/user-guide/machine-translation#f1c944081ca13"
             *         >支持的语言</a>
             * @since 1.3.1
             */
            enum Language {
                CHINESE("Chinese", "中文"),
                ENGLISH("English", "英语"),
                JAPANESE("Japanese", "日语"),
                KOREAN("Korean", "韩语"),
                THAI("Thai", "泰语"),
                FRENCH("French", "法语"),
                GERMAN("German", "德语"),
                SPANISH("Spanish", "西班牙语"),
                ARABIC("Arabic", "阿拉伯语"),
                INDONESIAN("Indonesian", "印尼语"),
                VIETNAMESE("Vietnamese", "越南语"),
                PORTUGUESE("Portuguese", "巴西葡萄牙语"),
                ITALIAN("Italian", "意大利语"),
                DUTCH("Dutch", "荷兰语"),
                RUSSIAN("Russian", "俄语"),
                KHMER("Khmer", "高棉语"),
                CEBUANO("Cebuano", "宿务语"),
                FILIPINO("Filipino", "菲律宾语"),
                CZECH("Czech", "捷克语"),
                POLISH("Polish", "波兰语"),
                PERSIAN("Persian", "波斯语"),
                HEBREW("Hebrew", "希伯来语"),
                TURKISH("Turkish", "土耳其语"),
                HINDI("Hindi", "印地语"),
                BENGALI("Bengali", "孟加拉语"),
                URDU("Urdu", "乌尔都语"),
                AUTO("auto", "自动");

                private final String code;
                private final String chineseName;

                Language(String code, String chineseName) {
                    this.code = code;
                    this.chineseName = chineseName;
                }

                public String getCode() {
                    return code;
                }

                public String getChineseName() {
                    return chineseName;
                }


            }
        }
    }
}