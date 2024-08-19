package io.github.sinri.AiOnHttpMix.dashscope.drawing;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class QueryTaskResponse extends SimpleJsonifiableEntity {
    public QueryTaskResponse(int statusCode, JsonObject jsonObject) {
        super(jsonObject);
        this.toJsonObject().put("status_code", statusCode);
    }

    /**
     * @return 本次请求的系统唯一码
     */
    public String getRequestId() {
        return readString("request_id");
    }

    /**
     * @return 本次请求的异步任务的作业 id，实际作业结果需要通过异步任务查询接口获取。
     */
    public String getTaskId() {
        return readString("output", "task_id");
    }

    /**
     * 任务状态：
     * PENDING 排队中
     * RUNNING 处理中
     * SUCCEEDED 成功
     * FAILED 失败
     * UNKNOWN 作业不存在或状态未知
     *
     * @return 被查询作业的作业状态
     */
    public TaskStatus getTaskStatus() {
        return TaskStatus.valueOf(readString("output", "task_status"));
    }

    public String getSubmitTime() {
        return readString("output", "submit_time");
    }

    public String getScheduledTime() {
        return readString("output", "scheduled_time");
    }

    public String getEndTime() {
        return readString("output", "end_time");
    }

    /**
     * @return 作业中每个batch任务的状态
     */
    public UsageTaskMetrics getUsageTaskMetrics() {
        return new UsageTaskMetrics(readJsonObject("usage", "task_metrics"));
    }

    public List<String> getImageUrlList() {
        List<JsonObject> results = readJsonObjectArray("output", "results");
        List<String> urls = new ArrayList<>();
        if (results != null) {
            results.forEach(result -> {
                urls.add(result.getString("url"));
            });
        }
        return urls;
    }

    public enum TaskStatus {
        PENDING, RUNNING, SUCCEEDED, FAILED, UNKNOWN
    }

    public static class UsageTaskMetrics extends SimpleJsonifiableEntity {

        public UsageTaskMetrics(JsonObject jsonObject) {
            super(jsonObject);
        }

        /**
         * @return 总batch数目
         */
        public Integer getTotal() {
            return readInteger("TOTAL");
        }

        /**
         * @return 已经成功的batch数目
         */
        public Integer getSucceeded() {
            return readInteger("SUCCEEDED");
        }

        /**
         * @return 已经失败的batch数目
         */
        public Integer getFailed() {
            return readInteger("FAILED");
        }
    }
}
