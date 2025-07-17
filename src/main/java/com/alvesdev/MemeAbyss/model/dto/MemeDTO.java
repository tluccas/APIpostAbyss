package com.alvesdev.MemeAbyss.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "Objeto de resposta com dados do meme (titulo e url da imagem)")
public class MemeDTO {

    @Schema(description = "Título do meme", example = "Depois dessa vou dormir")
    String titulo;
    @Schema(description = "URL da imagem do meme", example = "https://i.redd.it/7lwl2lpsq9df1.jpeg")
    String url;


}
