package com.example.mail_app.mail_app.inbox.email;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mail_app.mail_app.inbox.emailList.EmailListItem;
import com.example.mail_app.mail_app.inbox.emailList.EmailListItemKey;
import com.example.mail_app.mail_app.inbox.emailList.EmailListItemRepository;
import com.example.mail_app.mail_app.inbox.folders.UnreadEmailStatsRepository;
import com.example.mail_app.mail_app.inbox.folders.UnreadEmailStatsService;

@Service
public class EmailService {

    @Autowired EmailRepository emailRepository;
    @Autowired EmailListItemRepository emailListItemRepository;
    @Autowired UnreadEmailStatsRepository unreadEmailStatsRepository;
    @Autowired UnreadEmailStatsService unreadEmailStatsService;

    public void sendEmail(String from, List<String> to, String subject, String body) {

        Email email = new Email();
        email.setTo(to);
        email.setFrom(from);
        email.setBody(body);
        email.setSubject(subject);
        email.setId(Calendar.getInstance().getTimeInMillis());

        emailRepository.save(email);


        to.forEach(toId -> {
            EmailListItem toIdItem = createEmailListItem(to, subject, email, toId, "Inbox");
            emailListItemRepository.save(toIdItem);
            // update unread counter of the email recipient
            unreadEmailStatsService.incrementUnreadCount(toId, "Inbox");
        });

        EmailListItem sentItemEntry = createEmailListItem(to, subject, email, from, "Sent Items");
        sentItemEntry.setUnread(false);
        emailListItemRepository.save(sentItemEntry);
    }

    private EmailListItem createEmailListItem(List<String> to, String subject, Email email, String itemOwner, String folder) {
        EmailListItemKey key = new EmailListItemKey();
        key.setId(itemOwner);
        key.setLabel(folder);
        key.setTimeUuid(email.getId());

        EmailListItem item = new EmailListItem();
        item.setKey(key);
        item.setTo(to);
        item.setSubject(subject);
        item.setUnread(true);
        return item;
    }
}
