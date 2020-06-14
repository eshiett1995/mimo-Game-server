package com.gambeat.mimo.server.cron;

import com.gambeat.mimo.server.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

//@Configuration
//@EnableScheduling
public class CronJob {

    @Autowired
    MatchService matchService;

    //@Scheduled(fixedRateString = "${royal.rumble.cron.time}")
    public void calculateWinnerOfRoyalRumble() {
        //matchService.endRoyalRumbleMatchesCronJob();
    }
}
