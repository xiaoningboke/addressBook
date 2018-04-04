package com.sunxiaoning;

import java.sql.*;
import java.util.ResourceBundle;

public class DButil {
    private static String driverClass;
    private static String url;
    private static String user;
    private static String password;

    static{
        ResourceBundle rb = ResourceBundle.getBundle("dbinfo");
        //给上面4个变量赋值
        driverClass = rb.getString("driverClass");
        url = rb.getString("url");
        user = rb.getString("user");
        password = rb.getString("password");
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    //得到连接
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    //关闭资源
    public static void closeAll(ResultSet rs, Statement stmt, Connection conn){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            rs = null;
        }
        if(stmt!=null){
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            stmt = null;
        }
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conn = null;
        }
    }
}
