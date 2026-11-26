import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class API {
    private HttpClient client;
    private final String baseURL = "https://pokeapi.co/api/v2/";
    private static API api;

    public static API getInstance(){
        if (api == null)
        {
            api = new API();
        }
        return api;
    }

    // Potrebbe essere singleton
    private API() {
        this.client = HttpClient.newHttpClient();
    }

    public Pokemon getPokemonByIdOrName (String name) throws IOException, InterruptedException {
        return getPokemonFromResponse(getPokemonReponseByIdOrName(name));
    }

    private PokemonApiResponse getPokemonReponseByIdOrName(String name) throws IOException, InterruptedException {
        HttpRequest httpRequest =
                HttpRequest.newBuilder()
                        .GET()
                        .uri(URI.create(baseURL + "pokemon/" + name))
                        .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Error: "+ response.statusCode() + "\nFailed to retrieve pokemon data");
        }

        Gson gson = new Gson();

        return gson.fromJson(response.body(), PokemonApiResponse.class);
    }

    private Pokemon getPokemonFromResponse (PokemonApiResponse p)
    {
        return new Pokemon(p.id(), p.name(), p.types().get(0).type().name(), (p.types().size() == 2 ? p.types().get(1).type().name() : null));
    }
}
