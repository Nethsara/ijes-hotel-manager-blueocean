package me.siyum.blueoceanhotel.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.siyum.blueoceanhotel.model.Customer;
import me.siyum.blueoceanhotel.util.Navigation;
import me.siyum.blueoceanhotel.util.Routes;
import me.siyum.blueoceanhotel.view.tm.CustomerTm;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class CustomerManagerFormController {
    public TextField txtName;
    public TextField txtNIC;
    public TextField txtPhone;
    public TextField txtEmail;
    public TextField txtAddress;
    public TextField txtSearch;
    public JFXButton btnSaveCustomer;
    public TableColumn colName;
    public TableColumn colID;
    public TableColumn colPhone;
    public TableColumn colNIC;
    public TableColumn colEmail;
    public TableColumn colAddress;
    public TableColumn colActions;
    public JFXButton btnNewCustomer;
    public Circle btnClose;
    public Circle btnMinimize;
    public TableView<CustomerTm> tblCustomers;
    public Label lblDateTime;

    private String searchText = "";

    public void initialize() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colActions.setCellValueFactory(new PropertyValueFactory<>("btn"));
        setDateAndTime();
        searchCustomers(searchText);
        tblCustomers.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (null != newValue) {// newValue!=null
                        setData(newValue);
                    }
                });
        txtSearch.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    searchText = newValue;
                    searchCustomers(searchText);
                });
    }

    private void setDateAndTime(){
        Timeline time = new Timeline(
                new KeyFrame(Duration.ZERO, e->{
                    DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    lblDateTime.setText(LocalDateTime.now().format(formatter));
                }), new KeyFrame(Duration.seconds(1)));
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

    private void setData(CustomerTm tm) {
        txtName.setText(tm.getName());
        txtAddress.setText(tm.getAddress());
        txtNIC.setText(tm.getNic());
        txtEmail.setText(tm.getEmail());
        txtPhone.setText(tm.getPhone());
        btnSaveCustomer.setText("Update Customer");
    }

    private void searchCustomers(String text) {
        String searchText = "%" + text + "%";
        try {

            ObservableList<CustomerTm> tmList = FXCollections.observableArrayList();
            ResultSet set = CustomerController.searchText(searchText);
            while (set.next()) {
                Button btn = new Button("Edit");
                CustomerTm tm = new CustomerTm(
                        set.getString(1),
                        set.getString(2),
                        set.getString(3),
                        set.getString(4),
                        set.getString(5),
                        set.getString(6),
                        btn
                );
                tmList.add(tm);
                btn.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "are you sure whether do you want to delete this Customer?",
                            ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get() == ButtonType.YES) {
                        try {
                            boolean isDeleted = CustomerController.deleteCustomer(tm.getId());
                            if (isDeleted) {
                                searchCustomers(searchText);
                                new Alert(Alert.AlertType.INFORMATION, "Customer Deleted!").show();
                            } else {
                                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                            }
                        } catch (ClassNotFoundException | SQLException e) {
                            new Alert(Alert.AlertType.ERROR, "DB Loading Error, Please contact system admin");
                        }
                    }
                });
            }
            tblCustomers.setItems(tmList);

        } catch (ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR, "DB Loading Error, Please contact system admin");
        }

    }

    public void saveCustomerOnAction(ActionEvent actionEvent) {
        Customer r = new Customer(
                generateID(),
                txtName.getText(),
                txtNIC.getText(),
                txtPhone.getText(),
                txtEmail.getText(),
                txtAddress.getText()
        );
        if (btnSaveCustomer.getText().equalsIgnoreCase("Save Customer")) {
            try {
                boolean isSavedCust = CustomerController.saveCustomer(r);
                if (isSavedCust) {
                    searchCustomers(searchText);
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Room Saved!").show();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                }
            } catch (ClassNotFoundException | SQLException e) {
                new Alert(Alert.AlertType.ERROR, "DB Loading Error, Please contact system admin");
            }
        } else {
            try {
                boolean isUpdatedCust = CustomerController.updateCustomer(r);
                if (isUpdatedCust) {
                    searchCustomers(searchText);
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Customer Updated!").show();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again! Update Error").show();
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void newCustomerOnAction(ActionEvent actionEvent) {
        btnSaveCustomer.setText("Save Customer");
    }

    private String generateID() {
        ResultSet set = null;
        try {
            set = CustomerController.getLastID();
            if (set.next()) {
                String tempOrderId = set.getString(1);
                String[] array = tempOrderId.split("-");//[D,3]
                int tempNumber = Integer.parseInt(array[1]);
                int finalizeOrderId = tempNumber + 1;
                return String.format("C-%03d", finalizeOrderId);
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Loading Error, Please contact system admin");
        }
        return "C-001";
    }

    public void clearFields() {
        txtPhone.clear();
        txtEmail.clear();
        txtNIC.clear();
        txtAddress.clear();
        txtName.clear();
    }

    public void closeOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    public void minimizeOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnMinimize.getScene().getWindow();
        stage.setIconified(true);
    }

    public void roomsManagerOnAction(MouseEvent mouseEvent) {
        try {
            Navigation.navigate(Routes.STAFFROOM, mouseEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading UI");
        }
    }

    public void logoutOnAction(MouseEvent mouseEvent) {
        try {
            Navigation.navigate(Routes.LOGIN, mouseEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading UI");
        }
    }

    public void roomManagerOnAction(MouseEvent mouseEvent) {
        try {
            Navigation.navigate(Routes.STAFFROOM, mouseEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading UI");
        }
    }


    public void makeAReservationOnAction(MouseEvent mouseEvent) {
        try {
            Navigation.navigate(Routes.RESERVATIONS, mouseEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading UI");
        }
    }

    public void staffDashboardOnAction(MouseEvent mouseEvent) {
        try {
            Navigation.navigate(Routes.STAFF, mouseEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading UI");
        }
    }
}
