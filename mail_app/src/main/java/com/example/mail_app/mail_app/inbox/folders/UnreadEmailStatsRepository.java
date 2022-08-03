package com.example.mail_app.mail_app.inbox.folders;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnreadEmailStatsRepository extends MongoRepository<UnreadEmailStats, String> {
    List<UnreadEmailStats> findAllByUserId(String userId);

    Optional<UnreadEmailStats> findByUserIdAndLabel(String userId, String label);
}
