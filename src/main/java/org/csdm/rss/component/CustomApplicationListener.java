package org.csdm.rss.component;

import org.csdm.rss.service.SchedulingService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class CustomApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private final SchedulingService schedulingService;

    public CustomApplicationListener(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        schedulingService.executeTask();
    }
}
