package org.connector;

import java.sql.Connection;
import java.sql.DriverManager;

public class connector {

    Connection conn = null;

    public static Connection ConnectDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/bdkindergarten?useUnicode=yes&characterEncoding=UTF-8", "root", "1234");
            return conn;
        } catch (Exception e) {
            System.out.println("Подключение не удалось" + e);
            return null;
        }
    }
}