package io.github.sinri.AiOnHttpMix.test.unit.provider.core;

import io.github.sinri.AiOnHttpMix.utils.ServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.keel.logger.event.KeelEventLog;
import io.github.sinri.keel.logger.issue.recorder.KeelIssueRecorder;

public interface ModelServiceMixin<M extends ChatModel> {
    ServiceAdapter getServiceAdapter();

    M getModel();

    KeelIssueRecorder<KeelEventLog> getUnitTestLogger();
}
