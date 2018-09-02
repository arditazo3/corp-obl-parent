package com.tx.co.cache.schedule;

import com.tx.co.cache.service.CacheDataLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DailyJobScheduled extends CacheDataLoader {

    // set this to false to disable this job; set it it true by
    @Value("${job.dailyScheduled.enable:false}")
    private boolean scheduledJobEnabled;

   // @Scheduled(cron="*/10 * * * * *")
    public void jobScheduled() {
        if(scheduledJobEnabled) {
            init();
        }
    }

}
