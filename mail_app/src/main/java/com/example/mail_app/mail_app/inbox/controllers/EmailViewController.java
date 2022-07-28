package com.example.mail_app.mail_app.inbox.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.mail_app.mail_app.inbox.email.Email;
import com.example.mail_app.mail_app.inbox.email.EmailRepository;
import com.example.mail_app.mail_app.inbox.folders.Folder;
import com.example.mail_app.mail_app.inbox.folders.FolderRepository;
import com.example.mail_app.mail_app.inbox.folders.FolderService;

@Controller
public class EmailViewController {

    @Autowired private FolderRepository folderRepository;
    @Autowired private FolderService folderService;
    @Autowired private EmailRepository emailRepository;
    
    @GetMapping("/email/{id}")
    public String emailView(@AuthenticationPrincipal OAuth2User principal, Model model, @PathVariable long id) {
        // if(principal == null|| !StringUtils.hasText(principal.getAttribute("name"))){
        if(principal == null){
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
        return "email-page";
    }
}
