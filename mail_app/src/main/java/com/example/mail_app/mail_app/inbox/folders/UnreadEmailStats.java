package com.example.mail_app.mail_app.inbox.folders;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value="unread_email_stats")
public class UnreadEmailStats {

    @Id
    private String userId;
    private String label;
    private int unreadCount;

    public UnreadEmailStats() {
    }

    public UnreadEmailStats(String userId, String label, int unreadCount) {
        this.userId = userId;
        this.label = label;
        this.unreadCount = unreadCount;
    }

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
    public int getUnreadCount() {
        return unreadCount;
    }
    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    
}
