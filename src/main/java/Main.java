import java.sql.SQLException;

import static java.lang.IO.println;
import static java.lang.IO.readln;
import static java.lang.System.exit;

void main() {
    RequestAgent ra = null;
    try {
        ra = RequestAgent.getInstance();
    } catch (SQLException e) {
        System.out.println("Impossibile connettersi");
        exit(1);
    }
    while (true) {
        String in = readln("Inserisci nome o id pokemon: ");
        Pokemon p = ra.RequestPokemonByNameOrId(in);
        println(p == null ? "Non trovato" : p);
        println();
    }
}