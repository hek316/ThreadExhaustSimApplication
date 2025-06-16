package hek.threadexhaustsimapplication.controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

@Component
@Slf4j
public class TomcatThreadPoolMonitor implements ApplicationListener<WebServerInitializedEvent> {

    private volatile org.apache.tomcat.util.threads.ThreadPoolExecutor executor;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        if (event.getWebServer() instanceof TomcatWebServer tomcatWebServer) {
            var connector = tomcatWebServer.getTomcat().getConnector();
            ProtocolHandler handler = connector.getProtocolHandler();
            if (handler instanceof AbstractProtocol<?> protocol) {
                Executor exec = protocol.getExecutor();
                if (exec instanceof org.apache.tomcat.util.threads.ThreadPoolExecutor tpe) {
                    this.executor = tpe;
                    log.info("✅ Tomcat ThreadPoolExecutor initialized");
                }
            }
        }
    }

    public String getStatus() {
        if (executor == null) {
            return "ThreadPoolExecutor not initialized";
        }

        return String.format(
                "Pool Size: %d, Active Threads: %d, Completed Tasks: %d, Task Count: %d, Queue Size: %d",
                executor.getPoolSize(),
                executor.getActiveCount(),
                executor.getCompletedTaskCount(),
                executor.getTaskCount(),
                executor.getQueue().size()
        );
    }
}