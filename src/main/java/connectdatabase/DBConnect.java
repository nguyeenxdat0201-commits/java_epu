package connectdatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    private static final String url = "jdbc:mysql://localhost:3306/quan_cafe";
    private static final String user = "root";
    private static final String password = "1111";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static void checkConnection() {
        try(Connection connection = getConnection()){
            System.out.println("Kết nối thành công tới : "+ connection.getCatalog());
        }catch (SQLException exception){
            System.err.println("Kết nối thất bại: " + exception.getMessage());
        }
    }
}
