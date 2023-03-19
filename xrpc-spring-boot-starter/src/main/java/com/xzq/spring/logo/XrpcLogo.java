package com.xzq.spring.logo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/19 18:42
 */
@Slf4j
public class XrpcLogo {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String VERSION = "1.0.1-SNAPSHOT";
    private static final String DYNAMIC_LOGO = "                             \n" +
            "                             \n" +
            " __  __  _ __    _ __    ___ \n" +
            " \\ \\/ / | '_ \\  | '__|  / __|\n" +
            "  >  <  | |_) | | |    | (__ \n" +
            " /_/\\_\\ | .__/  |_|     \\___|\n" +
            "        | |                  \n" +
            "        |_|                  ";

    private final AtomicBoolean alreadyLog = new AtomicBoolean(false);

    private String buildBannerText() {
        return LINE_SEPARATOR
                + LINE_SEPARATOR
                + DYNAMIC_LOGO
                + LINE_SEPARATOR
                + " :: Xrpc  " + VERSION + " \n";
    }

    public void println() {
        if (!alreadyLog.compareAndSet(false, true)) {
            return;
        }
        log.info(buildBannerText());
    }

}
