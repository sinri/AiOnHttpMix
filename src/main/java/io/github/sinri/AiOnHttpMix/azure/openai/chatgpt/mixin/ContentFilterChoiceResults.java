package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin;

import io.github.sinri.AiOnHttpMix.azure.openai.core.mixin.filter.ContentFilterDetectedResult;
import io.github.sinri.AiOnHttpMix.azure.openai.core.mixin.filter.ContentFilterDetectedWithCitationResult;
import io.github.sinri.AiOnHttpMix.azure.openai.core.impl.filter.ContentFilterDetectedResultImpl;
import io.github.sinri.AiOnHttpMix.azure.openai.core.impl.filter.ContentFilterDetectedWithCitationResultImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

public interface ContentFilterChoiceResults extends ContentFilterResultsBase {
    // protected_material_text	contentFilterDetectedResult		No
    // protected_material_code	contentFilterDetectedWithCitationResult		No

    @Nullable
    default ContentFilterDetectedResult getProtectedMaterialText() {
        JsonObject protectedMaterialText = readJsonObject("protected_material_text");
        if (protectedMaterialText == null) {
            return null;
        }
        return new ContentFilterDetectedResultImpl(protectedMaterialText);
    }

    @Nullable
    default ContentFilterDetectedWithCitationResult getProtectedMaterialCode() {
        JsonObject protectedMaterialText = readJsonObject("protected_material_code");
        if (protectedMaterialText == null) {
            return null;
        }
        return new ContentFilterDetectedWithCitationResultImpl(protectedMaterialText);
    }
}
