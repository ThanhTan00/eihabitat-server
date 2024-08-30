//package com.eihabitat.eihabitat_server.service;
//
//import com.eihabitat.eihabitat_server.entity.Story;
//
//import javax.persistence.PrePersist;
//import javax.persistence.PreUpdate;
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//
//public class StoryListener {
//
//    @PrePersist
//    @PreUpdate
//    public void checkStoryAge(Story story) {
//        if (story.getCreatedAt() == null) {
//            story.setCreatedAt(LocalDateTime.now());
//        } else {
//            LocalDateTime now = LocalDateTime.now();
//            long hoursSinceCreation = ChronoUnit.HOURS.between(story.getCreatedAt(), now);
//
//            if (hoursSinceCreation >= 24) {
//                story.setShouldBeRemoved(true);
//            }
//        }
//    }
//}
