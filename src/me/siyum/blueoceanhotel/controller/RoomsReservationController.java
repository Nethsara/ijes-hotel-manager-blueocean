package me.siyum.blueoceanhotel.controller;

import me.siyum.blueoceanhotel.model.Book;
import me.siyum.blueoceanhotel.util.CRUDUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomsReservationController {
    public static ResultSet getLastID() throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("SELECT id FROM book ORDER BY id DESC LIMIT 1");
    }

    public static boolean saveBooking(Book b) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("INSERT INTO book VALUES(?,?,?,?,?,?,?,?)",
                b.getId(), b.getCustomerID(), b.getDate(), b.getTime(), b.getRoomID(), b.getMealID(), b.getPrice(),1);
    }
}
