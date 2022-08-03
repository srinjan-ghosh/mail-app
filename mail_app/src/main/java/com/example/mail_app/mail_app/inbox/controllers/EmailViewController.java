package com.example.mail_app.mail_app.inbox.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.mail_app.mail_app.inbox.email.Email;
import com.example.mail_app.mail_app.inbox.email.EmailRepository;
import com.example.mail_app.mail_app.inbox.emailList.EmailListItem;
import com.example.mail_app.mail_app.inbox.emailList.EmailListItemKey;
import com.example.mail_app.mail_app.inbox.emailList.EmailListItemRepository;
import com.example.mail_app.mail_app.inbox.folders.Folder;
import com.example.mail_app.mail_app.inbox.folders.FolderRepository;
import com.example.mail_app.mail_app.inbox.folders.FolderService;
import com.example.mail_app.mail_app.inbox.folders.UnreadEmailStatsService;

@Controller
public class EmailViewController {

    @Autowired private FolderRepository folderRepository;
    @Autowired private FolderService folderService;
    @Autowired private EmailRepository emailRepository;
    @Autowired private EmailListItemRepository emailListItemRepository;
    @Autowired private UnreadEmailStatsService unreadEmailStatsService;
    
    @GetMapping("/email/{id}")
    public String emailView(
        @AuthenticationPrincipal OAuth2User principal,
        Model model, 
        @PathVariable long id,
        @RequestParam String folder
    ) {
        // if(principal == null|| !StringUtils.hasText(principal.getAttribute("name"))){
        if(principal == null || !StringUtils.hasText(principal.getAttribute("login"))){
            return "index";
        }

        // show the folders
        String userId = principal.getAttribute("login");
        model.addAttribute("userId", userId);
        List<Folder> userFolders = folderRepository.findAllByUserId(userId);
        model.addAttribute("userFolders", userFolders);

        List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
        model.addAttribute("defaultFolders", defaultFolders);
 

        // show the emails
        Optional<Email> optionalEmail = emailRepository.findById(id);
        if(!optionalEmail.isPresent()) {
            return "inbox-page";
        }

        Email email = optionalEmail.get();
        String toIds = String.join(", ", email.getTo());

        model.addAttribute("email", optionalEmail.get());
        model.addAttribute("toIds", toIds);
        
        EmailListItemKey key = new EmailListItemKey();
        key.setId(userId);
        key.setLabel(folder);
        key.setTimeUuid(email.getId());

        Optional<EmailListItem> optionalEmailListItem = emailListItemRepository.findById(key);
        if(optionalEmailListItem.isPresent()) {
            EmailListItem emailListItem = optionalEmailListItem.get();
            if (emailListItem.isUnread()) {
                emailListItem.setUnread(false);
                emailListItemRepository.save(emailListItem);
                unreadEmailStatsService.decrementUnreadCount(userId, folder);
            }
        }

        Map<String, Integer> mapCountToLabels = folderService.mapCountToLabels(userId);
        model.addAttribute("stats", mapCountToLabels);

        return "email-page";
    }
}
