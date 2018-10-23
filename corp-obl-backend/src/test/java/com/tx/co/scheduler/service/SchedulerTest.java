package com.tx.co.scheduler.service;

import com.tx.co.abstraction.AbstractServiceTest;
import com.tx.co.common.scheduler.service.Scheduler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Tests for scheduler
 *
 * @author rfratti
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SchedulerTest extends AbstractServiceTest {

    @Test
    public void executeScheduler() {
        Scheduler scheduler = new Scheduler();
        scheduler.setExpirationService(super.getExpirationService());
        scheduler.setTaskService(super.getTaskService());
        scheduler.setUserService(super.getUserService());
        scheduler.execute();
    }
}
