package io.github.sinri.AiOnHttpMix.dashscope.wanx.task;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;

public interface WanxAsyncTaskResultFailedMixin extends UnmodifiableJsonifiableEntity {
    /**
     * 如果因为某种原因作业失败，则作业状态会设置为FAILED，并且通过code和message字段指明错误原因。
     */
    default String getCode() {
        return readString("output", "code");
    }

    /**
     * 如果因为某种原因作业失败，则作业状态会设置为FAILED，并且通过code和message字段指明错误原因。
     */
    default String getMessage() {
        return readString("output", "message");
    }
}
