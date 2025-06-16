package hek.threadexhaustsimapplication.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TomcatThreadPoolStatusController {

    private final TomcatThreadPoolMonitor monitor;

    @GetMapping("/tomcat/threadpool-status")
    public String getThreadPoolStatus() {
        return monitor.getStatus();
    }

    @GetMapping("/block")
    public String block(@RequestParam(defaultValue = "10000") long millis) throws InterruptedException {
        log.info("Thread {} is blocking for {} ms", Thread.currentThread().getName(), millis);
        Thread.sleep(millis); // 요청 처리 중 스레드 블로킹
        return "Completed after " + millis + "ms";
    }
}