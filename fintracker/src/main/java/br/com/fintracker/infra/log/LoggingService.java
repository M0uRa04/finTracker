package br.com.fintracker.infra.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingService {
    private final Logger logger = LoggerFactory.getLogger(LoggingService.class);

    public void logInfo(String message, Object... args) {
        logger.info(message, args);
    }

    public void logWarn(String message, Object... args) {
        logger.warn(message, args);
    }

    public void logError(String message, Throwable throwable, Object... args) {
        logger.error(message, throwable, args);
    }

    public void logDebug(String message, Object... args) {
        logger.debug(message, args);
    }

    public void logTrace(String message, Object... args) {
        logger.trace(message, args);
    }
}
