package servicios;

import org.postgresql.ds.PGSimpleDataSource;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Optional;

import java.util.logging.Logger;


public class CockraochService {
    private PGSimpleDataSource ds;
    private static final Logger LOGGER = Logger.getLogger(CockraochService.class.getName());
    private final String[] statements = {
            // Clear any existing data
            "DROP TABLE IF EXISTS usuarioLog",
            // CREATE the usuarios table
            "CREATE TABLE IF NOT EXISTS usuarioLog (username STRING, date TIMESTAMP)",
            // INSERT a row into the usuarios table
            "INSERT INTO usuarioLog (username, date) VALUES ('admin', NOW())",
            // SELECT a row from the usuarios table
            "SELECT * FROM usuarioLog"
    };


    public CockraochService(PGSimpleDataSource ds) {
        this.ds = ds;
    }

    public void executeStmt(Connection conn, String stmt) {
        try {
            Statement st = conn.createStatement();
            if (stmt.trim().toUpperCase().startsWith("SELECT")) {
                ResultSet rs = st.executeQuery(stmt);
                while (rs.next()) {
                    System.out.println(rs.getString(1));
                }
                rs.close();
            } else {
                int rowsAffected = st.executeUpdate(stmt);
                System.out.println("Filas afectadas: " + rowsAffected);
            }
            st.close();
        } catch (PSQLException e) {
            LOGGER.severe("Error al ejecutar la consulta SQL: " + e.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void addLog(String username) {
        try {
            Connection conn = ds.getConnection();
            String stmt = "INSERT INTO usuarioLog (username, date) VALUES ('" + username + "', NOW())";
            executeStmt(conn, stmt);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
        ds.setUrl(Optional.ofNullable(System.getenv("JDBC_DATABASE_URL")).orElseThrow(
                () -> new IllegalArgumentException("JDBC_DATABASE_URL is not set.")));
        try {
            Connection conn = ds.getConnection();
            System.out.println(conn);
            for (String stmt : this.statements) {
                executeStmt(conn, stmt);
                System.out.println("Ejecutando: " + stmt);
            }
            conn.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}