package com.example.mail_app.mail_app;

import java.util.Arrays;
import java.util.Calendar;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mail_app.mail_app.inbox.emailList.EmailListItem;
import com.example.mail_app.mail_app.inbox.emailList.EmailListItemKey;
import com.example.mail_app.mail_app.inbox.emailList.EmailListRepository;
import com.example.mail_app.mail_app.inbox.folders.Folder;
import com.example.mail_app.mail_app.inbox.folders.FolderRepository;

@SpringBootApplication
@EnableMongoRepositories
@RestController
public class MailAppApplication {

	@Autowired FolderRepository folderRepository;
	@Autowired EmailListRepository emailListRepository;

	public static void main(String[] args) {
		SpringApplication.run(MailAppApplication.class, args);
	}

	// using github oauth login

	@RequestMapping("/user")
	public String user(@AuthenticationPrincipal OAuth2User principal) {
		System.out.println(principal);
		return "Login Id:"+principal.getAttribute("login")+" Id: "+ principal.getAttribute("id");
	}

	@PostConstruct
	public void init() {
		//delete all before inserting. Testing purposes
		folderRepository.deleteAll();

		emailListRepository.deleteAll();

		folderRepository.save(new Folder("srinjan-ghosh", "User Inbox", "blue"));
		folderRepository.save(new Folder("srinjan-ghosh", "Sent", "green"));
		folderRepository.save(new Folder("srinjan-ghosh", "Important", "yellow"));

		for(int i=0;i<10;i++){
			EmailListItemKey key = new EmailListItemKey();
			key.setId("srinjan-ghosh");
			key.setLabel("Inbox");
			key.setTimeUuid(Calendar.getInstance().getTimeInMillis());

			EmailListItem item = new EmailListItem();
			item.setKey(key);
			item.setTo(Arrays.asList("srinjan-ghosh"));
			item.setSubject("Subject"+i);
			item.setUnread(true);

			emailListRepository.save(item);
		}

	}

}
