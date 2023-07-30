package io.bootify.visit_managment_system.util;

import io.bootify.visit_managment_system.domain.Visit;
import io.bootify.visit_managment_system.model.VisitStatus;
import io.bootify.visit_managment_system.repos.VisitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
public class VisitExpiredScheduledTask {
    private static Logger LOGGER = LoggerFactory.getLogger(VisitExpiredScheduledTask.class);
    @Autowired
    private VisitRepository visitRepository;

//    @Scheduled(cron = "0 15 10 15* ?") this is scheduling a task to be executed at 10:15 AM on 15th day of every month;
    @Scheduled(fixedDelay = 100000)
    public void markVisitsExpire(){
        LOGGER.info("Starting the task mark visit");
        List<Visit> visitList = visitRepository.findByStatus(VisitStatus.WAITING);
        for (Visit visit:visitList){
            visit.setStatus(VisitStatus.EXPIRED);
        }
        visitRepository.saveAll(visitList);
        LOGGER.info("completed the task mark visit");
    }

}
