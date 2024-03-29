import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Map;

public class App {

    public static void main(String[] args) throws Exception {

        // fazer uma conexão HTTP e buscar os top 250 filmes
        //API: https://mocki.io/v1/9a7c1ca9-29b4-4eb3-8306-1adb9d159060
        String url = "https://mocki.io/v1/9a7c1ca9-29b4-4eb3-8306-1adb9d159060";
        URI endereco = URI.create(url);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(endereco).GET().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        String body = response.body();

        // extrair só os dados que interessam (titulo, poster, classificação)
        var parser = new JsonParser();
        List<Map<String, String>> listaDeFilmes = parser.parse(body);

        // exibir e manipular os dados 
        var geradora = new GeradoraDeFigurinhas();

        for (Map<String,String> filme : listaDeFilmes) {

            String rating = filme.get("imDbRating");

            String urlImagem = filme.get("image");
            String titulo = filme.get("fullTitle");

            InputStream inputStream = new URL(urlImagem).openStream();            
            String nomeArquivo = titulo.replace(":", "-")  + ".png";


            geradora.cria(inputStream, nomeArquivo, rating);

            System.out.println(titulo);
            System.out.println();
        }
    }
}
