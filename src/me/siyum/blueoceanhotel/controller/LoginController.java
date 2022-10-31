package me.siyum.blueoceanhotel.controller;

import me.siyum.blueoceanhotel.util.CRUDUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    public static int login(String user, String pass) throws SQLException, ClassNotFoundException {
        ResultSet res = CRUDUtil.execute("SELECT role FROM user WHERE username=? AND password=?",
                user, pass
        );
        if (res.next()) {
            if (res.getString("role").equals("admin")) {
                return 1;
            } else if (res.getString("role").equals("staff")) return 0;
        }
        return -1;
    }
}
