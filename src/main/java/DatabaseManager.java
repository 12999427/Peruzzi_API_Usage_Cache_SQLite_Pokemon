import javax.management.InstanceNotFoundException;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static DatabaseManager dbm;
    private final Connection connection;

    public static DatabaseManager getInstance() throws SQLException {
        if (dbm == null)
        {
            dbm = new DatabaseManager("jdbc:sqlite:pokemon.db");
        }
        return dbm;
    }

    private DatabaseManager(String url) throws SQLException {
        connection = DriverManager.getConnection(url);
        CreateTablesIfNotExist();
    }

    private boolean testConnection ()
    {
        try {
            return connection.isValid(5);
        } catch (SQLException e)
        {
            return false;
        }
    }

    public void CreateTablesIfNotExist ()
    {
        String sql = """
                create table if not exists pokemon
                (
                    id    integer not null
                        constraint pokemon_pk
                            primary key,
                    nome  text    not null,
                    tipo1 text    not null,
                    tipo2 text
                );
                """;

        try (Statement s = connection.createStatement())
        {
            s.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean AddPokemon(Pokemon p)
    {
        String sql = "INSERT OR IGNORE INTO pokemon (id, nome, tipo1, tipo2) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, p.id());
            ps.setString(2, p.name());
            ps.setString(3, p.type1());
            if (p.type2() == null)
                ps.setNull(4, Types.VARCHAR);
            else
                ps.setString(4, p.type2());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Pokemon FindPokemonByName(String name) throws InstanceNotFoundException {
        String sql = "SELECT id, nome, tipo1, tipo2 FROM pokemon WHERE nome = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name.toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    String nome = rs.getString(2);
                    String tipo1 = rs.getString(3);
                    String tipo2 = rs.getString(4);

                    return new Pokemon(id, nome, tipo1, tipo2);
                }
                else throw new InstanceNotFoundException();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Pokemon FindPokemonById(int _id) throws InstanceNotFoundException {
        String sql = "SELECT id, nome, tipo1, tipo2 FROM pokemon WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, _id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    String nome = rs.getString(2);
                    String tipo1 = rs.getString(3);
                    String tipo2 = rs.getString(4);

                    return new Pokemon(id, nome, tipo1, tipo2);
                }
                else throw new InstanceNotFoundException();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
