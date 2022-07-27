package com.example.mail_app.mail_app.inbox.folders;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "folders_by_user")
public class Folder {

    @Id
    private String userId;
    private String label;
    private String color;

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    } 

    
}
