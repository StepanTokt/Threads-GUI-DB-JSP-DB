package by.tokt.dao;

import java.sql.DriverManager;
import java.sql.SQLException;


//класс для подключения к бд
public class Connection {

    public static java.sql.Connection getConnection() {
        java.sql.Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); //подгружаем JDBC и устанавливаем соединение
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/films", "root", "1015");
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException " + e);
        } catch (SQLException e) {
            System.out.println("SQLException " + e);
        }
        return connection;
    }

}
