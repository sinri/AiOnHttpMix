package io.github.sinri.AiOnHttpMix.dashscope.drawing;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

public class ApplyTaskResponse extends SimpleJsonifiableEntity {
    public ApplyTaskResponse(int statusCode, JsonObject jsonObject) {
        super(jsonObject);
        this.toJsonObject().put("status_code", statusCode);
    }

    public int getStatusCode() {
        return Objects.requireNonNull(readInteger("status_code"));
    }

    public String getTaskId() {
        return readString("output", "task_id");
    }

    public String getTaskStatus() {
        return readString("output", "task_status");
    }
}
