package io.github.sinri.AiOnHttpMix;

import io.github.sinri.keel.logger.KeelLogLevel;
import io.github.sinri.keel.logger.event.KeelEventLog;
import io.github.sinri.keel.logger.issue.center.KeelIssueRecordCenter;
import io.github.sinri.keel.logger.issue.recorder.KeelIssueRecorder;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 大语言模型服务套件全局控制器。
 */
public class AigcMix {

    private static final KeelIssueRecorder<KeelEventLog> silentVerboseLogger;
    @Nonnull
    private static final AtomicReference<KeelIssueRecorder<KeelEventLog>> verboseLoggerRef;

    static {
        silentVerboseLogger = KeelIssueRecorder.buildSilentIssueRecorder();
        verboseLoggerRef = new AtomicReference<>(silentVerboseLogger);
    }

    /**
     * 设定logger为按照给定的日志记录最低级别的对StdOut进行输出的日志记录器。
     *
     * @since 1.0.2
     */
    public static void enableVerboseLogger() {
        enableVerboseLogger(KeelIssueRecordCenter.outputCenter(), KeelLogLevel.DEBUG);
    }

    /**
     * @since 1.2.2
     */
    public static void enableVerboseLogger(@Nonnull KeelIssueRecordCenter center, @Nonnull KeelLogLevel level) {
        if (level == KeelLogLevel.SILENT) {
            disableVerboseLogger();
        } else {
            synchronized (verboseLoggerRef) {
                var logger = center.generateIssueRecorder("AigcMix", KeelEventLog::new);
                logger.setVisibleLevel(level);
                verboseLoggerRef.set(logger);
            }
        }
    }

    /**
     * @since 1.0.2
     */
    public static void disableVerboseLogger() {
        //verboseLogger = createLogger(KeelLogLevel.SILENT);
        synchronized (verboseLoggerRef) {
            verboseLoggerRef.set(silentVerboseLogger);
        }
    }

    /**
     * @since 1.0.2
     */
    public static @Nonnull KeelIssueRecorder<KeelEventLog> getVerboseLogger() {
        return verboseLoggerRef.get();
    }
}