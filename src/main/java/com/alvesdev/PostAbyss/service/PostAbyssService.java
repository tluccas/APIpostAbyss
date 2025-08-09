package com.alvesdev.PostAbyss.service;


import com.alvesdev.PostAbyss.model.dto.Reddit.Child;
import com.alvesdev.PostAbyss.model.dto.MemeDTO;
import com.alvesdev.PostAbyss.model.dto.Reddit.PostData;
import com.alvesdev.PostAbyss.model.dto.Reddit.RedditResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service

public class PostAbyssService {

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
                    .filter(Objects::nonNull)
                    .filter(PostFilter::isValidImagePost) // método estático criado por você
                    .toList();


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

    public static class PostFilter {
        public static boolean isValidImagePost(PostData pd) {
            var hint = pd.getTipoPost();
            var url = pd.getUrl();
            var urlDest = pd.getUrlOverriddenByDest();

            boolean hasImageHint = hint != null && (hint.equals("image") || hint.equals("link"));

            boolean hasValidUrl = false;
            if (url != null) {
                hasValidUrl = url.endsWith(".jpg") || url.endsWith(".jpeg") || url.endsWith(".png") || url.endsWith(".gif");
            }
            if (!hasValidUrl && urlDest != null) {
                hasValidUrl = urlDest.endsWith(".jpg") || urlDest.endsWith(".jpeg") || urlDest.endsWith(".png") || urlDest.endsWith(".gif");
            }

            var preview = pd.getPreview();
            boolean hasPreviewImages = preview != null
                    && preview.getImages() != null
                    && !preview.getImages().isEmpty();

            return hasImageHint && (hasValidUrl || hasPreviewImages);
        }
    }

}
