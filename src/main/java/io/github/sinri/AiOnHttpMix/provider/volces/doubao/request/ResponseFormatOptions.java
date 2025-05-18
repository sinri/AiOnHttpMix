package io.github.sinri.AiOnHttpMix.provider.volces.doubao.request;

import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface ResponseFormatOptions extends JsonifiableEntity<ResponseFormatOptions> {
    static ResponseFormatOptions wrap(JsonObject jsonObject) {
        return new ResponseFormatOptionsImpl(jsonObject);
    }

    static ResponseFormatOptions create() {
        return new ResponseFormatOptionsImpl();
    }

    /**
     * 模型默认回复文本格式内容。
     */
    static ResponseFormatOptions createForText() {
        return create()
                .type("text");
    }

    /**
     * 模型回复内容以JSON对象结构来组织。
     */
    static ResponseFormatOptions createForJsonObject() {
        return create()
                .type("json_object");
    }

    /**
     * <p>字段暂未支持</p>
     * <p>
     * 模型回复内容以JSON对象结构来组织，遵循 schema 字段定义的JSON结构。
     * </p>
     */
    static ResponseFormatOptions createForJsonSchema(JsonSchemaDef jsonSchemaDef) {
        return create()
                .type("json_schema")
                .jsonSchema(jsonSchemaDef);
    }

    default ResponseFormatOptions type(String type) {
        return this.write("type", type);
    }

    default ResponseFormatOptions jsonSchema(JsonSchemaDef jsonSchemaDef) {
        return this.write("json_schema", jsonSchemaDef.toJsonObject());
    }

    interface JsonSchemaDef extends JsonifiableEntity<JsonSchemaDef> {
        static JsonSchemaDef create() {
            return new ResponseFormatOptionsImpl.JsonSchemaDefImpl();
        }

        static JsonSchemaDef wrap(JsonObject jsonObject) {
            return new ResponseFormatOptionsImpl.JsonSchemaDefImpl(jsonObject);
        }

        /**
         * @param x 用户自定义的JSON结构的名称。
         */
        default JsonSchemaDef name(String x) {
            return write("name", x);
        }

        default String name() {
            return readString("name");
        }

        /**
         * @param x 回复用途描述，模型将根据此描述决定如何以该格式回复。
         */
        default JsonSchemaDef description(String x) {
            return write("description", x);
        }

        default String description() {
            return readString("description");
        }

        /**
         * @param x 回复格式的 JSON 格式定义，以 JSON Schema 对象的形式描述。
         */
        default JsonSchemaDef schema(JsonObject x) {
            return write("schema", x);
        }

        default JsonObject schema() {
            return readJsonObject("schema");
        }

        /**
         * @param x 是否在生成输出时启用严格遵循模式。默认值 false。
         *          ture：模型将始终严格遵循schema字段中定义的格式。
         *          false：模型会尽可能遵循schema字段中定义的结构。
         */
        default JsonSchemaDef strict(boolean x) {
            return write("strict", x);
        }

        default Boolean strict() {
            return readBoolean("strict");
        }
    }
}
