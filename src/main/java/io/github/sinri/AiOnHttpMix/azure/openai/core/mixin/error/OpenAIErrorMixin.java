package io.github.sinri.AiOnHttpMix.azure.openai.core.mixin.error;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;

public interface OpenAIErrorMixin {
    interface ErrorBase extends UnmodifiableJsonifiableEntity {
        default String getCode() {
            return readString("code");
        }

        default String getMessage() {
            return readString("message");
        }
    }

    // errorResponse
    // Error
    // innerError
    // innerErrorCode
    // dalleErrorResponse
    // dalleError
    // dalleInnerError

}
