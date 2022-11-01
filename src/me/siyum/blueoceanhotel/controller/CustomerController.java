package me.siyum.blueoceanhotel.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import me.siyum.blueoceanhotel.model.Customer;
import me.siyum.blueoceanhotel.util.CRUDUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class CustomerController {

    public static ResultSet searchText(String searchText) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("SELECT * FROM customers WHERE id LIKE ? || name LIKE ?", searchText, searchText);
    }

    public static ResultSet getLastID() throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("SELECT id FROM customers ORDER BY id DESC LIMIT 1");
    }

    public static boolean saveCustomer(Customer r) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("INSERT INTO customers VALUES(?,?,?,?,?,?)",
                r.getId(), r.getName(), r.getNic(), r.getPhone(), r.getEmail(), r.getAddress());
    }

    public static boolean updateCustomer(Customer r) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("UPDATE customers SET name=?,nic=?,phone=?,email=?, address=? WHERE id=?",
                r.getName(), r.getNic(), r.getPhone(), r.getEmail(), r.getAddress(), r.getId());
    }

    public static boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("DELETE FROM customers WHERE id=?", id);
    }

    public static ObservableList<String> getAllAvailableRooms() throws SQLException, ClassNotFoundException {
        ResultSet set = CRUDUtil.execute("SELECT id FROM customers");

        ArrayList<String> idList = new ArrayList<>();

        while (set.next()) {
            idList.add(set.getString("id"));
        }
        return FXCollections.observableArrayList(idList);
    }

    public static boolean validateEmail(String email) {
        if(email==null || email.isEmpty()){
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern emailPattern =Pattern.compile(emailRegex);

        if(emailPattern.matcher(email).matches()){
            return true;
        }else {
            return false;
        }
    }

    public static boolean validateNIC(String nic){
        if(nic.length() == 12) return true;
        return false;
    }

    public static boolean validatePhone(String phone) {
        if(phone==null || phone.isEmpty()){
            return false;
        }
        String phoneRegex = "^(075|077|071)([0-9]{7})$";
        Pattern phonePattern =Pattern.compile(phoneRegex);

        if(phonePattern.matcher(phone).matches()){
            return true;
        }else {
            return false;
        }
    }

    public static boolean validateName(String name) {
        return !name.isEmpty();
    }

    public static boolean validateAddress(String address) {
        return !address.isEmpty();
    }
}
