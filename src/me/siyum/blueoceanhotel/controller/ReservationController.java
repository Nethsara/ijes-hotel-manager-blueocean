package me.siyum.blueoceanhotel.controller;

import me.siyum.blueoceanhotel.util.CRUDUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReservationController {

    public static ResultSet searchText(String searchText) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute(
                "SELECT * FROM book WHERE id LIKE ? || customerID LIKE ? || roomID LIKE ? || mealID LIKE ?",
                searchText, searchText, searchText, searchText);
    }

    public static boolean changeStatus(String id) throws SQLException, ClassNotFoundException {
        ResultSet res = CRUDUtil.execute("SELECT status,roomID FROM book WHERE id=?", id);
        if (res.next()) {
            if (res.getString(1).equalsIgnoreCase("1")) {
                CRUDUtil.execute("UPDATE room_up SET reserved=? WHERE id=?",
                        false, res.getString(2));
                return CRUDUtil.execute("UPDATE book SET status=? WHERE id=?",
                        0, id);
            }
        }
        return false;
    }
}
