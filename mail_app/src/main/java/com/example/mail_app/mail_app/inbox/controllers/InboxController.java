package com.example.mail_app.mail_app.inbox.controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.mail_app.mail_app.inbox.emailList.EmailListItem;
import com.example.mail_app.mail_app.inbox.emailList.EmailListItemRepository;
import com.example.mail_app.mail_app.inbox.folders.Folder;
import com.example.mail_app.mail_app.inbox.folders.FolderRepository;
import com.example.mail_app.mail_app.inbox.folders.FolderService;

@Controller
public class InboxController {

    @Autowired private FolderRepository folderRepository;
    @Autowired private FolderService folderService;
    @Autowired private EmailListItemRepository emailListItemRepository;
    
    @GetMapping("/")
    public String homePage(@AuthenticationPrincipal OAuth2User principal, Model model, @RequestParam(required=false) String folder) {
        // if(principal == null|| !StringUtils.hasText(principal.getAttribute("name"))){
        if(principal == null){
            return "index";
        }

        String userId = principal.getAttribute("login");
        model.addAttribute("userId", userId);
        List<Folder> userFolders = folderRepository.findAllByUserId(userId);
        model.addAttribute("userFolders", userFolders);

        List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
        model.addAttribute("defaultFolders", defaultFolders);

        Map<String, Integer> mapCountToLabels = folderService.mapCountToLabels(userId);
        model.addAttribute("stats", mapCountToLabels); 

        //fetch messages
        if(!StringUtils.hasText(folder)){
            folder = "Inbox";
        }
        
        List<EmailListItem> emailList = emailListItemRepository.findAllByKey_IdAndKey_LabelOrderByKey_TimeUuidDesc(userId, folder);
        PrettyTime prettyTime = new PrettyTime();
        emailList.stream().forEach(emailItem -> {
            long time = emailItem.getKey().getTimeUuid();
            Date emailDateTime = new Date(time);
            emailItem.setAgoTimeString(prettyTime.format(emailDateTime));
        });
        model.addAttribute("emailList", emailList);
        model.addAttribute("folderName", folder);

        return "inbox-page";
    }
}
