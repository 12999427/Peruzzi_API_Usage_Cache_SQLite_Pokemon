import javax.management.InstanceNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

public class RequestAgent {
    private API api;
    private DatabaseManager dbm;

    private static RequestAgent ra;
    public static RequestAgent getInstance () throws SQLException {
        if (ra == null)
        {
            ra = new RequestAgent();
        }
        return ra;
    }

    private RequestAgent() throws SQLException {
        dbm = DatabaseManager.getInstance();
        api = API.getInstance();
    }

    public Pokemon RequestPokemonByNameOrId(String txt)
    {
        Integer id = null;
        try {
            id = Integer.parseInt(txt);
        } catch (NumberFormatException e)
        {
        }
        Pokemon p;
        try {
            if (id == null) {
                p = dbm.FindPokemonByName(txt);
            } else {
                p = dbm.FindPokemonById(id);
            }
            System.out.println("Trovato nel db sqlite");
        } catch (InstanceNotFoundException e)
        {
            try {
                p = api.getPokemonByIdOrName(txt);
                dbm.AddPokemon(p);
                System.out.println("Cercato dall'api e aggiunto al db sqlite");
            } catch (Exception _) {
                return null;
            }
        }
        return p;
    }

}
