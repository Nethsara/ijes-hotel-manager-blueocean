package me.siyum.blueoceanhotel.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import me.siyum.blueoceanhotel.model.Meal;
import me.siyum.blueoceanhotel.util.CRUDUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MealsController {
    public static ResultSet searchText(String searchText) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("SELECT * FROM meals WHERE id LIKE ? || type LIKE ?", searchText, searchText);
    }

    public static boolean deleteMeal(String getmealID) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("DELETE FROM meals WHERE id=?", getmealID);
    }

    public static boolean saveMeal(Meal r) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("INSERT INTO meals VALUES(?,?,?)",
                r.getId(),  r.getPrice(),r.getType());
    }

    public static boolean updateMeal(Meal r) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("UPDATE meals SET type=?,price=?,type=? WHERE id=?",
                r.getType(), r.getPrice(), r.getType(), r.getId());
    }

    public static ObservableList<String> getAllMeals() throws SQLException, ClassNotFoundException {
        ResultSet set = CRUDUtil.execute("SELECT id FROM meals");

        ArrayList<String> idList = new ArrayList<>();

        while (set.next()) {
            idList.add(set.getString("id"));
        }
        return FXCollections.observableArrayList(idList);
    }

    public static ResultSet getLastID() throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("SELECT id FROM meals ORDER BY id DESC LIMIT 1");
    }
}
