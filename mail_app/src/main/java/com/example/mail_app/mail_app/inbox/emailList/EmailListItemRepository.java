package com.example.mail_app.mail_app.inbox.emailList;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailListItemRepository extends MongoRepository<EmailListItem, EmailListItemKey> {

    List<EmailListItem> findAllByKey_IdAndKey_LabelOrderByKey_TimeUuidDesc(String id, String label);  
}
