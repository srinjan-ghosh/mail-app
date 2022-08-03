package com.example.mail_app.mail_app.inbox.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.mail_app.mail_app.inbox.email.EmailService;
import com.example.mail_app.mail_app.inbox.folders.Folder;
import com.example.mail_app.mail_app.inbox.folders.FolderRepository;
import com.example.mail_app.mail_app.inbox.folders.FolderService;

@Controller
public class ComposeController {

    @Autowired private FolderRepository folderRepository;
    @Autowired private FolderService folderService;
    @Autowired private EmailService emailService;

    @GetMapping(value="/compose")
    public String getComposePage(
        @AuthenticationPrincipal OAuth2User principal, 
        Model model, 
        @RequestParam(required = false) String to
    ) {

        if(principal == null || !StringUtils.hasText(principal.getAttribute("login"))){
            return "index";
        }
        
        // fetch folders
        String userId = principal.getAttribute("login");
        model.addAttribute("userId", userId);
        List<Folder> userFolders = folderRepository.findAllByUserId(userId);
        model.addAttribute("userFolders", userFolders);

        List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
        model.addAttribute("defaultFolders", defaultFolders);

        Map<String, Integer> mapCountToLabels = folderService.mapCountToLabels(userId);
        model.addAttribute("stats", mapCountToLabels); 

        List<String> uniqueToIds = splitToIds(to);
        // possible bug -> reply all will also send to the recipient
        // uniqueToIds.remove(userId);
        model.addAttribute("toIds", String.join(",", uniqueToIds));
        return "compose-page";
    }



    //post method to send the email
    @PostMapping("/sendEmail")
    public ModelAndView sendEmail(
        @AuthenticationPrincipal OAuth2User principal, 
        Model model, 
        @RequestBody MultiValueMap<String, String> formData
    ) {
        if(principal == null){
            // go back to the login page
            return new ModelAndView("redirect:/");
        }

        String from = principal.getAttribute("login");
        List<String> toIds = splitToIds(formData.getFirst("toUserIds"));
        String subject = formData.getFirst("subject");
        String body = formData.getFirst("body");

        emailService.sendEmail(from, toIds, subject, body);

        // return to the home page -> the inbox page for logged in user
        return new ModelAndView("redirect:/");
    }

    private List<String> splitToIds(String to) {
        if(!StringUtils.hasText(to)){
            return new ArrayList<String>();
        }
        String[] splitIds = to.split(",");
        List<String> uniqueToIds = Arrays.asList(splitIds)
                                        .stream()
                                        .map(id -> StringUtils.trimWhitespace(id))
                                        .filter(id-> StringUtils.hasText(id))
                                        .distinct()
                                        .collect(Collectors.toList());
        return uniqueToIds;
    }
}
