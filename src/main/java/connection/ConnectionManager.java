package connection;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class  ConnectionManager {

    private static Connection connection;

    // 1.1. Stwórz metodę getConnection, która będzie miała korzystała ze statycznego pola typu Connection, która w razie potrzeby ustawi wartość polu i która zwróci jego wartość (analogicznie dla singletonu).
    // <<Jeśli nie uda się zrobić w ciągu trzech minut to to sygnalizujemy!!!>>
    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            // Aby uzyskać obiekt połączenia użyjemy obiektu typu MysqlDataSource.
            MysqlDataSource dataSource = new MysqlDataSource();
            // Ustawimy maszynę, na której jest baza (localhost to ten komputer, na którym aplikacja jest uruchomiona)
            dataSource.setServerName("localhost");
            // Ustawiamy nazwę bazy
            dataSource.setDatabaseName("work");
            // Ustawimy port (domyślny port, na którym działa baza MySQL to 3306, jeśli w trakcie instalacji nie wybraliśmy)
            // inaczej lub później nie edytowaliśmy ustawienia, to będzie to ta wartość.
            dataSource.setPort(3306);
            // Użytkownik, którego chcemy użyć
            dataSource.setUser("root");
            // Hasło, którego chcemy użyć.
            dataSource.setPassword("1234");


            dataSource.setServerTimezone("GMT+2");
            dataSource.setUseSSL(false);


            // Nie będziemy używać certyfikatu aby łączyć się z bazą (pozbywamy się ostrzeżenia z drukowanego w konsoli).
//            dataSource. setUseSSL(false);
            // Uzyskujemy obiekt typu Connection.
            connection = dataSource.getConnection();

            // Moglibyśmy też uzyskać obiekt typu Connection za pomocą:
            // Class.forName("com.mysql.jdbc.Driver");
            // lub
            // DriverManager.registerDriver(new Driver()); //- gdzie Driver to com.mysql.jdbc.Driver
            // a następnie użycie metody
            // connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/appdb?useSSL=false","root","password");
            // (oczywiście dane musimy odpowiednio pozmieniać, aby tyczyły się bazy, której chcemy użyć).
        }
        return connection;
    }
}
