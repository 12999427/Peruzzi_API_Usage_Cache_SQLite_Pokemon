import java.util.List;

public record Pokemon(int id, String name, String type1, String type2) {
    @Override
    public String toString()
    {
        return String.format("[Pokemon #%d %s - Tipi: %s %s]", id, name, type1, type2);
    }
}
