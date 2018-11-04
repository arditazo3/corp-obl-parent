package com.tx.co.common.scheduler.service;

import com.tx.co.cache.service.CacheDataLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService extends CacheDataLoader implements ISchedulerService {

	private Scheduler scheduler;
	
	@Autowired
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	// set this to false to disable this job; set it it true by
	@Value("${job.dailyScheduled.enable:false}")
	private boolean scheduledJobEnabled;

	@Value("${scheduler.maxAttempts}")
	private Integer maxAttempts;

	// @Scheduled(cron="*/10 * * * * *")
	// @Scheduled(cron="0 0 0 * * *")
	@Override
	public void jobScheduled() {
		if(scheduledJobEnabled) {
			init();
			
			scheduler.execute();
		}
	}

}
