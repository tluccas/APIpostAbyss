package com.alvesdev.MemeAbyss.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alvesdev.MemeAbyss.service.MemeAbyssService;

@RestController
@RequestMapping("/meme")
public class MemeAbyssController {

    private final MemeAbyssService memeAbyssService;

    public MemeAbyssController(MemeAbyssService memeAbyssService){
        this.memeAbyssService = memeAbyssService;
    }

    @GetMapping
    public ResponseEntity<?> getRandomMeme(){
        String meme = memeAbyssService.searchMeme();
        return ResponseEntity.ok(meme);
    }
}
