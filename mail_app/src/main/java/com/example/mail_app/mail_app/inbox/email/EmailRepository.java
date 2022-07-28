package com.example.mail_app.mail_app.inbox.email;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends MongoRepository<Email, Long> {
    
}
