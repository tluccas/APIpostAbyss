package com.alvesdev.MemeAbyss.controller;

import com.alvesdev.MemeAbyss.model.dto.MemeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alvesdev.MemeAbyss.service.MemeAbyssService;

@RestController
@RequestMapping("/meme")
@Tag(name = "Memes", description = "Endpoints para buscar memes do Reddit")
public class MemeAbyssController {

    private final MemeAbyssService memeAbyssService;

    public MemeAbyssController(MemeAbyssService memeAbyssService){
        this.memeAbyssService = memeAbyssService;
    }

    @GetMapping("/random")
    @Operation(
            summary = "Obtém um meme aleatório",
            description = "Retorna um meme do tipo imagem aleatório do subreddit inserido na URL"
    )
    public ResponseEntity<MemeDTO> getRandomMeme(){
        MemeDTO meme = memeAbyssService.searchMeme();
        return ResponseEntity.ok(meme);
    }
}
