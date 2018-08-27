package com.tx.co.cache.schedule;

import com.tx.co.cache.service.CacheDataLoader;
import com.tx.co.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.cache.CacheManager;

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
