package com.tx.co.common.scheduler.service;

import com.tx.co.cache.service.CacheDataLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService extends CacheDataLoader implements ISchedulerService {

	// set this to false to disable this job; set it it true by
	@Value("${job.dailyScheduled.enable:false}")
	private boolean scheduledJobEnabled;

	@Value("${scheduler.maxAttempts}")
	private Integer maxAttempts;

	//    @Scheduled(cron="*/10 * * * * *")
	@Override
	public void jobScheduled() {
		if(scheduledJobEnabled) {
			init();
		}
	}

}
