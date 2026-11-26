import java.util.List;

public record PokemonApiResponse(int id, String name, List<Type> types) {
}
