package me.siyum.blueoceanhotel.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import me.siyum.blueoceanhotel.model.Room;
import me.siyum.blueoceanhotel.util.CRUDUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoomsController {

    public static boolean saveRoom(Room room) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("INSERT INTO room_up VALUES(?,?,?,?,?)",
                room.getId(), room.getType(), room.getPrice(), room.isMaintenance(), room.isReserved());
    }

    public static ResultSet searchText(String searchText) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("SELECT * FROM room_up WHERE id LIKE ? || type LIKE ? ORDER BY reserved DESC", searchText, searchText);
    }

    public static boolean deleteRoom(String id) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("DELETE FROM room_up WHERE id=?", id);
    }

    public static boolean updateRoom(Room r) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("UPDATE room_up SET type=?,price=?,status=?,reserved=? WHERE id=?",
                r.getType(), r.getPrice(), r.isMaintenance(), r.isReserved(), r.getId());
    }

    public static ObservableList<String> getAllRooms() throws SQLException, ClassNotFoundException {
        ResultSet set = CRUDUtil.execute("SELECT name FROM roomtypes");

        ArrayList<String> idList = new ArrayList<>();

        while (set.next()) {
            System.out.println("Add");
            idList.add(set.getString("name"));
        }
        return FXCollections.observableArrayList(idList);
    }

    public static ObservableList<String> getAllAvailableRooms() throws SQLException, ClassNotFoundException {
        ResultSet set = CRUDUtil.execute("SELECT id FROM room_up WHERE status=TRUE && reserved=FALSE");

        ArrayList<String> idList = new ArrayList<>();

        while (set.next()) {
            idList.add(set.getString("id"));
        }
        return FXCollections.observableArrayList(idList);
    }

    public static boolean changeStatus(String id) throws SQLException, ClassNotFoundException {

        ResultSet res = CRUDUtil.execute("SELECT status FROM room_up WHERE id=?", id);
        if (res.next()) {
            if (res.getBoolean(1)) {
                CRUDUtil.execute("UPDATE room_up SET reserved=? WHERE id=?",
                        false, id);
                return CRUDUtil.execute("UPDATE room_up SET status=? WHERE id=?",
                        false, id);

            } else {
                CRUDUtil.execute("UPDATE room_up SET reserved=? WHERE id=?",
                        false, id);
                return CRUDUtil.execute("UPDATE room_up SET status=? WHERE id=?",
                        true, id);
            }
        }
        return false;
    }

    public static boolean changeReservationStatus(String id) throws SQLException, ClassNotFoundException {

        ResultSet res = CRUDUtil.execute("SELECT reserved FROM room_up WHERE id=?", id);
        if (res.next()) {
            if (res.getBoolean("reserved")) {
                return CRUDUtil.execute("UPDATE room_up SET reserved=? WHERE id=?",
                        false, id);
            } else {
                return CRUDUtil.execute("UPDATE room_up SET reserved=? WHERE id=?",
                        true, id);
            }
        }
        return false;
    }

    public static ResultSet getLastID() throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("SELECT id FROM room_up ORDER BY id DESC LIMIT 1");
    }
}
