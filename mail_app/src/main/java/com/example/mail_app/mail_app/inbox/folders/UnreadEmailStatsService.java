package com.example.mail_app.mail_app.inbox.folders;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnreadEmailStatsService {
    @Autowired UnreadEmailStatsRepository unreadEmailStatsRepository;
    
    public void incrementUnreadCount(String userId, String label){
        Optional<UnreadEmailStats> optionalStats = unreadEmailStatsRepository.findByUserIdAndLabel(userId, label);
        if(optionalStats.isPresent()){
            UnreadEmailStats stats = optionalStats.get();
            stats.setUnreadCount(stats.getUnreadCount()+1);
            unreadEmailStatsRepository.save(stats);
        } else {
            UnreadEmailStats newUnreadEmailStats = new UnreadEmailStats();
            newUnreadEmailStats.setUserId(userId);
            newUnreadEmailStats.setLabel(label);
            newUnreadEmailStats.setUnreadCount(1);

            unreadEmailStatsRepository.save(newUnreadEmailStats);
        }
    }

    public void decrementUnreadCount(String userId, String label){
        Optional<UnreadEmailStats> optionalStats = unreadEmailStatsRepository.findByUserIdAndLabel(userId, label);
        if(optionalStats.isPresent()){
            UnreadEmailStats stats = optionalStats.get();
            stats.setUnreadCount(stats.getUnreadCount()-1);
            unreadEmailStatsRepository.save(stats);
        } else {
            UnreadEmailStats newUnreadEmailStats = new UnreadEmailStats();
            newUnreadEmailStats.setUserId(userId);
            newUnreadEmailStats.setLabel(label);
            newUnreadEmailStats.setUnreadCount(0);

            unreadEmailStatsRepository.save(newUnreadEmailStats);
        }
    }
}
