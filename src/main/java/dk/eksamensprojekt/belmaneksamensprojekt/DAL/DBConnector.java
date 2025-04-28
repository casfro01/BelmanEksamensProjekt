package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

// sql imports
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

// java imports
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;


public class DBConnector {
    //designate file path for db settings
    private static final String PROPERTIES_FILE = "config/config.settings";
    private SQLServerDataSource dataSource;

    public DBConnector() throws IOException {
        //load db settings to properties
        Properties props = new Properties();
        props.load(new FileInputStream(new File(PROPERTIES_FILE)));

        //dataSource configured with database connection details
        dataSource = new SQLServerDataSource();
        dataSource.setServerName(props.getProperty("Server"));
        dataSource.setDatabaseName(props.getProperty("Database"));
        dataSource.setUser(props.getProperty("User"));
        dataSource.setPassword(props.getProperty("Password"));
        dataSource.setPortNumber(1433);
        dataSource.setTrustServerCertificate(true);

    }

    public Connection getConnection() throws SQLServerException {
        return dataSource.getConnection();
    }

    //method to check whether connection to database is true or false
    public static void main(String[] args) throws Exception {
        DBConnector dbConnecter = new DBConnector();

        try (Connection connection = dbConnecter.getConnection()) {
            System.out.println("open = " + !connection.isClosed());
        } //Connection gets closed here
    }
}
