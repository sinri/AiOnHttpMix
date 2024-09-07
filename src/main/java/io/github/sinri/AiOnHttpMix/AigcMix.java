package io.github.sinri.AiOnHttpMix;

import io.github.sinri.keel.logger.KeelLogLevel;
import io.github.sinri.keel.logger.event.KeelEventLogger;
import io.github.sinri.keel.logger.issue.center.KeelIssueRecordCenter;

import javax.annotation.Nonnull;

public class AigcMix {
    @Nonnull
    private static KeelEventLogger verboseLogger = KeelIssueRecordCenter.silentCenter().generateEventLogger("");


    /**
     * 设定logger为按照给定的日志记录最低级别的对StdOut进行输出的日志记录器。
     *
     * @param level 日志记录最低级别
     * @since 1.0.2
     */
    public static void enableVerboseLogger(@Nonnull KeelLogLevel level) {
        verboseLogger = createLogger(level);
    }

    /**
     * 设定logger为给定的日志记录器。
     *
     * @param logger 一个指定的日志记录器
     * @since 1.0.2
     */
    public static void enableVerboseLogger(@Nonnull KeelEventLogger logger) {
        verboseLogger = logger;
    }

    /**
     * @since 1.0.2
     */
    public static void disableVerboseLogger() {
        verboseLogger = createLogger(KeelLogLevel.SILENT);
    }

    /**
     * @since 1.0.2
     */
    public static @Nonnull KeelEventLogger getVerboseLogger() {
        return verboseLogger;
    }

    /**
     * @since 1.0.3
     */
    private static @Nonnull KeelEventLogger createLogger(@Nonnull KeelLogLevel level) {
        if (level == KeelLogLevel.SILENT) {
            return KeelIssueRecordCenter.silentCenter().generateEventLogger("AigcMix");
        } else {
            var logger = KeelIssueRecordCenter.outputCenter().generateEventLogger("AigcMix");
            logger.setVisibleLevel(level);
            return logger;
        }
    }
}