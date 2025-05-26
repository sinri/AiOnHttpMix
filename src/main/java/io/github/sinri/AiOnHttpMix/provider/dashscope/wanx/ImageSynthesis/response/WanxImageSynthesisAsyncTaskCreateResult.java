package io.github.sinri.AiOnHttpMix.provider.dashscope.wanx.ImageSynthesis.response;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface WanxImageSynthesisAsyncTaskCreateResult extends UnmodifiableJsonifiableEntity {
    static WanxImageSynthesisAsyncTaskCreateResult wrap(JsonObject jsonObject) {
        return new WanxImageSynthesisAsyncTaskCreateResultImpl(jsonObject);
    }

    /**
     * @return 本次请求的系统唯一码。
     */
    default String getRequestId() {
        return readString("request_id");
    }

    /**
     * @return 提交异步任务后的作业状态。
     */
    default String getTaskStatus() {
        return readString("output", "task_status");
    }

    /**
     * @return 本次请求的异步任务的作业 id，实际作业结果需要通过异步任务查询接口获取。
     */
    default String getTaskId() {
        return readString("output", "task_id");
    }
}
