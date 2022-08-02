package com.example.mail_app.mail_app.inbox.emailList;


public class EmailListItemKey {

    // user id of the persons inbox
    private String id;

    private String label;
    // will be used for message id. Time based
    private long timeUuid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getTimeUuid() {
        return timeUuid;
    }

    public void setTimeUuid(long timeUuid) {
        this.timeUuid = timeUuid;
    }

    
    
}
