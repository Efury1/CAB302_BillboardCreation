import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StartServer {

    public static final String CREATE_DB="";

    public static final String DELETE = "DELETE FROM people WHERE name='Joe Bloggs'";
    public static final String INSERT = "INSERT INTO people VALUES ('Joe Bloggs', 42)";

    public static final String RENAME1 = "RENAME TABLE names TO people";

    public static final String RENAME2 = "RENAME TABLE people TO names";

    public static final String SELECT = "SELECT * FROM people ORDER BY age";

    public static final String UPDATE = "UPDATE people SET age=52 WHERE name='Joe Bloggs'";

    public static void main(String[] args) {
        Connection connection = DBConnection.getInstance();
        try {
            Statement st = connection.createStatement();

            // rename the table
            st.execute(RENAME1);
            displayContents(st);

            st.executeUpdate(INSERT);

            displayContents(st);

            st.executeUpdate(UPDATE);

            displayContents(st);

            // reset to original database
            st.executeUpdate(DELETE);
            displayContents(st);
            st.execute(RENAME2);

            st.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void displayContents(Statement st) throws SQLException {
        // get all current entries
        ResultSet rs = st.executeQuery(SELECT);

        // use metadata to get the number of columns
        int columnCount = rs.getMetaData().getColumnCount();

        // output the column names
        for (int i = 0; i < columnCount; i++) {
            System.out.printf("%-20s", rs.getMetaData().getColumnName(i + 1));
        }
        System.out.printf("%n");

        // output each row
        while (rs.next()) {
            for (int i = 0; i < columnCount; i++) {
                System.out.printf("%-20s", rs.getString(i + 1));
            }
            System.out.printf("%n");
        }
        System.out.printf("%n");
    }
}
