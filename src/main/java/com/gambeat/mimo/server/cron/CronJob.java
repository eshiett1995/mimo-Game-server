package com.gambeat.mimo.server.cron;

import com.gambeat.mimo.server.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@Configuration
@EnableScheduling
public class CronJob {

    final
    MatchService matchService;

    public CronJob(MatchService matchService) {
        this.matchService = matchService;
    }

    @Scheduled(fixedRateString = "${royal.rumble.cron.time}")
    public void calculateWinnerOfRoyalRumble() {
        System.out.println("cron is running " +  new Date().toString());
        matchService.endRoyalRumbleMatchesCronJob();
    }
}
