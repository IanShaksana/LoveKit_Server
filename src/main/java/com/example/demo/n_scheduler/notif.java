package com.example.demo.n_scheduler;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.onesignal.onesignal;
import com.example.demo.project_michelle.repo.rep_profile;
import com.example.demo.project_michelle.repo.rep_task;
import com.example.demo.project_michelle.table.profile;
import com.example.demo.project_michelle.table.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class notif {
    @Autowired
    private rep_task rep_task;
    @Autowired
    private rep_profile rep_profile;

    // example of scheduled task, pleas uncomment one of this

    // Schedule task with fix rate
    // @Scheduled(fixedRate = 10000)

    // Schedule task with delay, will wait after previous task complete

    // Schedule task with spesific repetion visit cronmaker.com
    // extra note, delete last expression from cronmaker.com

    @Scheduled(cron = "0 0 10 1/1 * ?")
    public void example() {
        List<task> task_unfiltered = rep_task.findAll();
        List<task> task_filtered = new ArrayList<>();
        List<profile> profiles = rep_profile.findAll();

        for (profile profile : profiles) {
            profile.setHearttank(profile.getHearttank()-5);
            if(profile.getHearttank()<=0){
                profile.setHearttank(0);
            }
            rep_profile.save(profile);
        }

        for (task task : task_unfiltered) {
            if (task.getStatus() == 1) {
                task_filtered.add(task);
            }
        }

        onesignal warn = new onesignal();

        for (task task : task_filtered) {
            profile currentProfile = rep_profile.findById(task.getIdRelationship()).get();
            warn.sendMessageToUser("Yuk, jangan lupa kerjakan komitmenmu: " + task.getTitle(), currentProfile.getOnesignalid());
        }

    }



}