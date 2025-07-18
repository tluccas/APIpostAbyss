package com.alvesdev.MemeAbyss.service;


import com.alvesdev.MemeAbyss.model.dto.Reddit.Child;
import com.alvesdev.MemeAbyss.model.dto.MemeDTO;
import com.alvesdev.MemeAbyss.model.dto.Reddit.PostData;
import com.alvesdev.MemeAbyss.model.dto.Reddit.RedditResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service

public class MemeAbyssService {

    public MemeDTO searchMeme(){
        try{
            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(""))
                    .header("User-Agent", "MemeAbyss/1.0 Hospedado")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            String json = response.body();

            System.out.println("Status HTTP: " + status);
            System.out.println("Resposta JSON (primeiros 500 chars): " + (json.length() > 500 ? json.substring(0, 500) : json));

            if (status != 200) {
                System.err.println("Erro HTTP: " + status);
                System.err.println("Resposta do servidor: " + json);
                return new MemeDTO("ERRO NA RESPOSTA DO REDDIT", "");
            }

            // Mapeando Json para objetos
            ObjectMapper mapper = new ObjectMapper();
            RedditResponse redditResponse = mapper.readValue(json, RedditResponse.class);

            // Filtro para imagens
            List<PostData> imageMemes = redditResponse.getData().getChildren().stream()
                    .map(Child::getPostData)
                    .filter(pd -> pd != null)
                    .filter(pd -> {
                        String hint = pd.getTipoPost();
                        String url = pd.getUrl();
                        String urlDest = pd.getUrlOverriddenByDest();

                        boolean hasImageHint = hint != null && (hint.equals("image") || hint.equals("link"));
                        boolean hasValidUrl = (url != null && (url.endsWith(".jpg") || url.endsWith(".jpeg") || url.endsWith(".png") || url.endsWith(".gif")))
                                || (urlDest != null && (urlDest.endsWith(".jpg") || urlDest.endsWith(".jpeg") || urlDest.endsWith(".png") || urlDest.endsWith(".gif")));

                        boolean hasPreviewImages = pd.getPreview() != null && pd.getPreview().getImages() != null && !pd.getPreview().getImages().isEmpty();

                        return hasImageHint && (hasValidUrl || hasPreviewImages);
                    })
                    .collect(Collectors.toList());


            if(imageMemes.isEmpty()){

                return new MemeDTO("ERRO: Nenhum meme encontrado", "");
            }
            //Randomiza
            PostData pd = imageMemes.get(new Random().nextInt(imageMemes.size()));

            String imagemUrl = null;

            if (pd.getUrlOverriddenByDest() != null) {
                imagemUrl = pd.getUrlOverriddenByDest();
            } else if (pd.getUrl() != null) {
                imagemUrl = pd.getUrl();
            } else if (pd.getPreview() != null && pd.getPreview().getImages() != null && !pd.getPreview().getImages().isEmpty()) {
                imagemUrl = pd.getPreview().getImages().get(0).getSource().getUrl();
            }

            // Remover entidades HTML
            if (imagemUrl != null) {
                imagemUrl = imagemUrl.replaceAll("&amp;", "&");
            }
            //DTO
            MemeDTO memeFinal = new MemeDTO();
            memeFinal.setTitulo(pd.getTitulo());
            memeFinal.setUrl(imagemUrl);

            return memeFinal;
        }catch (Exception e){
            e.printStackTrace();
            return new MemeDTO("ERRO", "");
        }
    }
}
