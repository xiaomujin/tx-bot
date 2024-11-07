package com.neko.txbot.listener;

import com.neko.txbot.util.PuppeteerUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class StartedListener implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("系统启动成功");
//        safeRun(PuppeteerUtil::getBrowser);
    }

    private void safeRun(Consumer fun) {
        try {
            fun.run();
        } catch (Exception e) {
            log.error("执行异常", e);
        }
    }

    @FunctionalInterface
    public interface Consumer {
        void run();
    }
}
