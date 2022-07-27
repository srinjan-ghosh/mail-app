package com.example.mail_app.mail_app.inbox.folders;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderRepository extends MongoRepository<Folder, String> {

    List<Folder> findAllByUserId(String userId);
    
}
