package io.github.sinri.AiOnHttpMix.utils.models.azure.openai.gpt;

@Deprecated(forRemoval = true)
public final class O1Model extends OModelSeries {
    public final static String MODEL_NAME = "o1";

    @Override
    public String getModelName() {
        return MODEL_NAME;
    }
}
