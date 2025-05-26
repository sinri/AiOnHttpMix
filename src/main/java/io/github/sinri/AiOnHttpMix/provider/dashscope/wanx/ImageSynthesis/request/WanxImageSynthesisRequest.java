package io.github.sinri.AiOnHttpMix.provider.dashscope.wanx.ImageSynthesis.request;

import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface WanxImageSynthesisRequest extends JsonifiableEntity<WanxImageSynthesisRequest> {
    static WanxImageSynthesisRequest wrap(@Nonnull JsonObject json) {
        return new WanxImageSynthesisRequestImpl().reloadDataFromJsonObject(json);
    }

    static WanxImageSynthesisRequest create() {
        return new WanxImageSynthesisRequestImpl()
                .setModel(WanxImageSynthesisModel.WanxV1);
    }

    @Nullable
    default WanxImageSynthesisModel getModel() {
        var x = this.readString("model");
        if (x == null) return null;
        return WanxImageSynthesisModel.fromModelCode(x);
    }

    default WanxImageSynthesisRequest setModel(@Nonnull WanxImageSynthesisModel model) {
        this.toJsonObject().put("model", model.getModelCode());
        return this;
    }

    default WanxImageSynthesisRequest handleInput(@Nonnull Handler<WanxImageSynthesisInput> inputHandler) {
        WanxImageSynthesisInput input;

        var x = this.readJsonObject("input");
        if (x != null) {
            input = WanxImageSynthesisInput.wrap(x);
        } else {
            input = WanxImageSynthesisInput.create();
        }

        inputHandler.handle(input);
        this.setInput(input);
        return this;
    }

    @Nullable
    default WanxImageSynthesisInput getInput() {
        var x = this.readJsonObject("input");
        if (x == null) return null;
        return WanxImageSynthesisInput.wrap(x);
    }

    default WanxImageSynthesisRequest setInput(@Nonnull WanxImageSynthesisInput input) {
        this.toJsonObject().put("input", input.toJsonObject());
        return this;
    }

    default WanxImageSynthesisRequest handleParameters(@Nonnull Handler<WanxImageSynthesisParameters> parametersHandler) {
        WanxImageSynthesisParameters parameters;

        var x = this.readJsonObject("parameters");
        if (x != null) {
            parameters = WanxImageSynthesisParameters.wrap(x);
        } else {
            parameters = WanxImageSynthesisParameters.create();
        }

        parametersHandler.handle(parameters);
        this.setParameters(parameters);
        return this;
    }

    @Nullable
    default WanxImageSynthesisParameters getParameters() {
        var x = this.readJsonObject("parameters");
        if (x == null) return null;
        return WanxImageSynthesisParameters.wrap(x);
    }

    default WanxImageSynthesisRequest setParameters(WanxImageSynthesisParameters parameters) {
        this.toJsonObject().put("parameters", parameters.toJsonObject());
        return this;
    }
}
