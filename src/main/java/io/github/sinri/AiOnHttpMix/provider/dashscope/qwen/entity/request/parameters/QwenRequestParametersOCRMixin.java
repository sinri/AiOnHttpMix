package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.request.parameters;


import io.vertx.core.json.JsonObject;

interface QwenRequestParametersOCRMixin<E> extends QwenRequestParametersCore<E> {
    /**
     * 当您使用通义千问OCR模型执行内置任务时需要配置的参数。
     */
    default E ocrOptions(QwenRequestOcrOptions ocrOptions) {
        return this.write("ocr_options", ocrOptions.toJsonObject());
    }

    default QwenRequestOcrOptions ocrOptions() {
        JsonObject x = readJsonObject("ocr_options");
        if (x == null) {
            return null;
        }
        return QwenRequestOcrOptions.wrap(x);
    }

}
